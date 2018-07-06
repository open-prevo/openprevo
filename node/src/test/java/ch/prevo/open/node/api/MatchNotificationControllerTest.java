// TODO: Fix test
//package ch.prevo.open.node.api;
//
//import ch.prevo.open.encrypted.model.CommencementMatchNotification;
//import ch.prevo.open.node.NodeApplication;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.test.web.servlet.MvcResult;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = NodeApplication.class)
//@WebAppConfiguration
//public class MatchNotificationControllerTest extends RestBaseTest {
//
//    @Test
//    public void getAllJobEndData() throws Exception {
//
//        MvcResult result = mockMvc.perform(post("/match-notification")
//                .content(this.convertToJson(new CommencementMatchNotification("756.1234.5678.97", "CHE-109.740.084")))
//                .contentType(contentType))
//                .andExpect(status().isCreated()).andReturn();
//
//        String encryptedCapitalTransferInformation = result.getResponse().getContentAsString();
//
//        assertThat(encryptedCapitalTransferInformation).contains("BKB");
//    }
//}