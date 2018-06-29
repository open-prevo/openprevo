package ch.prevo.open.hub;

import ch.prevo.open.encrypted.model.InsurantInformation;
import ch.prevo.open.hub.match.Match;
import ch.prevo.open.hub.match.MatcherService;
import ch.prevo.open.hub.nodes.NodeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;

@Service
public class HubService {

    private static Logger LOGGER = LoggerFactory.getLogger(HubService.class);

    @Inject
    private MatcherService matcher;

    @Inject
    private NodeService nodeService;

    void matchAndNotify() {
        Set<InsurantInformation> entries = nodeService.getCurrentEntries();
        Set<InsurantInformation> exits = nodeService.getCurrentExits();

        List<Match> matches = matcher.findMatches(exits, entries);

        LOGGER.debug("Found {} matches", matches.size());

        nodeService.notifyMatches(matches);
    }

}
