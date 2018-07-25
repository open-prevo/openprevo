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
package ch.prevo.open.hub.match;

import ch.prevo.open.encrypted.model.InsurantInformation;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.time.LocalDate.of;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MatcherServiceTest {

    private static final String OASI_1 = "oasi1";
    private static final String UID_1 = "uid1";
    private static final String OASI_2 = "oasi2";
    private static final String UID_2 = "uid2";
    private static final String UID_3 = "uid3";

    private MatcherService matcherService;

    @Before
    public void setup() {
        matcherService = new MatcherService();
    }

    @Test
    public void findMatches() {
        Set<InsurantInformation> terminations = createSet(new InsurantInformation(OASI_1, UID_1));
        Set<InsurantInformation> commencements = createSet(new InsurantInformation(OASI_1, UID_2), new InsurantInformation(OASI_2, UID_3));

        List<Match> matches = matcherService.findMatches(terminations, commencements);

        assertEquals(1, matches.size());
        Match match = matches.get(0);
        assertEquals(OASI_1, match.getEncryptedOasiNumber());
        assertEquals(UID_1, match.getPreviousRetirementFundUid());
        assertEquals(UID_2, match.getNewRetirementFundUid());
    }

    @Test
    public void findMatchesWithinSameRetirementFund() {
        Set<InsurantInformation> terminations = createSet(new InsurantInformation(OASI_1, UID_1, of(2018, 6, 1)));
        Set<InsurantInformation> commencements = createSet(new InsurantInformation(OASI_1, UID_1, of(2018, 8, 1)));

        List<Match> matches = matcherService.findMatches(terminations, commencements);

        assertEquals(1, matches.size());
    }

    @Test
    public void findMatchesWithinSameRetirementFund_entryBeforeExit() {
        Set<InsurantInformation> terminations = createSet(new InsurantInformation(OASI_1, UID_1, of(2018, 6, 1)));
        Set<InsurantInformation> commencements = createSet(new InsurantInformation(OASI_1, UID_1, of(2018, 1, 1)));

        List<Match> matches = matcherService.findMatches(terminations, commencements);

        assertEquals(0, matches.size());
    }

    @Test
    public void findMatchesEmptyInput() {
        assertTrue(matcherService.findMatches(emptySet(), emptySet()).isEmpty());
    }

    @Test
    public void findMatchesWithDuplicates() {
        Set<InsurantInformation> terminations = createSet(new InsurantInformation(OASI_1, UID_1));
        Set<InsurantInformation> commencements = createSet(new InsurantInformation(OASI_1, UID_2), new InsurantInformation(OASI_1, UID_3));

        List<Match> matches = matcherService.findMatches(terminations, commencements);

        assertThat(matches).isEmpty();
    }

    @Test
    public void findSeveralTerminationsForSingleCommencement() {
        // given
        final LocalDate terminationDate = LocalDate.of(2018, 6, 30);
        final Set<InsurantInformation> terminations = createSet(
                new InsurantInformation(OASI_1, UID_1, terminationDate),
                new InsurantInformation(OASI_1, UID_2, terminationDate)
        );
        final LocalDate commencementDate = LocalDate.of(2018, 7, 1);
        final Set<InsurantInformation> commencements = createSet(
                new InsurantInformation(OASI_1, UID_3, commencementDate)
        );

        // when
        final List<Match> matches = matcherService.findMatches(terminations, commencements);

        // then
        assertThat(matches).containsExactlyInAnyOrder(
                new Match(OASI_1, UID_1, UID_3, commencementDate, terminationDate),
                new Match(OASI_1, UID_2, UID_3, commencementDate, terminationDate)
        );
    }

    private Set<InsurantInformation> createSet(InsurantInformation... insurantInformations) {
        Set<InsurantInformation> result = new HashSet<>();
        Collections.addAll(result, insurantInformations);
        return result;
    }
}
