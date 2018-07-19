package ch.prevo.open.node.api;

import ch.prevo.open.encrypted.model.CapitalTransferInformation;
import ch.prevo.open.encrypted.model.EncryptedData;
import ch.prevo.open.encrypted.model.MatchForCommencement;
import ch.prevo.open.encrypted.services.CapitalTransferInfoEncrypter;
import ch.prevo.open.encrypted.services.Cryptography;
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
import java.time.LocalDate;
import java.util.Collections;

import static ch.prevo.open.encrypted.services.DataEncrypter.ASYMMETRIC_TRANSFORMATION_ALGORITHM;
import static org.junit.Assert.assertEquals;
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

    private static final String OWN_UID = "CHE-109.740.084";
    private static final String OTHER_UID = "CHE-109.537.488";

    @Inject
    private NodeConfigurationService nodeConfigurationService;

    @TestConfiguration
    static class Config {
        @Inject
        private NodeConfigurationService nodeConfigurationService;

        @Bean
        public MatchNotificationService matchNotificationService() throws Exception {
            final ServiceListFactoryBean factory = Mockito.mock(ServiceListFactoryBean.class);
            given(factory.getObject()).willReturn(Collections.singletonList(new MockProviderFactory()));
            return new MatchNotificationService(factory, nodeConfigurationService);
        }

        @Bean
        public NodeConfigurationService nodeConfigurationService() throws Exception {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ASYMMETRIC_TRANSFORMATION_ALGORITHM);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            NodeConfigurationService nodeConfigService = mock(NodeConfigurationService.class);
            when(nodeConfigService.getPublicKey(OTHER_UID)).thenReturn(keyPair.getPublic());
            when(nodeConfigService.getPrivateKey(OWN_UID)).thenReturn(keyPair.getPrivate());
            return nodeConfigService;
        }
    }

    @Test
    public void sendCommencementNotificationToPreviousRetirementFund() throws Exception {
        // given
        MatchForCommencement commencementMatchNotification = new MatchForCommencement();
        commencementMatchNotification.setEncryptedOasiNumber(Cryptography.digestOasiNumber("756.1234.5678.97"));
        commencementMatchNotification.setRetirementFundUid(OWN_UID);
        commencementMatchNotification.setPreviousRetirementFundUid(OTHER_UID);
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

        assertEquals(MockProvider.CAPITAL_TRANSFER_INFO_1, decrypt(mvcResult.getResponse().getContentAsString()));
    }

    private CapitalTransferInformation decrypt(String encryptedDataJson) throws IOException {
        EncryptedData encryptedData = new ObjectMapper().readValue(encryptedDataJson, EncryptedData.class);
        PrivateKey privateKey = nodeConfigurationService.getPrivateKey(OWN_UID);
        return new CapitalTransferInfoEncrypter().decrypt(encryptedData, privateKey);
    }
}
