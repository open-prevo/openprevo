package ch.prevo.open.node.services;

import ch.prevo.open.data.api.FullCommencementNotification;
import ch.prevo.open.data.api.FullTerminationNotification;
import ch.prevo.open.data.api.EmploymentTermination;
import ch.prevo.open.data.api.JobInfo;
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

    private final EmploymentCommencementProvider jobStartProvider;
    private final EmploymentTerminationProvider jobEndProvider;

    @Inject
    public MatchNotificationService(ServiceListFactoryBean factoryBean) {
        final ProviderFactory factory = AdapterServiceConfiguration.getAdapterService(factoryBean);
        this.jobStartProvider = factory != null ? factory.getEmploymentCommencementProvider() : null;
        this.jobEndProvider = factory != null ? factory.getEmploymentTerminationProvider() : null;
        this.listener = factory != null ? factory.getMatchNotificationListener() : null;
    }

    public void handleCommencementMatch(CommencementMatchNotification notification) {
        final Optional<EmploymentTermination> jobEnd = jobEndProvider.getEmploymentTerminations().stream()
                .filter(currentEmploymentTermination -> isSameAsNotification(currentEmploymentTermination, notification))
                .findAny();

        if (!jobEnd.isPresent()) {
            LOG.warn("Termination notification received which does not correlate with any job start: " + notification);
            return;
        }

        final FullCommencementNotification fullNotification = new FullCommencementNotification();
        fullNotification.setNewRetirementFundUid(notification.getNewRetirementFundUid());
        fullNotification.setCommencementDate(notification.getCommencementDate());
        fullNotification.setTransferInformation(notification.getTransferInformation());
        fullNotification.setEmploymentTermination(jobEnd.get());

        listener.handleCommencementMatch(fullNotification);
    }

    public Optional<CapitalTransferInformation> handleTerminationMatch(TerminationMatchNotification notification) {
        final Optional<EmploymentCommencement> jobStart = jobStartProvider.getEmploymentCommencements().stream()
                .filter(currentEmploymentCommencement -> isSameAsNotification(currentEmploymentCommencement, notification))
                .findAny();

        if (!jobStart.isPresent()) {
            LOG.warn("Termination notification received which does not correlate with any job start: " + notification);
            return Optional.empty();
        }

        final FullTerminationNotification fullNotification = new FullTerminationNotification();
        fullNotification.setPreviousRetirementFundUid(notification.getPreviousRetirementFundUid());
        fullNotification.setTerminationDate(notification.getTerminationDate());
        fullNotification.setEmploymentCommencement(jobStart.get());

        listener.handleTerminationMatch(fullNotification);

        return jobStart.map(EmploymentCommencement::getCapitalTransferInfo);
    }

    private boolean isSameAsNotification(EmploymentCommencement jobStart, TerminationMatchNotification notification) {
        JobInfo jobInfo = jobStart.getJobInfo();
        return Cryptography.digestOasiNumber(jobInfo.getOasiNumber()).equals(notification.getEncryptedOasiNumber()) &&
                jobInfo.getRetirementFundUid().equals(notification.getRetirementFundUid()) &&
                jobInfo.getDate().equals(notification.getCommencementDate());
    }

    private boolean isSameAsNotification(EmploymentTermination jobEnd, CommencementMatchNotification notification) {
        JobInfo jobInfo = jobEnd.getJobInfo();
        return Cryptography.digestOasiNumber(jobInfo.getOasiNumber()).equals(notification.getEncryptedOasiNumber()) &&
                jobInfo.getRetirementFundUid().equals(notification.getPreviousRetirementFundUid()) &&
                jobInfo.getDate().equals(notification.getTerminationDate());
    }
}
