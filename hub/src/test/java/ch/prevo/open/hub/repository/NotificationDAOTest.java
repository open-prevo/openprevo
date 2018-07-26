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

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import javax.inject.Inject;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import ch.prevo.open.encrypted.model.EncryptedData;
import ch.prevo.open.encrypted.model.MatchForCommencement;
import ch.prevo.open.encrypted.model.MatchForTermination;

@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan
public class NotificationDAOTest {

    private final String OASI = "756.1335.5778.23";
    private final String previousRetirementFundUID = "CHE-109.740.084";
    private final String newRetirementFundUID = "CHE-109.537.488";
    private final LocalDate commencementDate = LocalDate.of(2018, 7, 1);
    private final LocalDate terminationDate = LocalDate.of(2018, 6, 30);

    @Inject
    private NotificationDAO notificationDAO;

    @Test
    public void isMatchForTerminationAlreadyNotified() {

        // given
        MatchForTermination commencementMatch = getMatchForTermination();
        MatchForTermination duplicateCommencementMatch = getMatchForTermination();

        // when
        boolean initiallyNotMatched = notificationDAO
                .isMatchForTerminationAlreadyNotified(commencementMatch);

        notificationDAO.saveMatchForTermination(commencementMatch);

        boolean commencementMatchAlreadyNotified = notificationDAO
                .isMatchForTerminationAlreadyNotified(commencementMatch);

        boolean duplicateIsAlreadyNotified = notificationDAO
                .isMatchForTerminationAlreadyNotified(duplicateCommencementMatch);

        // then
        assertThat(initiallyNotMatched).isFalse();
        assertThat(commencementMatchAlreadyNotified).isTrue();
        assertThat(duplicateIsAlreadyNotified).isTrue();

    }

    private MatchForTermination getMatchForTermination() {
        EncryptedData transferInformation = new EncryptedData(RandomUtils.nextBytes(10), RandomUtils.nextBytes(10));
        return new MatchForTermination(OASI, previousRetirementFundUID, newRetirementFundUID,
                commencementDate, terminationDate,
                transferInformation);
    }

    @Test
    public void isMatchForCommencementAlreadyNotified() {

        // given
        MatchForCommencement terminationMatch = getMatchForCommencement();
        MatchForCommencement duplicateTerminationMatch = getMatchForCommencement();

        // when
        boolean initiallyNotMatched = notificationDAO
                .isMatchForCommencementAlreadyNotified(terminationMatch);

        notificationDAO.saveMatchForCommencement(terminationMatch);

        boolean commencementMatchAlreadyNotified = notificationDAO
                .isMatchForCommencementAlreadyNotified(terminationMatch);

        boolean duplicateIsAlreadyNotified = notificationDAO
                .isMatchForCommencementAlreadyNotified(duplicateTerminationMatch);

        // then
        assertThat(initiallyNotMatched).isFalse();
        assertThat(commencementMatchAlreadyNotified).isTrue();
        assertThat(duplicateIsAlreadyNotified).isTrue();
    }

    private MatchForCommencement getMatchForCommencement() {
        return new MatchForCommencement(OASI, newRetirementFundUID, previousRetirementFundUID,
                commencementDate, terminationDate);
    }
}
