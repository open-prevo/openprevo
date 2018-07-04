package ch.prevo.open.hub.nodes;

import static ch.prevo.open.hub.nodes.NodeRegistry.NODE_1;
import static ch.prevo.open.hub.nodes.NodeRegistry.NODE_2;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.ExpectedCount.never;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.Set;
import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import ch.prevo.open.encrypted.model.InsurantInformation;
import ch.prevo.open.hub.match.Match;

@RunWith(SpringRunner.class)
@RestClientTest({ NodeService.class, NodeRegistry.class })
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

    @Test
    public void currentExits() {
        when(nodeRegistry.currentNodes()).thenReturn(singletonList(NODE_1));
        server.expect(requestTo(NODE_1.getJobExitsUrl()))
                .andRespond(withSuccess(INSURANT_INFORMATION_JSON_ARRAY, MediaType.APPLICATION_JSON));

        Set<InsurantInformation> insurantInformations = nodeService.getCurrentExits();

        server.verify();
        assertEquals(1, insurantInformations.size());
        assertEqualsToTestdata(insurantInformations);
    }

    @Test
    public void tryGetCurrentExitsWithUnreachableNode() {
        // given
        when(nodeRegistry.currentNodes()).thenReturn(asList(NODE_2, NODE_1));
        server.expect(requestTo(NODE_2.getJobExitsUrl())).andRespond(withStatus(HttpStatus.NOT_FOUND));
        server.expect(requestTo(NODE_1.getJobExitsUrl()))
                .andRespond(withSuccess(INSURANT_INFORMATION_JSON_ARRAY, MediaType.APPLICATION_JSON));

        // when
        Set<InsurantInformation> currentEntries = nodeService.getCurrentExits();

        // then
        server.verify();
        assertEquals(1, currentEntries.size());
        assertEqualsToTestdata(currentEntries);
    }

    @Test
    public void currentEntries() {
        when(nodeRegistry.currentNodes()).thenReturn(singletonList(NODE_2));
        server.expect(requestTo(NODE_2.getJobEntriesUrl()))
                .andRespond(withSuccess(INSURANT_INFORMATION_JSON_ARRAY, MediaType.APPLICATION_JSON));

        Set<InsurantInformation> insurantInformations = nodeService.getCurrentEntries();

        server.verify();
        assertEquals(1, insurantInformations.size());
        assertEqualsToTestdata(insurantInformations);
    }

    @Test
    public void notifyMatch() {
        when(nodeRegistry.currentNodes()).thenReturn(asList(NODE_1, NODE_2));
        String notificationResponse = "notification response";
        server.expect(requestTo(NODE_1.getMatchNotifyUrl()))
                .andRespond(withSuccess(notificationResponse, MediaType.TEXT_PLAIN));
        server.expect(requestTo(NODE_2.getMatchNotifyUrl()))
                .andExpect(jsonPath("$.encryptedOasiNumber", is(OASI1)))
                .andExpect(jsonPath("$.newRetirementFundUid", is(NODE_2.getRetirementFundUids()[0])))
                .andRespond(withSuccess(notificationResponse, MediaType.TEXT_PLAIN));

        nodeService.notifyMatches(singletonList(new Match(OASI1, UID1, UID2)));

        server.verify();
    }

    @Test
    public void testNotificationForNonExistingRetirementFund() {
        // given
        when(nodeRegistry.currentNodes()).thenReturn(asList(NODE_1, NODE_2));
        Match invalidMatch = new Match(OASI1, UID1, "RandomUID");
        server.expect(never(), requestTo(NODE_1.getMatchNotifyUrl()));
        server.expect(never(), requestTo(NODE_2.getMatchNotifyUrl()));

        // when
        nodeService.notifyMatches(singletonList(invalidMatch));

        // then
        server.verify();
    }

    @Test
    public void testNotificationForUnreachableNodes() {
        // given
        when(nodeRegistry.currentNodes()).thenReturn(asList(NODE_1, NODE_2));
        String notification_response = "notification response";
        server.expect(requestTo(NODE_1.getMatchNotifyUrl())).andRespond(withStatus(HttpStatus.NOT_FOUND));
        server.expect(requestTo(NODE_2.getMatchNotifyUrl()))
                .andExpect(jsonPath("$.encryptedOasiNumber", is(OASI1)))
                .andExpect(jsonPath("$.newRetirementFundUid", is(NODE_2.getRetirementFundUids()[0])))
                .andRespond(withSuccess(notification_response, MediaType.TEXT_PLAIN));

        // when
        nodeService.notifyMatches(singletonList(new Match(OASI1, UID1, UID2)));

        // then
        server.verify();
    }

    private void assertEqualsToTestdata(Set<InsurantInformation> insurantInformations) {
        InsurantInformation insurantInformation = insurantInformations.iterator().next();
        assertEquals(OASI1, insurantInformation.getEncryptedOasiNumber());
        assertEquals(UID1, insurantInformation.getRetirementFundUid());
    }

}