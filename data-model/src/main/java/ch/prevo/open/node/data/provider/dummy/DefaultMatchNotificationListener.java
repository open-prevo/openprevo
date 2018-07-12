package ch.prevo.open.node.data.provider.dummy;

import ch.prevo.open.encrypted.model.CommencementMatchNotification;
import ch.prevo.open.encrypted.model.TerminationMatchNotification;
import ch.prevo.open.node.data.provider.MatchNotificationListener;

import java.io.PrintWriter;

public class DefaultMatchNotificationListener implements MatchNotificationListener {

    private final PrintWriter writer = new PrintWriter(System.out);
    private final NotificationWriter notificationWriter = new NotificationWriter();

    @Override
    public void handleTerminationMatch(TerminationMatchNotification notification) {
        notificationWriter.write(writer, notification);
    }

    @Override
    public void handleCommencementMatch(CommencementMatchNotification notification) {
        notificationWriter.write(writer, notification);
    }
}
