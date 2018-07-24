package ch.prevo.open.node.data.provider;

import ch.prevo.open.data.api.FullMatchForTerminationNotification;
import ch.prevo.open.data.api.FullMatchForCommencementNotification;
import ch.prevo.open.node.data.provider.error.NotificationException;

public interface MatchNotificationListener {

    void handleMatchForCommencementNotification(FullMatchForCommencementNotification notification) throws NotificationException;

    void handleMatchForTerminationNotification(FullMatchForTerminationNotification notification) throws NotificationException;
}
