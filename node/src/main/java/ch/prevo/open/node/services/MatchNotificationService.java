package ch.prevo.open.node.services;

import ch.prevo.open.data.api.FullCommencementNotification;
import ch.prevo.open.data.api.FullTerminationNotification;
import ch.prevo.open.data.api.EmploymentTermination;
import ch.prevo.open.data.api.EmploymentInfo;
import ch.prevo.open.data.api.EmploymentCommencement;
import ch.prevo.open.encrypted.model.CapitalTransferInformation;
import ch.prevo.open.encrypted.model.CommencementMatchNotification;
import ch.prevo.open.encrypted.model.TerminationMatchNotification;
import ch.prevo.open.encrypted.services.Cryptography;
import ch.prevo.open.node.config.AdapterServiceConfiguration;
import ch.prevo.open.node.data.provider.EmploymentTerminationProvider;
import ch.prevo.open.node.data.provider.EmploymentCommencementProvider;
import ch.prevo.open.node.data.provider.MatchNotificationListener;
import ch.prevo.open.node.data.provider.ProviderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.serviceloader.ServiceListFactoryBean;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Optional;

@Service
public class MatchNotificationService {

    private static final Logger LOG = LoggerFactory.getLogger(MatchNotificationService.class);

    private final MatchNotificationListener listener;

    private final EmploymentCommencementProvider employmentStartProvider;
    private final EmploymentTerminationProvider employmentEndProvider;

    @Inject
    public MatchNotificationService(ServiceListFactoryBean factoryBean) {
        final ProviderFactory factory = AdapterServiceConfiguration.getAdapterService(factoryBean);
        this.employmentStartProvider = factory != null ? factory.getEmploymentCommencementProvider() : null;
        this.employmentEndProvider = factory != null ? factory.getEmploymentTerminationProvider() : null;
        this.listener = factory != null ? factory.getMatchNotificationListener() : null;
    }

    public void handleCommencementMatch(CommencementMatchNotification notification) {
        final Optional<EmploymentTermination> employmentEnd = employmentEndProvider.getEmploymentTerminations().stream()
                .filter(currentEmploymentTermination -> isSameAsNotification(currentEmploymentTermination, notification))
                .findAny();

        if (!employmentEnd.isPresent()) {
            LOG.warn("Termination notification received which does not correlate with any employment start: " + notification);
            return;
        }

        final FullCommencementNotification fullNotification = new FullCommencementNotification();
        fullNotification.setNewRetirementFundUid(notification.getNewRetirementFundUid());
        fullNotification.setCommencementDate(notification.getCommencementDate());
        fullNotification.setTransferInformation(notification.getTransferInformation());
        fullNotification.setEmploymentTermination(employmentEnd.get());

        listener.handleCommencementMatch(fullNotification);
    }

    public Optional<CapitalTransferInformation> handleTerminationMatch(TerminationMatchNotification notification) {
        final Optional<EmploymentCommencement> employmentStart = employmentStartProvider.getEmploymentCommencements().stream()
                .filter(currentEmploymentCommencement -> isSameAsNotification(currentEmploymentCommencement, notification))
                .findAny();

        if (!employmentStart.isPresent()) {
            LOG.warn("Termination notification received which does not correlate with any employment start: " + notification);
            return Optional.empty();
        }

        final FullTerminationNotification fullNotification = new FullTerminationNotification();
        fullNotification.setPreviousRetirementFundUid(notification.getPreviousRetirementFundUid());
        fullNotification.setTerminationDate(notification.getTerminationDate());
        fullNotification.setEmploymentCommencement(employmentStart.get());

        listener.handleTerminationMatch(fullNotification);

        return employmentStart.map(EmploymentCommencement::getCapitalTransferInfo);
    }

    private boolean isSameAsNotification(EmploymentCommencement employmentStart, TerminationMatchNotification notification) {
        EmploymentInfo employmentInfo = employmentStart.getEmploymentInfo();
        return Cryptography.digestOasiNumber(employmentInfo.getOasiNumber()).equals(notification.getEncryptedOasiNumber()) &&
                employmentInfo.getRetirementFundUid().equals(notification.getRetirementFundUid()) &&
                employmentInfo.getDate().equals(notification.getCommencementDate());
    }

    private boolean isSameAsNotification(EmploymentTermination employmentEnd, CommencementMatchNotification notification) {
        EmploymentInfo employmentInfo = employmentEnd.getEmploymentInfo();
        return Cryptography.digestOasiNumber(employmentInfo.getOasiNumber()).equals(notification.getEncryptedOasiNumber()) &&
                employmentInfo.getRetirementFundUid().equals(notification.getPreviousRetirementFundUid()) &&
                employmentInfo.getDate().equals(notification.getTerminationDate());
    }
}
