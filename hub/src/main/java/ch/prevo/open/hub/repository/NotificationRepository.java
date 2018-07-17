package ch.prevo.open.hub.repository;

import ch.prevo.open.encrypted.model.MatchForCommencement;
import ch.prevo.open.encrypted.model.MatchForTermination;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class NotificationRepository {

    private final List<MatchForTermination> matchesForTerminations = Collections
            .synchronizedList(new ArrayList<>());

    private final List<MatchForCommencement> matchesForCommencements = Collections
            .synchronizedList(new ArrayList<>());

    public void saveMatchForTermination(MatchForTermination notification) {
        this.matchesForTerminations.add(notification);
    }

    public void saveMatchForCommencement(MatchForCommencement notification) {
        this.matchesForCommencements.add(notification);
    }

    public boolean isMatchForTerminationAlreadyNotified(MatchForTermination notification) {
        return matchesForTerminations.contains(notification);
    }

    public boolean isMatchForCommencementAlreadyNotified(MatchForCommencement notification) {
        return matchesForCommencements.contains(notification);
    }
}
