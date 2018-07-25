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
package ch.prevo.open.hub.nodes;

import ch.prevo.open.encrypted.model.EncryptedData;
import ch.prevo.open.encrypted.model.InsurantInformation;
import ch.prevo.open.encrypted.model.MatchForTermination;
import ch.prevo.open.hub.match.Match;
import ch.prevo.open.hub.match.MatcherService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;

import static java.time.LocalDate.of;
import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {NodeService.class, MatcherService.class})
public class NodeServiceTest {

    private static final String OASI1 = "756.1234.5678.97";
    private static final String UID1 = "CHE-223.471.073";
    private static final String UID2 = "CHE-109.723.097";
    private static final String UID3 = "CHE-109.537.488";

    @Inject
    private NodeService nodeService;
    @Inject
    private MatcherService matcherService;

    @MockBean
    private NodeCaller nodeCaller;
    @MockBean
    private NodeRegistry nodeRegistry;

    private NodeConfiguration node1_new;
    private NodeConfiguration node2_old;
    private NodeConfiguration node3_old;
    private InsurantInformation terminationInsurantInfo;
    private InsurantInformation commencementInsurantInfo;

    @Before
    public void setUp() {
        node1_new = new NodeConfiguration("https://host1", UID1);
        node2_old = new NodeConfiguration("https://host2", UID2);
        node3_old = new NodeConfiguration("https://host3", UID3);
        terminationInsurantInfo = new InsurantInformation(OASI1, UID1, of(2020, 12, 15));
        commencementInsurantInfo = new InsurantInformation(OASI1, UID2, of(2021, 2, 1));
    }

    @Test
    public void currentEmploymentTerminations() {
        when(nodeRegistry.getCurrentNodes()).thenReturn(singletonList(node1_new));
        when(nodeCaller.getInsurantInformationList(node1_new.getEmploymentTerminationsUrl()))
                .thenReturn(singletonList(terminationInsurantInfo));

        Set<InsurantInformation> insurantInformations = nodeService.getCurrentTerminations();

        assertEquals(1, insurantInformations.size());
        assertEquals(terminationInsurantInfo, insurantInformations.iterator().next());
    }

    @Test
    public void testFilterOfInvalidInsurantInformation() {
        // given
        InsurantInformation invalidInsurant = new InsurantInformation(OASI1, "RandomUID");
        when(nodeRegistry.getCurrentNodes()).thenReturn(asList(node2_old, node1_new));
        when(nodeCaller.getInsurantInformationList(node2_old.getEmploymentTerminationsUrl()))
                .thenReturn(singletonList(invalidInsurant));
        when(nodeCaller.getInsurantInformationList(node1_new.getEmploymentTerminationsUrl()))
                .thenReturn(singletonList(terminationInsurantInfo));

        // when
        Set<InsurantInformation> currentEntries = nodeService.getCurrentTerminations();

        // then
        assertEquals(1, currentEntries.size());
        assertEqualsToTestdata(currentEntries);

    }

    @Test
    public void tryGetCurrentCurrentEmploymentTerminationsWithUnreachableNode() {
        // given
        when(nodeRegistry.getCurrentNodes()).thenReturn(asList(node2_old, node1_new));
        when(nodeCaller.getInsurantInformationList(node2_old.getEmploymentTerminationsUrl()))
                .thenReturn(emptyList());
        when(nodeCaller.getInsurantInformationList(node1_new.getEmploymentTerminationsUrl()))
                .thenReturn(singletonList(terminationInsurantInfo));

        // when
        Set<InsurantInformation> currentEntries = nodeService.getCurrentTerminations();

        // then
        assertEquals(1, currentEntries.size());
        assertEqualsToTestdata(currentEntries);
    }

    @Test
    public void currentEmploymentCommencements() {
        when(nodeRegistry.getCurrentNodes()).thenReturn(singletonList(node2_old));
        when(nodeCaller.getInsurantInformationList(node2_old.getEmploymentCommencementsUrl()))
                .thenReturn(singletonList(commencementInsurantInfo));

        Set<InsurantInformation> insurantInformations = nodeService.getCurrentCommencements();

        assertEquals(1, insurantInformations.size());
        assertEquals(commencementInsurantInfo, insurantInformations.iterator().next());
    }

