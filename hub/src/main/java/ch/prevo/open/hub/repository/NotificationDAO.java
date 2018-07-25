/*============================================================================*
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
 *===========================================================================*/
package ch.prevo.open.hub.repository;

import java.util.Optional;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import ch.prevo.open.encrypted.model.MatchForCommencement;
import ch.prevo.open.encrypted.model.MatchForTermination;
import ch.prevo.open.encrypted.model.MatchNotification;

@Service
public class NotificationDAO {

    private final NotificationRepository repository;

    @Inject
    public NotificationDAO(NotificationRepository repository) {
        this.repository = repository;
    }

    public void saveMatchForTermination(MatchForTermination notification) {
        final NotificationDTO dto = findByNotification(notification)
                .orElseGet(() -> new NotificationDTO(notification));

        dto.setMatchForTerminationNotified(true);
        repository.save(dto);
    }

    public void saveMatchForCommencement(MatchForCommencement notification) {
        final NotificationDTO dto = findByNotification(notification)
                .orElseGet(
                        () -> new NotificationDTO(notification)
                );

        dto.setMatchForCommencementNotified(true);
        repository.save(dto);
    }

    public boolean isMatchForTerminationAlreadyNotified(MatchForTermination notification) {
        return findByNotification(notification)
                .map(NotificationDTO::isMatchForTerminationNotified)
                .orElse(false);
    }

    public boolean isMatchForCommencementAlreadyNotified(MatchForCommencement notification) {
        return findByNotification(notification)
                .map(NotificationDTO::isMatchForCommencementNotified)
                .orElse(false);
    }

    private Optional<NotificationDTO> findByNotification(MatchNotification notification) {
        return repository.findByEncryptedOasiNumberAndPreviousRetirementFundUidAndNewRetirementFundUidAndCommencementDateAndTerminationDate(
                notification.getEncryptedOasiNumber(),
                notification.getPreviousRetirementFundUid(),
                notification.getNewRetirementFundUid(),
                notification.getCommencementDate(),
                notification.getTerminationDate()
        );
    }
}
