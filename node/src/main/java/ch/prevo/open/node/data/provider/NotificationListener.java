package ch.prevo.open.node.data.provider;

import java.io.PrintWriter;

import org.springframework.stereotype.Service;

import ch.prevo.open.encrypted.model.CommencementMatchNotification;
import ch.prevo.open.encrypted.model.TerminationMatchNotification;

@Service
public class NotificationListener implements MatchNotificationListener {

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
