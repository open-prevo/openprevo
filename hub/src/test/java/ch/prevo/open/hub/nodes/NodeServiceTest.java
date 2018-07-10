package ch.prevo.open.hub.nodes;

import ch.prevo.open.encrypted.model.CapitalTransferInformation;
import ch.prevo.open.encrypted.model.CommencementMatchNotification;
import ch.prevo.open.encrypted.model.InsurantInformation;
import ch.prevo.open.hub.match.Match;
import ch.prevo.open.hub.match.MatcherService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@RestClientTest({NodeService.class, NodeRegistry.class, MatcherService.class})
public class NodeServiceTest {

    private static final String OASI1 = "756.1234.5678.97";
    private static final String UID1 = "CHE-223.471.073";
    private static final String UID2 = "CHE-109.723.097";

    @Inject
    private NodeService nodeService;

    @MockBean
    private NodeCaller nodeCaller;

    @MockBean
    private NodeRegistry nodeRegistry;

    private NodeConfiguration node1;
    private NodeConfiguration node2;
    private InsurantInformation insurantInformation;

    @Before
    public void setUp() {
        node1 = new NodeConfiguration("https://host1", singletonList(UID1));
        node2 = new NodeConfiguration("https://host2", singletonList(UID2));
        insurantInformation = new InsurantInformation(OASI1, UID1, LocalDate.of(2020, 12, 15));
    }

    @Test
    public void currentEmploymentTerminations() {
        when(nodeRegistry.getCurrentNodes()).thenReturn(singletonList(node1));
        when(nodeCaller.getInsurantInformationList(node1.getJobExitsUrl()))
                .thenReturn(singletonList(insurantInformation));

        Set<InsurantInformation> insurantInformations = nodeService.getCurrentExits();

        assertEquals(1, insurantInformations.size());
        assertEquals(insurantInformation, insurantInformations.iterator().next());
    }

    @Test
    public void testFilterOfInvalidInsurantInformation() {
        // given
        InsurantInformation invalidInsurant = new InsurantInformation(OASI1, "RandomUID");
        when(nodeRegistry.getCurrentNodes()).thenReturn(asList(node2, node1));
        when(nodeCaller.getInsurantInformationList(node2.getJobExitsUrl()))
                .thenReturn(singletonList(invalidInsurant));
        when(nodeCaller.getInsurantInformationList(node1.getJobExitsUrl()))
                .thenReturn(singletonList(insurantInformation));

        // when
        Set<InsurantInformation> currentEntries = nodeService.getCurrentExits();

        // then
        assertEquals(1, currentEntries.size());
        assertEqualsToTestdata(currentEntries);

    }

    @Test
    public void tryGetCurrentCurrentEmploymentTerminationsWithUnreachableNode() {
        // given
        when(nodeRegistry.getCurrentNodes()).thenReturn(asList(node2, node1));
        when(nodeCaller.getInsurantInformationList(node2.getJobExitsUrl()))
                .thenReturn(emptyList());
        when(nodeCaller.getInsurantInformationList(node1.getJobExitsUrl()))
                .thenReturn(singletonList(insurantInformation));

        // when
        Set<InsurantInformation> currentEntries = nodeService.getCurrentExits();

        // then
        assertEquals(1, currentEntries.size());
        assertEqualsToTestdata(currentEntries);
    }

    @Test
    public void currentEmploymentCommencements() {
        when(nodeRegistry.getCurrentNodes()).thenReturn(singletonList(node1));
        when(nodeCaller.getInsurantInformationList(node1.getJobEntriesUrl()))
                .thenReturn(singletonList(insurantInformation));

        Set<InsurantInformation> insurantInformations = nodeService.getCurrentEntries();

        assertEquals(1, insurantInformations.size());
        assertEquals(insurantInformation, insurantInformations.iterator().next());
    }

    @Test
    public void notifyMatch() {
        when(nodeRegistry.getCurrentNodes()).thenReturn(asList(node1, node2));

        CapitalTransferInformation transferInformation = new CapitalTransferInformation();
        when(nodeCaller.postCommencementNotification(eq(node2.getCommencementMatchNotifyUrl()), any()))
                .thenReturn(transferInformation);

        // when
        LocalDate entryDate = LocalDate.of(2018, 7, 1);
        LocalDate exitDate = LocalDate.of(2018, 6, 30);
        nodeService.notifyMatches(
                singletonList(new Match(OASI1, UID1, UID2, entryDate, exitDate)));

        // then
        CommencementMatchNotification matchNotification = new CommencementMatchNotification(OASI1, UID1, UID2, entryDate, exitDate, transferInformation);
        verify(nodeCaller).postTerminationNotification(eq(node1.getTerminationMatchNotifyUrl()), eq(matchNotification));
    }

    @Test
    public void testNotificationForUnreachableNodes() {
        // given
        when(nodeRegistry.getCurrentNodes()).thenReturn(asList(node1, node2));
        when(nodeCaller.postCommencementNotification(eq(node2.getCommencementMatchNotifyUrl()), any())).thenReturn(null);

        // when
        nodeService.notifyMatches(singletonList(new Match(OASI1, UID1, UID2, LocalDate.of(2018, 7, 1), LocalDate.of(2018, 6, 30))));

        // then
        // TODO: fix and activate; we should not notify without the Transfer info
        // verify(nodeCaller, times(0)).postTerminationNotification(any(), any());
    }

    private void assertEqualsToTestdata(Set<InsurantInformation> insurantInformations) {
        InsurantInformation insurantInformation = insurantInformations.iterator().next();
        assertEquals(OASI1, insurantInformation.getEncryptedOasiNumber());
        assertEquals(UID1, insurantInformation.getRetirementFundUid());
    }
}
