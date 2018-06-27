package ch.prevo.open.hub;

import ch.prevo.open.encrypted.model.InsurantInformation;
import ch.prevo.open.hub.match.Match;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static java.util.Collections.emptySet;

@Service
public class NodeService {

    // TODO call client

    public Set<InsurantInformation> currentExits() {
        return emptySet();
    }

    public Set<InsurantInformation> currentEntries() {
        return emptySet();
    }

    public void notify(List<Match> matches) {

    }
}
