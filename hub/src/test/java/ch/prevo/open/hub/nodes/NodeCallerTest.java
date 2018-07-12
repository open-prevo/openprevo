package ch.prevo.open.hub.nodes;

import ch.prevo.open.encrypted.model.CapitalTransferInformation;
import ch.prevo.open.encrypted.model.CommencementMatchNotification;
import ch.prevo.open.encrypted.model.InsurantInformation;
import ch.prevo.open.encrypted.model.TerminationMatchNotification;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import javax.inject.Inject;
import java.util.List;

import static java.time.LocalDate.of;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@RestClientTest(NodeCaller.class)
public class NodeCallerTest {

    private static final String OASI1 = "756.1234.5678.97";
    private static final String UID1 = "CHE-223.471.073";
    private static final String UID2 = "CHE-109.723.097";

    private static final String RETIREMENT_FUND_NAME = "Baloise-Sammelstiftung";
    private static final String IBAN = "CH53 0077 0016 02222 3334 4";

    private static final String INSURANT_INFORMATION_JSON_ARRAY
            = "[{\"encryptedOasiNumber\" : \"" + OASI1 + "\", \"retirementFundUid\" : \"" + UID1 + "\", \"date\" : \"2017-12-31\"}]";

    private static final String CAPITAL_TRANSFER_INFORMATION
            = "{\"name\" : \"" + RETIREMENT_FUND_NAME + "\", \"iban\" : \"" + IBAN + "\"}";

    private static final String URL1 = "https://host.domain1/path";
    private static final String URL2 = "https://host.domain2/path";

    @Inject
    private MockRestServiceServer server;

    @Inject
    private NodeCaller nodeCaller;

    @Test
    public void getInsurantInformationList() throws Exception {
        server.expect(requestTo(URL1))
                .andRespond(withSuccess(INSURANT_INFORMATION_JSON_ARRAY, MediaType.APPLICATION_JSON));

        List<InsurantInformation> insurantInformationList = nodeCaller.getInsurantInformationList(URL1);

        assertEquals(1, insurantInformationList.size());
        assertEquals(UID1, insurantInformationList.get(0).getRetirementFundUid());
        assertEquals(OASI1, insurantInformationList.get(0).getEncryptedOasiNumber());
        assertEquals(of(2017, 12, 31), insurantInformationList.get(0).getDate());
        server.verify();
    }

    @Test
    public void tryGetInsurantInformationListWithUnreachableNode() {
        // given
        server.expect(requestTo(URL2)).andRespond(withStatus(HttpStatus.NOT_FOUND));

        // when
        List<InsurantInformation> insurantInformationList = nodeCaller.getInsurantInformationList(URL2);

        // then
        server.verify();
        assertTrue(insurantInformationList.isEmpty());
    }

    @Test
    public void notifyCommencementMatch() {
        server.expect(requestTo(URL1))
                .andExpect(jsonPath("$.encryptedOasiNumber", is(OASI1)))
                .andExpect(jsonPath("$.retirementFundUid", is(UID2)))
                .andRespond(withSuccess(CAPITAL_TRANSFER_INFORMATION, MediaType.APPLICATION_JSON));

        TerminationMatchNotification terminationMatchNotification = createTerminationMatchNotification();

        // when
        CapitalTransferInformation capitalTransferInformation = nodeCaller.postCommencementNotification(URL1, terminationMatchNotification);

        // then
        server.verify();
        assertEquals(RETIREMENT_FUND_NAME, capitalTransferInformation.getName());
        assertEquals(IBAN, capitalTransferInformation.getIban());
    }

    @Test
    public void notifyTerminationMatch() {
        server.expect(requestTo(URL2))
                .andExpect(jsonPath("$.transferInformation.iban", is(IBAN)))
                .andRespond(withSuccess());

        CommencementMatchNotification commencementMatchNotification = createCommencementMatchNotification();

        // when
        nodeCaller.postTerminationNotification(URL2, commencementMatchNotification);

        // then
        server.verify();
    }


    @Test
    public void testCommencementNotificationForUnreachableNodes() {
        // given
        server.expect(requestTo(URL1)).andRespond(withStatus(HttpStatus.NOT_FOUND));

        // when
        nodeCaller.postCommencementNotification(URL1, createTerminationMatchNotification());

        // then
        server.verify();
    }

    @Test
    public void testTerminationNotificationForUnreachableNodes() {
        // given
        server.expect(requestTo(URL2)).andRespond(withStatus(HttpStatus.NOT_FOUND));

        // when
        nodeCaller.postTerminationNotification(URL2, createCommencementMatchNotification());

        // then
        server.verify();
    }

    private CommencementMatchNotification createCommencementMatchNotification() {
        CapitalTransferInformation capitalTransferInformation = new CapitalTransferInformation(RETIREMENT_FUND_NAME, IBAN);
        CommencementMatchNotification commencementMatchNotification = new CommencementMatchNotification();
        commencementMatchNotification.setEncryptedOasiNumber(OASI1);
        commencementMatchNotification.setNewRetirementFundUid(UID2);
        commencementMatchNotification.setCommencementDate(of(2018, 7, 1));
        commencementMatchNotification.setTerminationDate(of(2018, 6, 30));
        commencementMatchNotification.setTransferInformation(capitalTransferInformation);
        return commencementMatchNotification;
    }

    private TerminationMatchNotification createTerminationMatchNotification() {
        TerminationMatchNotification terminationMatchNotification = new TerminationMatchNotification();
        terminationMatchNotification.setEncryptedOasiNumber(OASI1);
        terminationMatchNotification.setEntryDate(of(2018, 7, 1));
        terminationMatchNotification.setPreviousRetirementFundUid(UID1);
        terminationMatchNotification.setRetirementFundUid(UID2);
        return terminationMatchNotification;
    }

}
