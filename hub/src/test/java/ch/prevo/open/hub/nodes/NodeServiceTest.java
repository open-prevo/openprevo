package ch.prevo.open.hub.nodes;

import ch.prevo.open.encrypted.model.InsurantInformation;
import ch.prevo.open.hub.match.Match;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import javax.inject.Inject;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@RestClientTest({NodeService.class, NodeRegistry.class})
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
    public void currentExits() throws Exception {
        final NodeConfiguration node = new NodeConfiguration();
        node.setJobExitsUrl("https://host1/job-exits");

        when(nodeRegistry.currentNodes()).thenReturn(singletonList(node));
        server.expect(requestTo(node.getJobExitsUrl()))
                .andRespond(withSuccess(INSURANT_INFORMATION_JSON_ARRAY, MediaType.APPLICATION_JSON));

        Set<InsurantInformation> insurantInformations = nodeService.getCurrentExits();

        server.verify();
        assertEquals(1, insurantInformations.size());
        assertEqualsToTestdata(insurantInformations);
    }

    @Test
    public void currentEntries() throws Exception {
        final NodeConfiguration node = new NodeConfiguration();
        node.setJobEntriesUrl("https://host2/job-entries");

        when(nodeRegistry.currentNodes()).thenReturn(singletonList(node));
        server.expect(requestTo(node.getJobEntriesUrl()))
                .andRespond(withSuccess(INSURANT_INFORMATION_JSON_ARRAY, MediaType.APPLICATION_JSON));

        Set<InsurantInformation> insurantInformations = nodeService.getCurrentEntries();

        server.verify();
        assertEquals(1, insurantInformations.size());
        assertEqualsToTestdata(insurantInformations);
    }

    @Test
    public void notifyMatch() throws Exception {
        final NodeConfiguration node1 = new NodeConfiguration();
        node1.setMatchNotifyUrl("https://host1/match-notify");
        node1.setRetirementFundUids(UID1);
        final NodeConfiguration node2 = new NodeConfiguration();
        node2.setMatchNotifyUrl("https://host2/match-notify");
        node2.setRetirementFundUids(UID2);

        when(nodeRegistry.currentNodes()).thenReturn(asList(node1, node2));
        String notification_response = "notification response";
        server.expect(requestTo(node1.getMatchNotifyUrl())).andRespond(withSuccess(notification_response, MediaType.TEXT_PLAIN));
        server.expect(requestTo(node2.getMatchNotifyUrl()))
                .andExpect(jsonPath("$.encryptedOasiNumber", is(OASI1)))
                .andExpect(jsonPath("$.newRetirementFundUid", is(node2.getRetirementFundUids()[0])))
                .andRespond(withSuccess(notification_response, MediaType.TEXT_PLAIN));


        nodeService.notifyMatches(singletonList(new Match(OASI1, UID1, UID2)));

        server.verify();
    }

    private void assertEqualsToTestdata(Set<InsurantInformation> insurantInformations) {
        InsurantInformation insurantInformation = insurantInformations.iterator().next();
        assertEquals(OASI1, insurantInformation.getEncryptedOasiNumber());
        assertEquals(UID1, insurantInformation.getRetirementFundUid());
    }

}