/*============================================================================*
 * Copyright (c) 2018 - Prevo-System AG and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * 
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License, v. 2.0 are satisfied: GNU General Public License, version 3
 * with the GNU Classpath Exception which is
 * available at https://www.gnu.org/software/classpath/license.html.
 * 
 * SPDX-License-Identifier: EPL-2.0 OR GPL-3.0 WITH Classpath-exception-2.0
 * 
 * Contributors:
 *     Prevo-System AG - initial API and implementation
 *===========================================================================*/
package ch.prevo.open.hub.nodes;

import static java.time.LocalDate.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import ch.prevo.open.encrypted.model.EncryptedData;
import ch.prevo.open.encrypted.model.InsurantInformation;
import ch.prevo.open.encrypted.model.MatchForCommencement;
import ch.prevo.open.encrypted.model.MatchForTermination;
import ch.prevo.open.hub.repository.NotificationDAO;

@RunWith(SpringRunner.class)
@RestClientTest({ NodeCaller.class })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class NodeCallerTest {

    private static final String OASI1 = "756.1234.5678.97";
    private static final String OASI2 = "756.1335.5778.23";
    private static final String UID1 = "CHE-223.471.073";
    private static final String UID2 = "CHE-109.723.097";
    private static final String UID3 = "CHE-109.537.488";

    private static final String ENCRYPTED_DATA = "This is the encrypted data";
    private static final String ENCRYPTED_KEY = "This is the encrypted key";
    private static final String IV = "IV";

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
    private static final String URL3 = "https://host.domain3/path";

    @Inject
    private MockRestServiceServer server;

    @Inject
    private NodeCaller nodeCaller;

    @MockBean
    private NotificationDAO notificationDAO;

    @Test
    public void getInsurantInformationList() {
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
        EncryptedData capitalTransferInformation = nodeCaller
                .postCommencementNotification(URL1, MatchForCommencement);

        // then
        server.verify();
        assertThat(capitalTransferInformation.getEncryptedDataBase64()).isEqualTo(ENCRYPTED_DATA);
        assertThat(capitalTransferInformation.getEncryptedSymmetricKeyBase64()).isEqualTo(ENCRYPTED_KEY);
    }

    @Test
    public void notifyCommencementMatchOnlyOnce() {
        // given
        final MatchForCommencement matchForCommencement = createMatchForCommencement();
        server.expect(requestTo(URL1))
                .andExpect(jsonPath("$.encryptedOasiNumber", is(OASI1)))
                .andExpect(jsonPath("$.newRetirementFundUid", is(UID2)))
                .andRespond(withSuccess(CAPITAL_TRANSFER_INFORMATION, MediaType.APPLICATION_JSON));
        when(notificationDAO.isMatchForCommencementAlreadyNotified(matchForCommencement)).thenReturn(false).thenReturn(true);

        // when
        final EncryptedData capitalTransferInformation = nodeCaller
                .postCommencementNotification(URL1, matchForCommencement);
        final EncryptedData secondCallTransferInfo = nodeCaller
                .postCommencementNotification(URL1, matchForCommencement);

        // then
        server.verify();
        assertThat(capitalTransferInformation).isNotNull();
        assertThat(secondCallTransferInfo).isNull();
        verify(notificationDAO).saveMatchForCommencement(matchForCommencement);
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
        EncryptedData capitalTransferInformation = nodeCaller
                .postCommencementNotification(URL1, matchForCommencement_node2);
        EncryptedData secondCallTransferInfo = nodeCaller
                .postCommencementNotification(URL1, matchForCommencement_node3);

        // then
        server.verify();
        assertThat(capitalTransferInformation.getEncryptedDataBase64()).isNotBlank();
        assertThat(secondCallTransferInfo.getEncryptedDataBase64()).isNotBlank();
        assertThat(secondCallTransferInfo).isEqualTo(capitalTransferInformation);
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
        // given
        final MatchForTermination matchForTermination = createMatchForTermination();
        server.expect(requestTo(URL2))
                .andExpect(jsonPath("$.transferInformation.encryptedSymmetricKeyBase64", is(ENCRYPTED_KEY)))
                .andRespond(withSuccess());
        when(notificationDAO.isMatchForTerminationAlreadyNotified(matchForTermination)).thenReturn(false).thenReturn(true);

        // when
        nodeCaller.postTerminationNotification(URL2, matchForTermination);
        nodeCaller.postTerminationNotification(URL2, matchForTermination);

        // then
        server.verify();
        verify(notificationDAO).saveMatchForTermination(matchForTermination);
    }

    @Test
    public void notifySeveralTerminationMatchesForSingleCommencement() {
        EncryptedData transferInformation2 = new EncryptedData("Data2", "Key2", "Iv2)");
        server.expect(requestTo(URL1))
                .andExpect(jsonPath("$.transferInformation.encryptedSymmetricKeyBase64", is(ENCRYPTED_KEY)))
                .andRespond(withSuccess());
        server.expect(requestTo(URL3))
                .andExpect(jsonPath("$.transferInformation.encryptedSymmetricKeyBase64", is(transferInformation2.getEncryptedSymmetricKeyBase64())))
                .andRespond(withSuccess());

        MatchForTermination matchForTermination_node1 = createMatchForTermination();
        matchForTermination_node1.setPreviousRetirementFundUid(UID1);
        MatchForTermination matchForTermination_node3 = createMatchForTermination();
        matchForTermination_node3.setTransferInformation(transferInformation2);
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
        EncryptedData transferInformation = new EncryptedData(ENCRYPTED_DATA, ENCRYPTED_KEY, IV);
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
        MatchForCommencement.setNewRetirementFundUid(UID2);
        return MatchForCommencement;
    }

}
