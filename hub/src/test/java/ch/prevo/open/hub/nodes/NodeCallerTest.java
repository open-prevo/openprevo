package ch.prevo.open.hub.nodes;

import ch.prevo.open.encrypted.model.CapitalTransferInformation;
import ch.prevo.open.encrypted.model.MatchForTermination;
import ch.prevo.open.encrypted.model.InsurantInformation;
import ch.prevo.open.encrypted.model.MatchForCommencement;
import ch.prevo.open.hub.repository.NotificationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import javax.inject.Inject;
import java.util.List;

import static java.time.LocalDate.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@RestClientTest({ NodeCaller.class, NotificationRepository.class })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class NodeCallerTest {

    private static final String OASI1 = "756.1234.5678.97";
    private static final String OASI2 = "756.1335.5778.23";
    private static final String UID1 = "CHE-223.471.073";
    private static final String UID2 = "CHE-109.723.097";
    private static final String UID3 = "CHE-109.537.488";

    private static final String RETIREMENT_FUND_NAME = "Baloise-Sammelstiftung";
    private static final String IBAN = "CH53 0077 0016 02222 3334 4";

    private static final String INSURANT_INFORMATION_JSON_ARRAY
            = "[{\"encryptedOasiNumber\" : \"" + OASI1 + "\", \"retirementFundUid\" : \"" + UID1
            + "\", \"date\" : \"2017-12-31\"}]";

    private static final String INSURANT_INFORMATION_JSON_ARRAY_WITH_MULTIPLE_FUNDS
            = "[" +
            "{\"encryptedOasiNumber\" : \"" + OASI1 + "\", \"retirementFundUid\" : \"" + UID1 + "\", \"date\" : \"2017-12-31\"}," +
            "{\"encryptedOasiNumber\" : \"" + OASI2 + "\", \"retirementFundUid\" : \"" + UID2 + "\", \"date\" : \"2017-12-31\"}" +
            "]";

    private static final String CAPITAL_TRANSFER_INFORMATION
            = "{\"name\" : \"" + RETIREMENT_FUND_NAME + "\", \"iban\" : \"" + IBAN + "\"}";

    private static final String URL1 = "https://host.domain1/path";
    private static final String URL2 = "https://host.domain2/path";
    private static final String URL3 = "https://host.domain3/path";

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
    public void getInsurantInformationListWithMultipleRetirementFunds() {
        // given
        server.expect(requestTo(URL1))
                .andRespond(withSuccess(INSURANT_INFORMATION_JSON_ARRAY_WITH_MULTIPLE_FUNDS, MediaType.APPLICATION_JSON));

        List<InsurantInformation> insurantInformationList = nodeCaller.getInsurantInformationList(URL1);

        assertEquals(2, insurantInformationList.size());
        assertEquals(UID1, insurantInformationList.get(0).getRetirementFundUid());
        assertEquals(OASI1, insurantInformationList.get(0).getEncryptedOasiNumber());
        assertEquals(of(2017, 12, 31), insurantInformationList.get(0).getDate());
        assertEquals(UID2, insurantInformationList.get(1).getRetirementFundUid());
        assertEquals(OASI2, insurantInformationList.get(1).getEncryptedOasiNumber());
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
                .andExpect(jsonPath("$.newRetirementFundUid", is(UID2)))
                .andRespond(withSuccess(CAPITAL_TRANSFER_INFORMATION, MediaType.APPLICATION_JSON));

        MatchForCommencement MatchForCommencement = createMatchForCommencement();

        // when
        CapitalTransferInformation capitalTransferInformation = nodeCaller
                .postCommencementNotification(URL1, MatchForCommencement);

        // then
        server.verify();
        assertThat(capitalTransferInformation.getName()).isEqualTo(RETIREMENT_FUND_NAME);
        assertThat(capitalTransferInformation.getIban()).isEqualTo(IBAN);
    }

    @Test
    public void notifyCommencementMatchOnlyOnce() {
        server.expect(requestTo(URL1))
                .andExpect(jsonPath("$.encryptedOasiNumber", is(OASI1)))
                .andExpect(jsonPath("$.newRetirementFundUid", is(UID2)))
                .andRespond(withSuccess(CAPITAL_TRANSFER_INFORMATION, MediaType.APPLICATION_JSON));

        MatchForCommencement MatchForCommencement = createMatchForCommencement();

        // when
        CapitalTransferInformation capitalTransferInformation = nodeCaller
                .postCommencementNotification(URL1, MatchForCommencement);
        CapitalTransferInformation secondCallTransferInfo = nodeCaller
                .postCommencementNotification(URL1, MatchForCommencement);

        // then
        server.verify();
        assertThat(capitalTransferInformation.getName()).isEqualTo(RETIREMENT_FUND_NAME);
        assertThat(capitalTransferInformation.getIban()).isEqualTo(IBAN);
        assertThat(secondCallTransferInfo).isNull();
    }

    @Test
    public void notifySingleCommencementMatchForSeveralTerminationMatches() {
        server.expect(requestTo(URL1))
                .andExpect(jsonPath("$.encryptedOasiNumber", is(OASI1)))
                .andExpect(jsonPath("$.newRetirementFundUid", is(UID2)))
                .andRespond(withSuccess(CAPITAL_TRANSFER_INFORMATION, MediaType.APPLICATION_JSON));
        server.expect(requestTo(URL1))
                .andExpect(jsonPath("$.encryptedOasiNumber", is(OASI1)))
                .andExpect(jsonPath("$.newRetirementFundUid", is(UID3)))
                .andRespond(withSuccess(CAPITAL_TRANSFER_INFORMATION, MediaType.APPLICATION_JSON));

        MatchForCommencement matchForCommencement_node2 = createMatchForCommencement();
        MatchForCommencement matchForCommencement_node3 = createMatchForCommencement();
        matchForCommencement_node3.setNewRetirementFundUid(UID3);

        // when
        CapitalTransferInformation capitalTransferInformation = nodeCaller
                .postCommencementNotification(URL1, matchForCommencement_node2);
        CapitalTransferInformation secondCallTransferInfo = nodeCaller
                .postCommencementNotification(URL1, matchForCommencement_node3);

        // then
        server.verify();
        assertThat(capitalTransferInformation.getName()).isEqualTo(RETIREMENT_FUND_NAME);
        assertThat(capitalTransferInformation.getIban()).isEqualTo(IBAN);
        assertThat(secondCallTransferInfo.getName()).isEqualTo(RETIREMENT_FUND_NAME);
        assertThat(secondCallTransferInfo.getIban()).isEqualTo(IBAN);
    }

    @Test
    public void verifyNotifyCommencementMatchIsSentInSecondApproachIfFirstWasNotSuccessful() {
        server.expect(requestTo(URL1)).andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));
        server.expect(requestTo(URL1))
                .andExpect(jsonPath("$.encryptedOasiNumber", is(OASI1)))
                .andExpect(jsonPath("$.newRetirementFundUid", is(UID2)))
                .andRespond(withSuccess(CAPITAL_TRANSFER_INFORMATION, MediaType.APPLICATION_JSON));

        MatchForCommencement MatchForCommencement = createMatchForCommencement();

        // when
        CapitalTransferInformation capitalTransferInformation = nodeCaller
                .postCommencementNotification(URL1, MatchForCommencement);
        CapitalTransferInformation secondCallTransferInfo = nodeCaller
                .postCommencementNotification(URL1, MatchForCommencement);

        // then
        server.verify();
        assertThat(capitalTransferInformation).isNull();
        assertThat(secondCallTransferInfo.getName()).isEqualTo(RETIREMENT_FUND_NAME);
        assertThat(secondCallTransferInfo.getIban()).isEqualTo(IBAN);
    }

    @Test
    public void notifyTerminationMatch() {
        server.expect(requestTo(URL2))
                .andExpect(jsonPath("$.transferInformation.iban", is(IBAN)))
                .andRespond(withSuccess());

        MatchForTermination MatchForTermination = createMatchForTermination();

        // when
        nodeCaller.postTerminationNotification(URL2, MatchForTermination);

        // then
        server.verify();
    }

    @Test
    public void notifyTerminationMatchOnlyOnce() {
        server.expect(requestTo(URL2))
                .andExpect(jsonPath("$.transferInformation.iban", is(IBAN)))
                .andRespond(withSuccess());

        MatchForTermination MatchForTermination = createMatchForTermination();

        // when
        nodeCaller.postTerminationNotification(URL2, MatchForTermination);
        nodeCaller.postTerminationNotification(URL2, MatchForTermination);

        // then
        server.verify();
    }

    @Test
    public void notifySeveralTerminationMatchesForSingleCommencement() {
        server.expect(requestTo(URL1))
                .andExpect(jsonPath("$.transferInformation.iban", is(IBAN)))
                .andRespond(withSuccess());
        server.expect(requestTo(URL3))
                .andExpect(jsonPath("$.transferInformation.iban", is(IBAN)))
                .andRespond(withSuccess());

        MatchForTermination matchForTermination_node1 = createMatchForTermination();
        matchForTermination_node1.setPreviousRetirementFundUid(UID1);
        MatchForTermination matchForTermination_node3 = createMatchForTermination();
        matchForTermination_node3.setPreviousRetirementFundUid(UID3);

        // when
        nodeCaller.postTerminationNotification(URL1, matchForTermination_node1);
        nodeCaller.postTerminationNotification(URL3, matchForTermination_node3);

        // then
        server.verify();
    }

    @Test
    public void verifyNotifyTerminationMatchIsSentInSecondApproachIfFirstWasNotSuccessful() {
        server.expect(requestTo(URL2)).andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));
        server.expect(requestTo(URL2))
                .andExpect(jsonPath("$.transferInformation.iban", is(IBAN)))
                .andRespond(withSuccess());

        MatchForTermination MatchForTermination = createMatchForTermination();

        // when
        nodeCaller.postTerminationNotification(URL2, MatchForTermination);
        nodeCaller.postTerminationNotification(URL2, MatchForTermination);

        // then
        server.verify();
    }

    @Test
    public void testCommencementNotificationForUnreachableNodes() {
        // given
        server.expect(requestTo(URL1)).andRespond(withStatus(HttpStatus.NOT_FOUND));

        // when
        nodeCaller.postCommencementNotification(URL1, createMatchForCommencement());

        // then
        server.verify();
    }

    @Test
    public void testTerminationNotificationForUnreachableNodes() {
        // given
        server.expect(requestTo(URL2)).andRespond(withStatus(HttpStatus.NOT_FOUND));

        // when
        nodeCaller.postTerminationNotification(URL2, createMatchForTermination());

        // then
        server.verify();
    }

    private MatchForTermination createMatchForTermination() {
        CapitalTransferInformation capitalTransferInformation = new CapitalTransferInformation(RETIREMENT_FUND_NAME,
                IBAN);
        MatchForTermination MatchForTermination = new MatchForTermination();
        MatchForTermination.setEncryptedOasiNumber(OASI1);
        MatchForTermination.setNewRetirementFundUid(UID2);
        MatchForTermination.setCommencementDate(of(2018, 7, 1));
        MatchForTermination.setTerminationDate(of(2018, 6, 30));
        MatchForTermination.setTransferInformation(capitalTransferInformation);
        return MatchForTermination;
    }

    private MatchForCommencement createMatchForCommencement() {
        MatchForCommencement MatchForCommencement = new MatchForCommencement();
        MatchForCommencement.setEncryptedOasiNumber(OASI1);
        MatchForCommencement.setCommencementDate(of(2018, 7, 1));
        MatchForCommencement.setTerminationDate(of(2018, 6, 30));
        MatchForCommencement.setPreviousRetirementFundUid(UID1);
        MatchForCommencement.setNewRetirementFundUid(UID2);
        return MatchForCommencement;
    }

}
