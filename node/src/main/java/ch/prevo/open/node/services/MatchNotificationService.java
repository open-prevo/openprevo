package ch.prevo.open.node.services;

import ch.prevo.open.data.api.JobInfo;
import ch.prevo.open.data.api.JobStart;
import ch.prevo.open.encrypted.model.CapitalTransferInformation;
import ch.prevo.open.encrypted.model.CommencementMatchNotification;
import ch.prevo.open.encrypted.model.TerminationMatchNotification;
import ch.prevo.open.node.config.AdapterServiceConfiguration;
import ch.prevo.open.node.data.provider.JobStartProvider;
import ch.prevo.open.node.data.provider.MatchNotificationListener;
import ch.prevo.open.node.data.provider.ProviderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.serviceloader.ServiceListFactoryBean;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class MatchNotificationService {

    private static final Logger LOG = LoggerFactory.getLogger(MatchNotificationService.class);

    private final MatchNotificationListener listener;

    private final JobStartProvider jobStartProvider;

    @Inject
    public MatchNotificationService(ServiceListFactoryBean factoryBean) {
        final ProviderFactory factory = AdapterServiceConfiguration.getAdapterService(factoryBean);
        this.jobStartProvider = factory != null? factory.getJobStartProvider() : null;
        this.listener = factory != null? factory.getMatchNotificationListener() : null;
    }

    public void handleCommencementMatch(CommencementMatchNotification notification) {
        listener.handleCommencementMatch(notification);
    }

    public CapitalTransferInformation handleTerminationMatch(TerminationMatchNotification notification) {
        listener.handleTerminationMatch(notification);

        CapitalTransferInformation transferInformation = jobStartProvider.getJobStarts().stream()
                .filter(j -> isSameAsNotification(j, notification)).findAny()
                .map(JobStart::getCapitalTransferInfo).orElse(null);

        if (transferInformation == null) {
            LOG.warn("No CaptialTransferInformation returned for notification " + notification);
        }

        return transferInformation;
    }

    private boolean isSameAsNotification(JobStart jobStart, TerminationMatchNotification notification) {
        JobInfo jobInfo = jobStart.getJobInfo();
        return jobInfo.getOasiNumber().equals(notification.getEncryptedOasiNumber()) &&
                jobInfo.getRetirementFundUid().equals(notification.getRetirementFundUid()) &&
                jobInfo.getDate().equals(notification.getEntryDate());
    }
}
