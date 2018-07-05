package ch.prevo.open.node.api;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.prevo.open.node.NodeApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NodeApplication.class)
@WebAppConfiguration
public class JobStartControllerTest extends RestBaseTest {

    @Test
    public void getAllJobStartData() throws Exception {
        mockMvc.perform(get("/job-start"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].encryptedOasiNumber", is("756.1234.5678.97")))
                .andExpect(jsonPath("$[0].retirementFundUid", is("CHE-109.740.084")));
    }
}
