package ch.prevo.open.hub;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Profile("docker")
public class HubScheduler {

    private static Logger LOGGER = LoggerFactory.getLogger(HubScheduler.class);

    @Inject
    private HubService hubService;

    @Scheduled(initialDelay = 10000, fixedDelay = 120000)
    public void run() {
        LOGGER.debug("Start matching task");
        this.hubService.matchAndNotify();
    }
}
