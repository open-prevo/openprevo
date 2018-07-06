package ch.prevo.open.node.data.provider;

import ch.prevo.open.encrypted.model.CapitalTransferInformation;
import ch.prevo.open.encrypted.model.TerminationMatchNotification;
import ch.prevo.open.encrypted.model.CommencementMatchNotification;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;

@Service
public class NotificationListener implements MatchNotificationListener {

    private final PrintWriter writer = new PrintWriter(System.out);
    private final NotificationWriter notificationWriter = new NotificationWriter();

    @Override
    public CapitalTransferInformation handleTerminationMatch(TerminationMatchNotification notification) {
        notificationWriter.write(writer, notification);

        // TODO: Lookup information and replace hard-coded data
        return new CapitalTransferInformation("BKB", "CH53 0077 0015 0222 3334 4");
    }

    @Override
    public void handleCommencementMatch(CommencementMatchNotification notification) {
        notificationWriter.write(writer, notification);
    }
}