    @Test
    public void notifyMatch() {
        when(nodeRegistry.getCurrentNodes()).thenReturn(asList(node1_new, node2_old));

        EncryptedData transferInformation = new EncryptedData("", "");
        when(nodeCaller.postCommencementNotification(eq(node2_old.getCommencementMatchNotifyUrl()), any()))
                .thenReturn(transferInformation);

        // when
        LocalDate entryDate = of(2018, 7, 1);
        LocalDate exitDate = of(2018, 6, 30);
        nodeService.notifyMatches(
                singletonList(new Match(OASI1, UID1, UID2, entryDate, exitDate)));

        // then
        MatchForTermination matchNotification = new MatchForTermination(OASI1, UID1, UID2, entryDate, exitDate, transferInformation);
        verify(nodeCaller).postTerminationNotification(eq(node1_new.getTerminationMatchNotifyUrl()), eq(matchNotification));
    }


    @Test
    public void testIgnoringAlreadyMatchedEmploymentTerminations() {
        when(nodeRegistry.getCurrentNodes()).thenReturn(asList(node1_new, node2_old));
        matcherService.findMatches(singleton(terminationInsurantInfo), singleton(commencementInsurantInfo));

        when(nodeCaller.getInsurantInformationList(node2_old.getEmploymentTerminationsUrl()))
                .thenReturn(singletonList(terminationInsurantInfo));

        Set<InsurantInformation> insurantInformations = nodeService.getCurrentTerminations();

        assertTrue(insurantInformations.isEmpty());
    }

    @Test
    public void testIgnoringAlreadyMatchedEmploymentCommencements() {
        when(nodeRegistry.getCurrentNodes()).thenReturn(asList(node1_new, node2_old));
        matcherService.findMatches(singleton(terminationInsurantInfo), singleton(commencementInsurantInfo));

        when(nodeCaller.getInsurantInformationList(node1_new.getEmploymentCommencementsUrl()))
                .thenReturn(singletonList(commencementInsurantInfo));

        Set<InsurantInformation> insurantInformations = nodeService.getCurrentCommencements();

        assertTrue(insurantInformations.isEmpty());
    }

    @Test
    public void testNotificationForUnreachableNodes() {
        // given
        when(nodeRegistry.getCurrentNodes()).thenReturn(asList(node1_new, node2_old));
        when(nodeCaller.postCommencementNotification(eq(node1_new.getCommencementMatchNotifyUrl()), any())).thenReturn(null);

        // when
        nodeService.notifyMatches(singletonList(new Match(OASI1, UID1, UID2, commencementInsurantInfo.getDate(), terminationInsurantInfo.getDate())));

        // then
        verify(nodeCaller, never()).postTerminationNotification(any(), any());
    }

    @Test
    public void testNotificationForSeveralTerminationsMatchingSingleCommencement() {
        when(nodeRegistry.getCurrentNodes()).thenReturn(asList(node1_new, node2_old, node3_old));

        EncryptedData transferInformation = new EncryptedData();
        when(nodeCaller.postCommencementNotification(eq(node1_new.getCommencementMatchNotifyUrl()), any()))
                .thenReturn(transferInformation);

        LocalDate commencementDate = of(2018, 7, 1);
        LocalDate terminationDate = of(2018, 6, 30);

        // when
        nodeService.notifyMatches(Arrays.asList(
                new Match(OASI1, UID2, UID1, commencementDate, terminationDate),
                new Match(OASI1, UID3, UID1, commencementDate, terminationDate)
        ));

        // then
        MatchForTermination matchNotification_node2 = new MatchForTermination(OASI1, UID2, UID1, commencementDate, terminationDate, transferInformation);
        MatchForTermination matchNotification_node3 = new MatchForTermination(OASI1, UID3, UID1, commencementDate, terminationDate, transferInformation);
        verify(nodeCaller).postTerminationNotification(eq(node2_old.getTerminationMatchNotifyUrl()), eq(matchNotification_node2));
        verify(nodeCaller).postTerminationNotification(eq(node3_old.getTerminationMatchNotifyUrl()), eq(matchNotification_node3));
    }

    private void assertEqualsToTestdata(Set<InsurantInformation> insurantInformations) {
        InsurantInformation insurantInformation = insurantInformations.iterator().next();
        assertEquals(OASI1, insurantInformation.getEncryptedOasiNumber());
        assertEquals(UID1, insurantInformation.getRetirementFundUid());
    }
}
