package ch.prevo.open.node.data.provider;

import ch.prevo.open.data.api.FullCommencementNotification;
import ch.prevo.open.data.api.FullTerminationNotification;
import ch.prevo.open.node.data.provider.error.NotificationException;

public interface MatchNotificationListener {

    void handleTerminationMatch(FullTerminationNotification notification) throws NotificationException;

    void handleCommencementMatch(FullCommencementNotification notification) throws NotificationException;
}
