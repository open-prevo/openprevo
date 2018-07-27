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
package ch.prevo.open.node.api;

import ch.prevo.open.data.api.CapitalTransferInformation;
import ch.prevo.open.encrypted.model.EncryptedData;
import ch.prevo.open.encrypted.model.MatchForCommencement;
import ch.prevo.open.encrypted.model.MatchForTermination;
import ch.prevo.open.encrypted.services.Cryptography;
import ch.prevo.open.node.crypto.DataEncryptionService;
import ch.prevo.open.node.NodeApplication;
import ch.prevo.open.node.config.NodeConfigurationService;
import ch.prevo.open.node.data.provider.MockProvider;
import ch.prevo.open.node.data.provider.MockProviderFactory;
import ch.prevo.open.node.services.MatchNotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.serviceloader.ServiceListFactoryBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;

import javax.inject.Inject;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDate;
import java.util.Collections;

import static ch.prevo.open.node.crypto.DataEncryptionService.ASYMMETRIC_TRANSFORMATION_ALGORITHM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {NodeApplication.class, MatchNotificationControllerTest.Config.class})
@WebAppConfiguration
public class MatchNotificationControllerTest extends RestBaseTest {

    private static final String NEW_UID = "CHE-109.740.084";
    private static final String PREVIOUS_UID = "CHE-109.537.488";

    @Inject
    private NodeConfigurationService nodeConfigurationService;

    private DataEncryptionService dataEncryptionService = new DataEncryptionService();

    @TestConfiguration
    static class Config {

        @Bean
        public MatchNotificationService matchNotificationService(NodeConfigurationService nodeConfigurationService) throws Exception {
            final ServiceListFactoryBean factory = Mockito.mock(ServiceListFactoryBean.class);
            given(factory.getObject()).willReturn(Collections.singletonList(new MockProviderFactory()));
            return new MatchNotificationService(factory, nodeConfigurationService);
        }

        @Bean
        public NodeConfigurationService nodeConfigurationService() throws Exception {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ASYMMETRIC_TRANSFORMATION_ALGORITHM);
            KeyPair previousFundKeyPair = keyPairGenerator.generateKeyPair();
            KeyPair newFundKeyPair = keyPairGenerator.generateKeyPair();

            NodeConfigurationService nodeConfigService = mock(NodeConfigurationService.class);
            when(nodeConfigService.getPublicKey(PREVIOUS_UID)).thenReturn(previousFundKeyPair.getPublic());
            when(nodeConfigService.getPublicKey(NEW_UID)).thenReturn(newFundKeyPair.getPublic());
            when(nodeConfigService.getPrivateKey(PREVIOUS_UID)).thenReturn(previousFundKeyPair.getPrivate());
            when(nodeConfigService.getPrivateKey(NEW_UID)).thenReturn(newFundKeyPair.getPrivate());
            return nodeConfigService;
        }
    }

    @Test
    public void sendCommencementNotificationToPreviousRetirementFund() throws Exception {
        // given
        MatchForCommencement commencementMatchNotification = new MatchForCommencement();
        commencementMatchNotification.setEncryptedOasiNumber(Cryptography.digestOasiNumber("756.1234.5678.97"));
        commencementMatchNotification.setNewRetirementFundUid(NEW_UID);
        commencementMatchNotification.setPreviousRetirementFundUid(PREVIOUS_UID);
        commencementMatchNotification.setCommencementDate(LocalDate.of(2018, 7, 1));
        commencementMatchNotification.setTerminationDate(LocalDate.of(2018, 6, 30));

        // when
        MvcResult mvcResult = mockMvc.perform(post("/commencement-match-notification")
                .content(this.convertToJson(commencementMatchNotification))
                .contentType(contentType))

                //then

                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andReturn();

        CapitalTransferInformation decryptedAndVerifiedResult = decryptAndVerify(mvcResult.getResponse().getContentAsString());
        assertThat(decryptedAndVerifiedResult).isEqualTo(MockProvider.CAPITAL_TRANSFER_INFO_1);
    }

    @Test
    public void sendTerminationNotificationToNewRetirementFund() throws Exception {
        // given
        MatchForTermination matchForTermination = new MatchForTermination();
        matchForTermination.setEncryptedOasiNumber(Cryptography.digestOasiNumber("756.1234.5678.97"));
        matchForTermination.setNewRetirementFundUid(PREVIOUS_UID);
        matchForTermination.setPreviousRetirementFundUid(NEW_UID);
        matchForTermination.setCommencementDate(LocalDate.of(2018, 7, 1));
        matchForTermination.setTerminationDate(LocalDate.of(2018, 6, 30));
        matchForTermination.setTransferInformation(encryptAndSign(MockProvider.CAPITAL_TRANSFER_INFO_1));
        // when

        mockMvc.perform(post("/termination-match-notification")
                .content(this.convertToJson(matchForTermination))
                .contentType(contentType))
        // then
                .andExpect(status().isOk());
    }


    private CapitalTransferInformation decryptAndVerify(String encryptedDataJson) throws IOException {
        EncryptedData encryptedData = new ObjectMapper().readValue(encryptedDataJson, EncryptedData.class);
        PrivateKey privateKey = nodeConfigurationService.getPrivateKey(PREVIOUS_UID);
        PublicKey publicKey = nodeConfigurationService.getPublicKey(NEW_UID);
        return dataEncryptionService.decryptAndVerify(encryptedData, CapitalTransferInformation.class, privateKey, publicKey);
    }

    private EncryptedData encryptAndSign(CapitalTransferInformation capitalTransferInformation) {
        PrivateKey privateKey = nodeConfigurationService.getPrivateKey(NEW_UID);
        PublicKey publicKey = nodeConfigurationService.getPublicKey(PREVIOUS_UID);
        return dataEncryptionService.encryptAndSign(capitalTransferInformation, publicKey, privateKey);
    }
}
