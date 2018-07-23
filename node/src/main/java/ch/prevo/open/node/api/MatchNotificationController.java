/*******************************************************************************
 * Copyright (c) 2018 - Prevo-System AG and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * 
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License, v. 2.0 are satisfied: GNU General Public License, version 3
 * with the GNU Classpath Exception which is
 * available at https://www.gnu.org/software/classpath/license.html.
 * 
 * SPDX-License-Identifier: EPL-2.0 OR GPL-3.0 WITH Classpath-exception-2.0
 * 
 * Contributors:
 *     Prevo-System AG - initial API and implementation
 ******************************************************************************/
package ch.prevo.open.node.api;

import ch.prevo.open.encrypted.model.EncryptedData;
import ch.prevo.open.encrypted.model.MatchForCommencement;
import ch.prevo.open.encrypted.model.MatchForTermination;
import ch.prevo.open.node.data.provider.error.NotificationException;
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
import java.util.Optional;

@RestController
public class MatchNotificationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatchNotificationController.class);

    private final MatchNotificationService notificationService;

    @Inject
    public MatchNotificationController(MatchNotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RequestMapping(value = "/commencement-match-notification", method = RequestMethod.POST)
    public ResponseEntity<EncryptedData> receiveCommencementMatchNotification(@RequestBody MatchForCommencement matchNotification) {
        LOGGER.debug("Receive commencement match notification for OASI {}, switching to new retirement fund: {}",
                matchNotification.getEncryptedOasiNumber(), matchNotification.getNewRetirementFundUid());

        try {
            final Optional<EncryptedData> transferInformation = notificationService
                    .handleTerminationMatch(matchNotification);

            return transferInformation.isPresent() ?
                    ResponseEntity.status(HttpStatus.OK).body(transferInformation.get()) :
                    ResponseEntity.notFound().build();

        } catch (NotificationException e) {
            LOGGER.error("Could not process commencement notification", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(value = "/termination-match-notification", method = RequestMethod.POST)
    public ResponseEntity<Void> receiveMatchForCommencement(@RequestBody MatchForTermination matchNotification) {
        LOGGER.debug("Receive termination match notification for OASI {}, switching to new retirement fund: {}",
                matchNotification.getEncryptedOasiNumber(), matchNotification.getNewRetirementFundUid());

        try {
            notificationService.handleCommencementMatch(matchNotification);
        } catch (NotificationException e) {
            LOGGER.error("Could not process commencement notification", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
