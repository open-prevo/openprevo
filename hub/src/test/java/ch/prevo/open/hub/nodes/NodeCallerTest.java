package ch.prevo.open.hub.nodes;

import ch.prevo.open.encrypted.model.EncryptedData;
import ch.prevo.open.encrypted.model.InsurantInformation;
import ch.prevo.open.encrypted.model.MatchForCommencement;
import ch.prevo.open.encrypted.model.MatchForTermination;
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
@RestClientTest({NodeCaller.class, NotificationRepository.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class NodeCallerTest {

    private static final String OASI1 = "756.1234.5678.97";
    private static final String OASI2 = "756.1335.5778.23";
    private static final String UID1 = "CHE-223.471.073";
    private static final String UID2 = "CHE-109.723.097";

    private static final String ENCRYPTED_DATA = "This is the encrypted data";
    private static final String ENCRYPTED_KEY = "This is the encrypted key";

    private static final String INSURANT_INFORMATION_JSON_ARRAY
            = "[{\"encryptedOasiNumber\" : \"" + OASI1 + "\", \"retirementFundUid\" : \"" + UID1
            + "\", \"date\" : \"2017-12-31\"}]";

    private static final String INSURANT_INFORMATION_JSON_ARRAY_WITH_MULTIPLE_FUNDS
            = "[" +
            "{\"encryptedOasiNumber\" : \"" + OASI1 + "\", \"retirementFundUid\" : \"" + UID1 + "\", \"date\" : \"2017-12-31\"}," +
            "{\"encryptedOasiNumber\" : \"" + OASI2 + "\", \"retirementFundUid\" : \"" + UID2 + "\", \"date\" : \"2017-12-31\"}" +
            "]";

    private static final String CAPITAL_TRANSFER_INFORMATION
            = "{\"encryptedDataBase64\" : \"" + ENCRYPTED_DATA + "\", \"encryptedSymmetricKeyBase64\" : \"" + ENCRYPTED_KEY + "\"}";


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
                .andExpect(jsonPath("$.retirementFundUid", is(UID2)))
                .andRespond(withSuccess(CAPITAL_TRANSFER_INFORMATION, MediaType.APPLICATION_JSON));

        MatchForCommencement MatchForCommencement = createMatchForCommencement();

        // when
        EncryptedData capitalTransferInformation = nodeCaller
                .postCommencementNotification(URL1, MatchForCommencement);

        // then
        server.verify();
        assertThat(capitalTransferInformation.getEncryptedDataBase64()).isEqualTo(ENCRYPTED_DATA);
        assertThat(capitalTransferInformation.getEncryptedSymmetricKeyBase64()).isEqualTo(ENCRYPTED_KEY);
    }

    @Test
    public void notifyCommencementMatchOnlyOnce() {
        server.expect(requestTo(URL1))
                .andExpect(jsonPath("$.encryptedOasiNumber", is(OASI1)))
                .andExpect(jsonPath("$.retirementFundUid", is(UID2)))
                .andRespond(withSuccess(CAPITAL_TRANSFER_INFORMATION, MediaType.APPLICATION_JSON));

        MatchForCommencement MatchForCommencement = createMatchForCommencement();

        // when
        EncryptedData capitalTransferInformation = nodeCaller
                .postCommencementNotification(URL1, MatchForCommencement);
        EncryptedData secondCallTransferInfo = nodeCaller
                .postCommencementNotification(URL1, MatchForCommencement);

        // then
        server.verify();
        assertThat(capitalTransferInformation).isNotNull();
        assertThat(secondCallTransferInfo).isNull();
    }

    @Test
    public void verifyNotifyCommencementMatchIsSentInSecondApproachIfFirstWasNotSuccessful() {
        server.expect(requestTo(URL1)).andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));
        server.expect(requestTo(URL1))
                .andExpect(jsonPath("$.encryptedOasiNumber", is(OASI1)))
                .andExpect(jsonPath("$.retirementFundUid", is(UID2)))
                .andRespond(withSuccess(CAPITAL_TRANSFER_INFORMATION, MediaType.APPLICATION_JSON));

        MatchForCommencement MatchForCommencement = createMatchForCommencement();

        // when
        EncryptedData capitalTransferInformation = nodeCaller
                .postCommencementNotification(URL1, MatchForCommencement);
        EncryptedData secondCallTransferInfo = nodeCaller
                .postCommencementNotification(URL1, MatchForCommencement);

        // then
        server.verify();
        assertThat(capitalTransferInformation).isNull();
        assertThat(secondCallTransferInfo).isNotNull();
    }

    @Test
    public void notifyTerminationMatch() {
        server.expect(requestTo(URL2))
                .andExpect(jsonPath("$.transferInformation.encryptedSymmetricKeyBase64", is(ENCRYPTED_KEY)))
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
                .andExpect(jsonPath("$.transferInformation.encryptedSymmetricKeyBase64", is(ENCRYPTED_KEY)))
                .andRespond(withSuccess());

        MatchForTermination MatchForTermination = createMatchForTermination();

        // when
        nodeCaller.postTerminationNotification(URL2, MatchForTermination);
        nodeCaller.postTerminationNotification(URL2, MatchForTermination);

        // then
        server.verify();
    }

    @Test
    public void verifyNotifyTerminationMatchIsSentInSecondApproachIfFirstWasNotSuccessful() {
        server.expect(requestTo(URL2)).andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));
        server.expect(requestTo(URL2))
                .andExpect(jsonPath("$.transferInformation.encryptedSymmetricKeyBase64", is(ENCRYPTED_KEY)))
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
        EncryptedData transferInformation = new EncryptedData();
        transferInformation.setEncryptedDataBase64(ENCRYPTED_DATA);
        transferInformation.setEncryptedSymmetricKeyBase64(ENCRYPTED_KEY);
        MatchForTermination MatchForTermination = new MatchForTermination();
        MatchForTermination.setEncryptedOasiNumber(OASI1);
        MatchForTermination.setNewRetirementFundUid(UID2);
        MatchForTermination.setCommencementDate(of(2018, 7, 1));
        MatchForTermination.setTerminationDate(of(2018, 6, 30));
        MatchForTermination.setTransferInformation(transferInformation);
        return MatchForTermination;
    }

    private MatchForCommencement createMatchForCommencement() {
        MatchForCommencement MatchForCommencement = new MatchForCommencement();
        MatchForCommencement.setEncryptedOasiNumber(OASI1);
        MatchForCommencement.setCommencementDate(of(2018, 7, 1));
        MatchForCommencement.setTerminationDate(of(2018, 6, 30));
        MatchForCommencement.setPreviousRetirementFundUid(UID1);
        MatchForCommencement.setRetirementFundUid(UID2);
        return MatchForCommencement;
    }

}
