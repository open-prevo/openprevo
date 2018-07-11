package ch.prevo.open.node.data.provider;

import ch.prevo.open.data.api.FullCommencementNotification;
import ch.prevo.open.data.api.FullTerminationNotification;

public interface MatchNotificationListener {

    void handleTerminationMatch(FullTerminationNotification notification);

    void handleCommencementMatch(FullCommencementNotification notification);

}
