package ch.prevo.open.hub.nodes;

import ch.prevo.open.encrypted.model.InsurantInformation;
import ch.prevo.open.hub.match.MatcherService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import javax.inject.Inject;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@RestClientTest({ NodeService.class, NodeRegistry.class, MatcherService.class })
public class NodeServiceTest {

    private static final String OASI1 = "756.1234.5678.97";
    private static final String UID1 = "CHE-223.471.073";
    private static final String UID2 = "CHE-109.723.097";

    private static final String INSURANT_INFORMATION_JSON_ARRAY
            = "[{\"encryptedOasiNumber\" : \"" + OASI1 + "\", \"retirementFundUid\" : \"" + UID1 + "\"}]";

    @Inject
    private NodeService nodeService;

    @Inject
    private MockRestServiceServer server;

    @MockBean
    private NodeRegistry nodeRegistry;

    private NodeConfiguration node1;
    private NodeConfiguration node2;

    @Before
    public void setUp() {
        node1 = new NodeConfiguration("https://host1", singletonList(UID1));
        node2 = new NodeConfiguration("https://host2", singletonList(UID2));
    }

    @Test
    public void currentEmploymentTerminations() {

        when(nodeRegistry.getCurrentNodes()).thenReturn(singletonList(node1));
        server.expect(requestTo(node1.getJobExitsUrl()))
                .andRespond(withSuccess(INSURANT_INFORMATION_JSON_ARRAY, MediaType.APPLICATION_JSON));

        Set<InsurantInformation> insurantInformations = nodeService.getCurrentExits();

        server.verify();
        assertEquals(1, insurantInformations.size());
        assertEqualsToTestdata(insurantInformations);
    }

    @Test
    public void testFilterOfInvalidInsurantInformation() {

        // given
        String invalidInsurant = "[{\"encryptedOasiNumber\" : \"OASI\", \"retirementFundUid\" : \"RandomUID\"}]";
        when(nodeRegistry.getCurrentNodes()).thenReturn(asList(node2, node1));
        server.expect(requestTo(node2.getJobExitsUrl()))
                .andRespond(withSuccess(invalidInsurant, MediaType.APPLICATION_JSON));
        server.expect(requestTo(node1.getJobExitsUrl()))
                .andRespond(withSuccess(INSURANT_INFORMATION_JSON_ARRAY, MediaType.APPLICATION_JSON));

        // when
        Set<InsurantInformation> currentEntries = nodeService.getCurrentExits();

        // then
        server.verify();
        assertEquals(1, currentEntries.size());
        assertEqualsToTestdata(currentEntries);

    }

    @Test
    public void tryGetCurrentCurrentEmploymentTerminationsWithUnreachableNode() {
        // given
        when(nodeRegistry.getCurrentNodes()).thenReturn(asList(node2, node1));
        server.expect(requestTo(node2.getJobExitsUrl())).andRespond(withStatus(HttpStatus.NOT_FOUND));
        server.expect(requestTo(node1.getJobExitsUrl()))
                .andRespond(withSuccess(INSURANT_INFORMATION_JSON_ARRAY, MediaType.APPLICATION_JSON));

        // when
        Set<InsurantInformation> currentEntries = nodeService.getCurrentExits();

        // then
        server.verify();
        assertEquals(1, currentEntries.size());
        assertEqualsToTestdata(currentEntries);
    }

    @Test
    public void currentEmploymentCommencements() {
        when(nodeRegistry.getCurrentNodes()).thenReturn(singletonList(node1));
        server.expect(requestTo(node1.getJobEntriesUrl()))
                .andRespond(withSuccess(INSURANT_INFORMATION_JSON_ARRAY, MediaType.APPLICATION_JSON));

        Set<InsurantInformation> insurantInformations = nodeService.getCurrentEntries();

        server.verify();
        assertEquals(1, insurantInformations.size());
        assertEqualsToTestdata(insurantInformations);
    }

//    @Test
//    public void notifyMatch() {
//        when(nodeRegistry.getCurrentNodes()).thenReturn(asList(node1, node2));
//        String notification_response = "notification response";
//        CapitalTransferInformation transferInformation = new CapitalTransferInformation("", "iban");
//        server.expect(requestTo(node1.getTerminationMatchNotifyUrl()))
//                .andRespond(withSuccess(transferInformation, MediaType.APPLICATION_JSON));
//        server.expect(requestTo(node2.getCommencementMatchNotifyUrl()))
//                .andExpect(jsonPath("$.encryptedOasiNumber", is(OASI1)))
//                .andExpect(jsonPath("$.newRetirementFundUid", is(node2.getRetirementFundUids().get(0))))
//                .andRespond(withSuccess(notification_response, MediaType.TEXT_PLAIN));
//
//        // when
//        nodeService.notifyMatches(singletonList(new Match(OASI1, UID1, UID2, entryDate, exitDate)));
//
//        // then
//        server.verify();
//    }
//
//    @Test
//    public void testNotificationForUnreachableNodes() {
//        // given
//        when(nodeRegistry.getCurrentNodes()).thenReturn(asList(node1, node2));
//        String notification_response = "notification response";
//        server.expect(requestTo(node1.getMatchNotifyUrl())).andRespond(withStatus(HttpStatus.NOT_FOUND));
//        server.expect(requestTo(node2.getMatchNotifyUrl()))
//                .andExpect(jsonPath("$.encryptedOasiNumber", is(OASI1)))
//                .andExpect(jsonPath("$.newRetirementFundUid", is(node2.getRetirementFundUids().get(0))))
//                .andRespond(withSuccess(notification_response, MediaType.TEXT_PLAIN));
//
//        // when
//        nodeService.notifyMatches(singletonList(new Match(OASI1, UID1, UID2, entryDate, exitDate)));
//
//        // then
//        server.verify();
//    }

    private void assertEqualsToTestdata(Set<InsurantInformation> insurantInformations) {
        InsurantInformation insurantInformation = insurantInformations.iterator().next();
        assertEquals(OASI1, insurantInformation.getEncryptedOasiNumber());
        assertEquals(UID1, insurantInformation.getRetirementFundUid());
    }
}
