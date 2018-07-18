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
import java.util.Set;

import static java.time.LocalDate.of;
import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {NodeService.class, MatcherService.class})
public class NodeServiceTest {

    private static final String OASI1 = "756.1234.5678.97";
    private static final String UID1_OLD = "CHE-223.471.073";
    private static final String UID2_NEW = "CHE-109.723.097";

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
    private InsurantInformation terminationInsurantInfo;
    private InsurantInformation commencementInsurantInfo;

    @Before
    public void setUp() {
        node1_new = new NodeConfiguration("https://host1", UID1_OLD);
        node2_old = new NodeConfiguration("https://host2", UID2_NEW);
        terminationInsurantInfo = new InsurantInformation(OASI1, UID1_OLD, of(2020, 12, 15));
        commencementInsurantInfo = new InsurantInformation(OASI1, UID2_NEW, of(2021, 2, 1));
    }

    @Test
    public void currentEmploymentTerminations() {
        when(nodeRegistry.getCurrentNodes()).thenReturn(singletonList(node1_new));
        when(nodeCaller.getInsurantInformationList(node1_new.getEmploymentTerminationsUrl()))
                .thenReturn(singletonList(terminationInsurantInfo));

        Set<InsurantInformation> insurantInformations = nodeService.getCurrentExits();

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
        Set<InsurantInformation> currentEntries = nodeService.getCurrentExits();

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
        Set<InsurantInformation> currentEntries = nodeService.getCurrentExits();

        // then
        assertEquals(1, currentEntries.size());
        assertEqualsToTestdata(currentEntries);
    }

    @Test
    public void currentEmploymentCommencements() {
        when(nodeRegistry.getCurrentNodes()).thenReturn(singletonList(node2_old));
        when(nodeCaller.getInsurantInformationList(node2_old.getEmploymentCommencementsUrl()))
                .thenReturn(singletonList(commencementInsurantInfo));

        Set<InsurantInformation> insurantInformations = nodeService.getCurrentEntries();

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
                singletonList(new Match(OASI1, UID1_OLD, UID2_NEW, entryDate, exitDate)));

        // then
        MatchForTermination matchNotification = new MatchForTermination(OASI1, UID1_OLD, UID2_NEW, entryDate, exitDate, transferInformation);
        verify(nodeCaller).postTerminationNotification(eq(node1_new.getTerminationMatchNotifyUrl()), eq(matchNotification));
    }


    @Test
    public void testIgnoringAlreadyMatchedEmploymentTerminations() {
        when(nodeRegistry.getCurrentNodes()).thenReturn(asList(node1_new, node2_old));
        matcherService.findMatches(singleton(terminationInsurantInfo), singleton(commencementInsurantInfo));

        when(nodeCaller.getInsurantInformationList(node2_old.getEmploymentTerminationsUrl()))
                .thenReturn(singletonList(terminationInsurantInfo));

        Set<InsurantInformation> insurantInformations = nodeService.getCurrentExits();

        assertTrue(insurantInformations.isEmpty());
    }

    @Test
    public void testIgnoringAlreadyMatchedEmploymentCommencements() {
        when(nodeRegistry.getCurrentNodes()).thenReturn(asList(node1_new, node2_old));
        matcherService.findMatches(singleton(terminationInsurantInfo), singleton(commencementInsurantInfo));

        when(nodeCaller.getInsurantInformationList(node1_new.getEmploymentCommencementsUrl()))
                .thenReturn(singletonList(commencementInsurantInfo));

        Set<InsurantInformation> insurantInformations = nodeService.getCurrentEntries();

        assertTrue(insurantInformations.isEmpty());
    }

    @Test
    public void testNotificationForUnreachableNodes() {
        // given
        when(nodeRegistry.getCurrentNodes()).thenReturn(asList(node1_new, node2_old));
        when(nodeCaller.postCommencementNotification(eq(node1_new.getCommencementMatchNotifyUrl()), any())).thenReturn(null);

        // when
        nodeService.notifyMatches(singletonList(new Match(OASI1, UID1_OLD, UID2_NEW, commencementInsurantInfo.getDate(), terminationInsurantInfo.getDate())));

        // then
        // TODO: fix and activate; we should not notify without the Transfer info
        // verify(nodeCaller, times(0)).postTerminationNotification(any(), any());
    }

    private void assertEqualsToTestdata(Set<InsurantInformation> insurantInformations) {
        InsurantInformation insurantInformation = insurantInformations.iterator().next();
        assertEquals(OASI1, insurantInformation.getEncryptedOasiNumber());
        assertEquals(UID1_OLD, insurantInformation.getRetirementFundUid());
    }
}
