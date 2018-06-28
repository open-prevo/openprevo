package ch.prevo.open.node.api;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.nio.charset.Charset;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import ch.prevo.open.node.NodeApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NodeApplication.class)
@WebAppConfiguration
public class JobStartControllerTest {

    private MockMvc mockMvc;

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Inject
    private WebApplicationContext webApplicationContext;

    @Before
    public final void setUp(){
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

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