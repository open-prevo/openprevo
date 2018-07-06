package ch.prevo.open.node.api;

import ch.prevo.open.encrypted.model.CapitalTransferInformation;
import ch.prevo.open.encrypted.model.CommencementMatchNotification;
import ch.prevo.open.encrypted.model.TerminationMatchNotification;
import ch.prevo.open.node.services.MatchNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
public class MatchNotificationController {

    private static Logger LOGGER = LoggerFactory.getLogger(MatchNotificationController.class);

    private final MatchNotificationService notificationService;

    @Inject
    public MatchNotificationController(MatchNotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RequestMapping(value = "/commencement-match-notification", method = RequestMethod.POST)
    public void receiveCommencementMatchNotification(@RequestBody CommencementMatchNotification matchNotification) {
        LOGGER.debug("Receive commencement match notification for OASI {}, switching to new retirement fund: {}",
                matchNotification.getEncryptedOasiNumber(), matchNotification.getRetirementFundUid());

        notificationService.handleCommencementMatch(matchNotification);
    }

    @RequestMapping(value = "/termination-match-notification", method = RequestMethod.POST)
    public ResponseEntity<CapitalTransferInformation> receiveTerminationMatchNotification(@RequestBody TerminationMatchNotification matchNotification) {
        LOGGER.debug("Receive termination match notification for OASI {}, switching to new retirement fund: {}",
                matchNotification.getEncryptedOasiNumber(), matchNotification.getNewRetirementFundUid());

        final CapitalTransferInformation transferInformation = notificationService.handleTerminationMatch(matchNotification);

        return ResponseEntity.status(HttpStatus.OK).body(transferInformation);
    }
}
