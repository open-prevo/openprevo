package ch.prevo.open.node.data.provider.dummy;

import ch.prevo.open.data.api.FullCommencementNotification;
import ch.prevo.open.data.api.FullTerminationNotification;
import ch.prevo.open.node.data.provider.MatchNotificationListener;

import java.io.PrintWriter;

public class DefaultMatchNotificationListener implements MatchNotificationListener {

    private final PrintWriter writer = new PrintWriter(System.out);
    private final NotificationWriter notificationWriter = new NotificationWriter();

    @Override
    public void handleTerminationMatch(FullTerminationNotification notification) {
        notificationWriter.write(writer, notification);
    }

    @Override
    public void handleCommencementMatch(FullCommencementNotification notification) {
        notificationWriter.write(writer, notification);
    }
}
