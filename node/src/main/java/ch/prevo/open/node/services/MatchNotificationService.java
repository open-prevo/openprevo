package ch.prevo.open.node.services;

import ch.prevo.open.data.api.JobInfo;
import ch.prevo.open.encrypted.model.CapitalTransferInformation;
import ch.prevo.open.encrypted.model.CommencementMatchNotification;
import ch.prevo.open.node.data.provider.MatchNotificationListener;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class MatchNotificationService {

    private final MatchNotificationListener listener;

    @Inject
    public MatchNotificationService(MatchNotificationListener listener) {
        this.listener = listener;
    }

    public CapitalTransferInformation handleMatchForEmploymentCommencement(CommencementMatchNotification matchNotification, JobInfo jobInfo) {

    }

    public void handleMatchForEmploymentTermination(CommencementMatchNotification matchNotification, CapitalTransferInformation capitalTransferInfo, JobInfo jobInfo) {

    }
}
