package ch.prevo.open.hub;

import ch.prevo.open.hub.match.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;

@Component
@Profile("!test")
public class HubScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(HubScheduler.class);

    @Inject
    private HubService hubService;

    @Scheduled(initialDelay = 10000, fixedDelay = 120000)
    public void run() {
        LOGGER.info("Start matching task");
        List<Match> matches = hubService.matchAndNotify();
        LOGGER.info("Matching task ended, found {} matches", matches.size());
    }
}
