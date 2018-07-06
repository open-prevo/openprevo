package ch.prevo.open.node.api;

import ch.prevo.open.encrypted.model.CapitalTransferInformation;
import ch.prevo.open.encrypted.model.CommencementMatchNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MatchNotificationController {

    private static Logger LOGGER = LoggerFactory.getLogger(MatchNotificationController.class);

    @RequestMapping(value = "/match-notification", method = RequestMethod.POST)
    public ResponseEntity<String> receiveMatchNotification(@RequestBody CommencementMatchNotification matchNotification) {
        LOGGER.debug("Receive match notification for OASI {}, switching to new retirement fund: {}",
                matchNotification.getEncryptedOasiNumber(), matchNotification.getNewRetirementFundUid());
        String capitalTransferInformation = new CapitalTransferInformation("BKB", "CH53 0077 0015 0222 3334 4").toString();
        return ResponseEntity.status(HttpStatus.CREATED).body(capitalTransferInformation);
    }
}
