package ch.prevo.open.hub;

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

import static ch.prevo.open.hub.NodeRegistry.NODE_1;
import static ch.prevo.open.hub.NodeRegistry.NODE_2;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@RestClientTest({NodeService.class, NodeRegistry.class})
public class NodeServiceTest {

    private static final String AHV1 = "756.1234.5678.97";
    private static final String UID1 = "CHE-223.471.073";
    private static final String UID2 = "CHE-109.723.097";

    private static final String INSURANT_INFORMATION_JSON_ARRAY
            = "[{\"encryptedOasiNumber\" : \"" + AHV1 + "\", \"retirementFundUid\" : \"" + UID1 + "\"}]";

    @Inject
    private NodeService nodeService;
    @Inject
    private MockRestServiceServer server;
    @MockBean
    private NodeRegistry nodeRegistry;

    @Test
    public void currentExits() throws Exception {
        when(nodeRegistry.currentNodes()).thenReturn(singletonList(NODE_1));
        server.expect(requestTo(NODE_1.getJobExitsUrl()))
                .andRespond(withSuccess(INSURANT_INFORMATION_JSON_ARRAY, MediaType.APPLICATION_JSON));

        Set<InsurantInformation> insurantInformations = nodeService.currentExits();

        server.verify();
        assertEquals(1, insurantInformations.size());
        assertEqualsToTestdata(insurantInformations);
    }

    @Test
    public void currentEntries() throws Exception {
        when(nodeRegistry.currentNodes()).thenReturn(singletonList(NODE_2));
        server.expect(requestTo(NODE_2.getJobEntriesUrl()))
                .andRespond(withSuccess(INSURANT_INFORMATION_JSON_ARRAY, MediaType.APPLICATION_JSON));

        Set<InsurantInformation> insurantInformations = nodeService.currentEntries();

        server.verify();
        assertEquals(1, insurantInformations.size());
        assertEqualsToTestdata(insurantInformations);
    }

    @Test
    public void notifyMatch() throws Exception {
        when(nodeRegistry.currentNodes()).thenReturn(asList(NODE_1, NODE_2));
        server.expect(requestTo(NODE_1.getMatchNotifyUrl())).andRespond(withSuccess());
        server.expect(requestTo(NODE_2.getMatchNotifyUrl())).andRespond(withSuccess());

        nodeService.notifyMatches(singletonList(new Match(new InsurantInformation(AHV1, UID1), new InsurantInformation(AHV1, UID2))));

        server.verify();
    }

    private void assertEqualsToTestdata(Set<InsurantInformation> insurantInformations) {
        InsurantInformation insurantInformation = insurantInformations.iterator().next();
        assertEquals(AHV1, insurantInformation.getEncryptedOasiNumber());
        assertEquals(UID1, insurantInformation.getRetirementFundUid());
    }

}