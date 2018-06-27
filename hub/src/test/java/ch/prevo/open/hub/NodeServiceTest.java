package ch.prevo.open.hub;

import ch.prevo.open.encrypted.model.InsurantInformation;
import ch.prevo.open.hub.match.Match;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import javax.inject.Inject;
import java.util.Set;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@RestClientTest(NodeService.class)
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

    @Test
    public void currentExits() throws Exception {
        server.expect(requestTo("/" + NodeService.JOB_END_ENDPOINT_NAME))
                .andRespond(withSuccess(INSURANT_INFORMATION_JSON_ARRAY, MediaType.APPLICATION_JSON));

        Set<InsurantInformation> insurantInformations = nodeService.currentExits();

        assertEquals(1, insurantInformations.size());
        assertEqualsToTestdata(insurantInformations);
    }

    @Test
    public void currentEntries() throws Exception {
        server.expect(requestTo("/" + NodeService.JOB_START_ENDPOINT_NAME))
                .andRespond(withSuccess(INSURANT_INFORMATION_JSON_ARRAY, MediaType.APPLICATION_JSON));

        Set<InsurantInformation> insurantInformations = nodeService.currentEntries();

        assertEquals(1, insurantInformations.size());
        assertEqualsToTestdata(insurantInformations);
    }

    @Test
    public void notifyMatch() throws Exception {
        server.expect(requestTo("/" + NodeService.MATCH_NOTIFY_ENDPOINT_NAME))
                .andRespond(withSuccess());

        nodeService.notifyMatches(singletonList(new Match(new InsurantInformation(AHV1, UID1), new InsurantInformation(AHV1, UID2))));
    }

    private void assertEqualsToTestdata(Set<InsurantInformation> insurantInformations) {
        InsurantInformation insurantInformation = insurantInformations.iterator().next();
        assertEquals(AHV1, insurantInformation.getEncryptedOasiNumber());
        assertEquals(UID1, insurantInformation.getRetirementFundUid());
    }

}