package ch.prevo.open.node.api;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.prevo.open.encrypted.model.TerminationMatchNotification;
import ch.prevo.open.node.NodeApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NodeApplication.class)
@WebAppConfiguration
public class MatchNotificationControllerTest extends RestBaseTest {

    @Test
    public void sendCommencementNotificationToPreviousRetirementFund() throws Exception {

        // given
        TerminationMatchNotification commencementMatchNotification = new TerminationMatchNotification();
        commencementMatchNotification.setEncryptedOasiNumber("756.1234.5678.97");
        commencementMatchNotification.setPreviousRetirementFundUid("CHE-109.740.084");

        // when
        mockMvc.perform(post("/commencement-match-notification")
                .content(this.convertToJson(commencementMatchNotification))
                .contentType(contentType))

        //then

                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.name", is("BKB_Test_Bank")))
                .andExpect(jsonPath("$.iban", is("CH53 0077 0016 02222 3334 4")));
    }
}