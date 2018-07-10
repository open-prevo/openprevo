package ch.prevo.open.node.data.provider;

import ch.prevo.open.encrypted.model.CommencementMatchNotification;
import ch.prevo.open.encrypted.model.TerminationMatchNotification;

public interface MatchNotificationListener {

    void handleTerminationMatch(TerminationMatchNotification notification);

    void handleCommencementMatch(CommencementMatchNotification notification);

}
