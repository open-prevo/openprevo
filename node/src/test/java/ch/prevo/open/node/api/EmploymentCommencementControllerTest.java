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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.serviceloader.ServiceListFactoryBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.prevo.open.encrypted.services.Cryptography;
import ch.prevo.open.node.NodeApplication;
import ch.prevo.open.node.data.provider.MockProviderFactory;
import ch.prevo.open.node.services.AdapterDataValidationService;
import ch.prevo.open.node.services.EmploymentCommencementService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { NodeApplication.class, EmploymentCommencementControllerTest.Config.class })
@WebAppConfiguration
public class EmploymentCommencementControllerTest extends RestBaseTest {

    @TestConfiguration
    static class Config {

        @Bean
        public EmploymentCommencementService employmentCommencementService(AdapterDataValidationService adapterDataValidationService) throws Exception {
            final ServiceListFactoryBean factory = Mockito.mock(ServiceListFactoryBean.class);
            given(factory.getObject()).willReturn(Collections.singletonList(new MockProviderFactory()));
            return new EmploymentCommencementService(factory, adapterDataValidationService);
        }
    }

    @Test
    public void getAllEmploymentCommencementData() throws Exception {
        mockMvc.perform(get("/commencement-of-employment"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].encryptedOasiNumber", is(Cryptography.digestOasiNumber("756.1234.5678.97"))))
                .andExpect(jsonPath("$[0].retirementFundUid", is("CHE-109.740.084")))
                .andExpect(jsonPath("$[1].encryptedOasiNumber", is(Cryptography.digestOasiNumber("756.5678.1234.11"))))
                .andExpect(jsonPath("$[1].retirementFundUid", is("CHE-109.740.078")))
                .andExpect(jsonPath("$[2].encryptedOasiNumber", is(Cryptography.digestOasiNumber("756.1298.6578.93"))))
                .andExpect(jsonPath("$[2].retirementFundUid", is("CHE-109.537.488")));
    }
}
