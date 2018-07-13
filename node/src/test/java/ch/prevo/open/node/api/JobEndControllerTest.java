package ch.prevo.open.node.api;

import ch.prevo.open.node.NodeApplication;
import ch.prevo.open.node.data.provider.MockProviderFactory;
import ch.prevo.open.encrypted.services.Cryptography;
import ch.prevo.open.node.services.JobEndService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.serviceloader.ServiceListFactoryBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { NodeApplication.class, JobEndControllerTest.Config.class })
@WebAppConfiguration
public class JobEndControllerTest extends RestBaseTest {

    @TestConfiguration
    static class Config {
        @Bean
        public JobEndService jobEndService() throws Exception {
            final ServiceListFactoryBean factory = Mockito.mock(ServiceListFactoryBean.class);
            given(factory.getObject()).willReturn(Collections.singletonList(new MockProviderFactory()));
            return new JobEndService(factory);
        }
    }

    @Test
    public void getAllJobEndData() throws Exception {
        mockMvc.perform(get("/job-end"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].encryptedOasiNumber", is(Cryptography.hash("756.9534.5271.94"))))
                .andExpect(jsonPath("$[0].retirementFundUid", is("CHE-109.740.078")));
    }
}