package ch.prevo.open.node.services;

import ch.prevo.open.data.api.FullCommencementNotification;
import ch.prevo.open.data.api.FullTerminationNotification;
import ch.prevo.open.data.api.JobEnd;
import ch.prevo.open.data.api.JobInfo;
import ch.prevo.open.data.api.JobStart;
import ch.prevo.open.encrypted.model.CapitalTransferInformation;
import ch.prevo.open.encrypted.model.CommencementMatchNotification;
import ch.prevo.open.encrypted.model.TerminationMatchNotification;
import ch.prevo.open.node.config.AdapterServiceConfiguration;
import ch.prevo.open.node.data.provider.JobEndProvider;
import ch.prevo.open.node.data.provider.JobStartProvider;
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

    private final JobStartProvider jobStartProvider;
    private final JobEndProvider jobEndProvider;

    @Inject
    public MatchNotificationService(ServiceListFactoryBean factoryBean) {
        final ProviderFactory factory = AdapterServiceConfiguration.getAdapterService(factoryBean);
        this.jobStartProvider = factory != null ? factory.getJobStartProvider() : null;
        this.jobEndProvider = factory != null ? factory.getJobEndProvider() : null;
        this.listener = factory != null ? factory.getMatchNotificationListener() : null;
    }

    public void handleCommencementMatch(CommencementMatchNotification notification) {
        final Optional<JobEnd> jobEnd = jobEndProvider.getJobEnds().stream()
                .filter(currentJobEnd -> isSameAsNotification(currentJobEnd, notification))
                .findAny();

        if (!jobEnd.isPresent()) {
            LOG.warn("Termination notification received which does not correlate with any job start: " + notification);
            return;
        }

        final FullCommencementNotification fullNotification = new FullCommencementNotification();
        fullNotification.setNewRetirementFundUid(notification.getNewRetirementFundUid());
        fullNotification.setCommencementDate(notification.getCommencementDate());
        fullNotification.setTransferInformation(notification.getTransferInformation());
        fullNotification.setJobEnd(jobEnd.get());

        listener.handleCommencementMatch(fullNotification);
    }

    public Optional<CapitalTransferInformation> handleTerminationMatch(TerminationMatchNotification notification) {
        final Optional<JobStart> jobStart = jobStartProvider.getJobStarts().stream()
                .filter(currentJobStart -> isSameAsNotification(currentJobStart, notification))
                .findAny();

        if (!jobStart.isPresent()) {
            LOG.warn("Termination notification received which does not correlate with any job start: " + notification);
            return Optional.empty();
        }

        final FullTerminationNotification fullNotification = new FullTerminationNotification();
        fullNotification.setPreviousRetirementFundUid(notification.getPreviousRetirementFundUid());
        fullNotification.setTerminationDate(notification.getTerminationDate());
        fullNotification.setJobStart(jobStart.get());

        listener.handleTerminationMatch(fullNotification);

        return jobStart.map(JobStart::getCapitalTransferInfo);
    }

    private boolean isSameAsNotification(JobStart jobStart, TerminationMatchNotification notification) {
        JobInfo jobInfo = jobStart.getJobInfo();
        return jobInfo.getOasiNumber().equals(notification.getEncryptedOasiNumber()) &&
                jobInfo.getRetirementFundUid().equals(notification.getRetirementFundUid()) &&
                jobInfo.getDate().equals(notification.getCommencementDate());
    }

    private boolean isSameAsNotification(JobEnd jobEnd, CommencementMatchNotification notification) {
        JobInfo jobInfo = jobEnd.getJobInfo();
        return jobInfo.getOasiNumber().equals(notification.getEncryptedOasiNumber()) &&
                jobInfo.getRetirementFundUid().equals(notification.getPreviousRetirementFundUid()) &&
                jobInfo.getDate().equals(notification.getTerminationDate());
    }
}
