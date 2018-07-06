package ch.prevo.open.node.services;

import ch.prevo.open.encrypted.model.CapitalTransferInformation;
import ch.prevo.open.encrypted.model.CommencementMatchNotification;
import ch.prevo.open.encrypted.model.TerminationMatchNotification;
import ch.prevo.open.node.data.provider.MatchNotificationListener;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class MatchNotificationService {

    private final MatchNotificationListener listener;

    @Inject
    public MatchNotificationService(MatchNotificationListener listener) {
        this.listener = listener;
    }

    public void handleCommencementMatch(CommencementMatchNotification notification) {
        listener.handleCommencementMatch(notification);
    }

    public CapitalTransferInformation handleTerminationMatch(TerminationMatchNotification notification) {
        return listener.handleTerminationMatch(notification);
    }
}
