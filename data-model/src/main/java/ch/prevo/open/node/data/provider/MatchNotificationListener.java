package ch.prevo.open.node.data.provider;

import ch.prevo.open.encrypted.model.CapitalTransferInformation;
import ch.prevo.open.encrypted.model.TerminationMatchNotification;
import ch.prevo.open.encrypted.model.CommencementMatchNotification;

public interface MatchNotificationListener {

    CapitalTransferInformation handleTerminationMatch(TerminationMatchNotification notification);

    void handleCommencementMatch(CommencementMatchNotification notification);

}
