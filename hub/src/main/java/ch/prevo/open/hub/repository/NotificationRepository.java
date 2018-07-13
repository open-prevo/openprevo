package ch.prevo.open.hub.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Repository;

import ch.prevo.open.encrypted.model.CommencementMatchNotification;
import ch.prevo.open.encrypted.model.TerminationMatchNotification;

@Repository
public class NotificationRepository {

    private final List<CommencementMatchNotification> matchedEmploymentCommencements = Collections
            .synchronizedList(new ArrayList<>());

    private final List<TerminationMatchNotification> matchedEmploymentTerminations = Collections
            .synchronizedList(new ArrayList<>());

    public void saveCommencementMatchNotification(CommencementMatchNotification notification) {
        this.matchedEmploymentCommencements.add(notification);
    }

    public void saveTerminationMatchNotification(TerminationMatchNotification notification) {
        this.matchedEmploymentTerminations.add(notification);
    }

    public boolean isCommencementMatchAlreadyNotified(CommencementMatchNotification notification) {
        return matchedEmploymentCommencements.contains(notification);
    }

    public boolean isTerminationMatchAlreadyNotified(TerminationMatchNotification notification) {
        return matchedEmploymentTerminations.contains(notification);
    }
}
