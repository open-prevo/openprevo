package ch.prevo.open.node.adapter.excel;

import ch.prevo.open.data.api.JobInfo;
import ch.prevo.open.encrypted.model.CapitalTransferInformation;
import ch.prevo.open.encrypted.model.CommencementMatchNotification;
import ch.prevo.open.node.data.provider.MatchNotificationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;

public class NotificationListener implements MatchNotificationListener {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationListener.class);

    private final PrintWriter writer = new PrintWriter(System.out);
    private final NotificationWriter notificationWriter = new NotificationWriter();

    @Override
    public String handleMatchForEmploymentCommencement(CommencementMatchNotification matchNotification, JobInfo jobInfo) {
        try {
            notificationWriter.write(writer, matchNotification, jobInfo);
        } catch (IOException e) {
            LOG.error("Exception while trying to write notification for employment commencement match");
            return "Error";
        }
        return "Success";
    }

    @Override
    public String handleMatchForEmploymentTermination(CommencementMatchNotification matchNotification, CapitalTransferInformation capitalTransferInfo, JobInfo jobInfo) {
        try {
            notificationWriter.write(writer, matchNotification, capitalTransferInfo, jobInfo);
        } catch (IOException e) {
            LOG.error("Exception while trying to write notification for employment termination match");
            return "Error";
        }
        return "Success";
    }
}
