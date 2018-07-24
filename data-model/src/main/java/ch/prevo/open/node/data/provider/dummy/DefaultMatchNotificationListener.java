package ch.prevo.open.node.data.provider.dummy;

import java.io.PrintWriter;

import ch.prevo.open.data.api.FullMatchForTerminationNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.prevo.open.data.api.FullMatchForCommencementNotification;
import ch.prevo.open.node.data.provider.MatchNotificationListener;
import ch.prevo.open.node.data.provider.error.NotificationException;

public class DefaultMatchNotificationListener implements MatchNotificationListener {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultMatchNotificationListener.class);

    private final PrintWriter writer = new PrintWriter(System.out);
    private final NotificationWriter notificationWriter = new NotificationWriter();

    @Override
    public void handleMatchForCommencementNotification(FullMatchForCommencementNotification notification) throws NotificationException {
        try {
            notificationWriter.write(writer, notification);
        } catch (Exception e) {
            LOG.error("Could not handle termination match", e);
            throw new NotificationException(e);
        }
    }

    @Override
    public void handleMatchForTerminationNotification(FullMatchForTerminationNotification notification) throws NotificationException {
        try {
            notificationWriter.write(writer, notification);
        } catch (Exception e) {
            LOG.error("Could not handle commencement match", e);
            throw new NotificationException(e);
        }
    }
}
