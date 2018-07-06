package ch.prevo.open.node.data.provider;

import ch.prevo.open.data.api.JobInfo;
import ch.prevo.open.encrypted.model.CapitalTransferInformation;
import ch.prevo.open.encrypted.model.CommencementMatchNotification;

public interface MatchNotificationListener {

    CapitalTransferInformation handleMatchForEmploymentCommencement(CommencementMatchNotification matchNotification, JobInfo jobInfo);

    void handleMatchForEmploymentTermination(CommencementMatchNotification matchNotification, CapitalTransferInformation capitalTransferInfo, JobInfo jobInfo);

}
