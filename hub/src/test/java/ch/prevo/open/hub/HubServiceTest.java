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
package ch.prevo.open.hub;

import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import ch.prevo.open.encrypted.model.InsurantInformation;
import ch.prevo.open.hub.match.Match;
import ch.prevo.open.hub.nodes.NodeService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HubServiceTest {

    @MockBean
    private NodeService nodeService;

    @Inject
    private HubService hubService;

    @SuppressWarnings("unchecked")
    @Test
    public void matchAndNotify() {
        InsurantInformation entry = new InsurantInformation("123", "1");
        InsurantInformation exit = new InsurantInformation("123", "2");
        when(nodeService.getCurrentCommencements()).thenReturn(singleton(entry));
        when(nodeService.getCurrentTerminations()).thenReturn(singleton(exit));
        ArgumentCaptor<List<Match>> nodeServiceArgumentCaptor = ArgumentCaptor.forClass(List.class);

        hubService.matchAndNotify();

        verify(nodeService).notifyMatches(nodeServiceArgumentCaptor.capture());
        List<Match> notifiedMatches = nodeServiceArgumentCaptor.getValue();
        assertEquals(1, notifiedMatches.size());
        assertEquals(exit.getRetirementFundUid(), notifiedMatches.get(0).getPreviousRetirementFundUid());
        assertEquals(entry.getRetirementFundUid(), notifiedMatches.get(0).getNewRetirementFundUid());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void matchAndNotifySeveralTerminationsForSingleCommencement() {
        // given
        final LocalDate terminationDate = LocalDate.of(2018, 6, 30);
        final Set<InsurantInformation> terminations = new HashSet<>(Arrays.asList(
                new InsurantInformation("OASI_1", "UID_1", terminationDate),
                new InsurantInformation("OASI_1", "UID_2", terminationDate)
        ));
        final LocalDate commencementDate = LocalDate.of(2018, 7, 1);
        final Set<InsurantInformation> commencements = Collections.singleton(
                new InsurantInformation("OASI_1", "UID_3", commencementDate)
        );
        when(nodeService.getCurrentCommencements()).thenReturn(commencements);
        when(nodeService.getCurrentTerminations()).thenReturn(terminations);

        // when
        hubService.matchAndNotify();

        // then
        final ArgumentCaptor<List<Match>> nodeServiceArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(nodeService).notifyMatches(nodeServiceArgumentCaptor.capture());
        final List<Match> notifiedMatches = nodeServiceArgumentCaptor.getValue();
        assertThat(notifiedMatches).containsExactlyInAnyOrder(
                new Match("OASI_1", "UID_1", "UID_3", commencementDate, terminationDate),
                new Match("OASI_1", "UID_2", "UID_3", commencementDate, terminationDate)
        );
    }
}
