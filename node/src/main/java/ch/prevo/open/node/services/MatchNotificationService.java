package ch.prevo.open.node.services;

import ch.prevo.open.data.api.FullCommencementNotification;
import ch.prevo.open.data.api.FullTerminationNotification;
import ch.prevo.open.data.api.EmploymentTermination;
import ch.prevo.open.data.api.EmploymentInfo;
import ch.prevo.open.data.api.EmploymentCommencement;
import ch.prevo.open.encrypted.model.CapitalTransferInformation;
import ch.prevo.open.encrypted.model.MatchForTermination;
import ch.prevo.open.encrypted.model.MatchForCommencement;
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

    private final EmploymentCommencementProvider employmentCommencementProvider;
    private final EmploymentTerminationProvider employmentTerminationProvider;

    @Inject
    public MatchNotificationService(ServiceListFactoryBean factoryBean) {
        final ProviderFactory factory = AdapterServiceConfiguration.getAdapterService(factoryBean);
        this.employmentCommencementProvider = factory != null ? factory.getEmploymentCommencementProvider() : null;
        this.employmentTerminationProvider = factory != null ? factory.getEmploymentTerminationProvider() : null;
        this.listener = factory != null ? factory.getMatchNotificationListener() : null;
    }

    public void handleCommencementMatch(MatchForTermination notification) {
        final Optional<EmploymentTermination> employmentTermination = employmentTerminationProvider.getEmploymentTerminations().stream()
                .filter(currentEmploymentTermination -> isSameAsNotification(currentEmploymentTermination, notification))
                .findAny();

        if (!employmentTermination.isPresent()) {
            LOG.warn("Termination notification received which does not correlate with any employment start: " + notification);
            return;
        }

        final FullCommencementNotification fullNotification = new FullCommencementNotification();
        fullNotification.setNewRetirementFundUid(notification.getNewRetirementFundUid());
        fullNotification.setCommencementDate(notification.getCommencementDate());
        fullNotification.setTransferInformation(notification.getTransferInformation());
        fullNotification.setEmploymentTermination(employmentTermination.get());

        listener.handleCommencementMatch(fullNotification);
    }

    public Optional<CapitalTransferInformation> handleTerminationMatch(MatchForCommencement notification) {
        final Optional<EmploymentCommencement> employmentCommencement = employmentCommencementProvider.getEmploymentCommencements().stream()
                .filter(currentEmploymentCommencement -> isSameAsNotification(currentEmploymentCommencement, notification))
                .findAny();

        if (!employmentCommencement.isPresent()) {
            LOG.warn("Termination notification received which does not correlate with any employment start: " + notification);
            return Optional.empty();
        }

        final FullTerminationNotification fullNotification = new FullTerminationNotification();
        fullNotification.setPreviousRetirementFundUid(notification.getPreviousRetirementFundUid());
        fullNotification.setTerminationDate(notification.getTerminationDate());
        fullNotification.setEmploymentCommencement(employmentCommencement.get());

        listener.handleTerminationMatch(fullNotification);

        return employmentCommencement.map(EmploymentCommencement::getCapitalTransferInfo);
    }

    private boolean isSameAsNotification(EmploymentCommencement employmentCommencement, MatchForCommencement notification) {
        EmploymentInfo employmentInfo = employmentCommencement.getEmploymentInfo();
        return Cryptography.digestOasiNumber(employmentInfo.getOasiNumber()).equals(notification.getEncryptedOasiNumber()) &&
                employmentInfo.getRetirementFundUid().equals(notification.getRetirementFundUid()) &&
                employmentInfo.getDate().equals(notification.getCommencementDate());
    }

    private boolean isSameAsNotification(EmploymentTermination employmentTermination, MatchForTermination notification) {
        EmploymentInfo employmentInfo = employmentTermination.getEmploymentInfo();
        return Cryptography.digestOasiNumber(employmentInfo.getOasiNumber()).equals(notification.getEncryptedOasiNumber()) &&
                employmentInfo.getRetirementFundUid().equals(notification.getPreviousRetirementFundUid()) &&
                employmentInfo.getDate().equals(notification.getTerminationDate());
    }
}