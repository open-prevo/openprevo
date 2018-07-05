package ch.prevo.open.node.api;

import ch.prevo.open.node.NodeApplication;
import ch.prevo.open.node.data.provider.MockJobEventProviderImpl;
import ch.prevo.open.node.services.JobStartService;
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
@SpringBootTest(classes = { NodeApplication.class, JobStartControllerTest.Config.class })
@WebAppConfiguration
public class JobStartControllerTest extends RestBaseTest {

    @TestConfiguration
    static class Config {
        @Bean
        public JobStartService jobStartService() throws Exception {
            final ServiceListFactoryBean factory = Mockito.mock(ServiceListFactoryBean.class);
            given(factory.getObject()).willReturn(Collections.singletonList(new MockJobEventProviderImpl()));
            return new JobStartService(factory);
        }
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
