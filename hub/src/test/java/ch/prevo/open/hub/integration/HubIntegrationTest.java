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
package ch.prevo.open.hub.integration;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;

import ch.prevo.open.encrypted.services.Cryptography;
import ch.prevo.open.hub.HubService;
import ch.prevo.open.hub.match.Match;
import ch.prevo.open.hub.nodes.NodeConfiguration;
import ch.prevo.open.hub.nodes.NodeRegistry;
import ch.prevo.open.hub.nodes.NodeService;
import ch.prevo.open.hub.repository.NotificationDAO;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HubIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(HubIntegrationTest.class);

    private static final int NODE_PORT = 8080;

    @Inject
    private HubService hubService;

    @SpyBean
    private NodeService nodeService;

    @SpyBean
    private NotificationDAO notificationDAO;

    @MockBean
    private NodeRegistry nodeRegistry;

    private static final Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(LOGGER);

    @ClassRule
    public static DockerComposeContainer<?> environment =
            new DockerComposeContainer<>(Paths.get("../docker-compose-nodes.yml").toAbsolutePath().normalize().toFile())
                    .withExposedService("node_baloise", NODE_PORT, Wait.forHttp("/commencement-of-employment").forStatusCode(200))
                    .withExposedService("node_helvetia", NODE_PORT, Wait.forHttp("/commencement-of-employment").forStatusCode(200))
                    .withLogConsumer("node_helvetia", logConsumer)
                    .withLogConsumer("node_baloise", logConsumer)
                    .withPull(false);

    @Before
    public void setup() {

        NodeConfiguration nodeBaloise = new NodeConfiguration(getBaseUrlForNode("node_baloise"),
                "CHE-109.740.084", "CHE-223.471.073");

        NodeConfiguration nodeHelvetia = new NodeConfiguration(getBaseUrlForNode("node_helvetia"),
                "CHE-109.537.519", "CHE-109.633.927");

        when(nodeRegistry.getCurrentNodes()).thenReturn(asList(nodeBaloise, nodeHelvetia));
    }

    private String getBaseUrlForNode(String nodeName) {
        String serviceHost = environment.getServiceHost(nodeName, NODE_PORT);
        Integer servicePort = environment.getServicePort(nodeName, NODE_PORT);
        return "http://" + serviceHost + ":" + servicePort;
    }

    @Test
    public void testMatchingService() {
        //given
        Match expectedMatchFromHelvetia1ToBaloise1 = new Match(Cryptography.digestOasiNumber("756.1234.5678.97"),
                "CHE-109.537.519",
                "CHE-109.740.084", LocalDate.of(2018, 7, 1), LocalDate.of(2018, 6, 30));
        Match expectedMatchFromHelvetia1ToBaloise2 = new Match(Cryptography.digestOasiNumber("756.3324.5678.55"),
                "CHE-109.537.519",
                "CHE-223.471.073", LocalDate.of(2018, 7, 1), LocalDate.of(2018, 6, 30));
        Match expectedMatchFromHelvetia2ToBaloise1 = new Match(Cryptography.digestOasiNumber("756.5678.1234.11"),
                "CHE-109.633.927",
                "CHE-109.740.084", LocalDate.of(2018, 7, 1), LocalDate.of(2018, 6, 30));
        Match expectedMatchFromBaloise1ToHelvetia1 = new Match(Cryptography.digestOasiNumber("756.1335.5778.25"),
                "CHE-109.740.084",
                "CHE-109.537.519", LocalDate.of(2018, 7, 1), LocalDate.of(2018, 6, 30));
        Match expectedMatchFromBaloise1ToHelvetia2 = new Match(Cryptography.digestOasiNumber("756.9534.5271.91"),
                "CHE-109.740.084",
                "CHE-109.633.927", LocalDate.of(2018, 7, 1), LocalDate.of(2018, 6, 30));
        Match expectedMatchFromBaloise2ToHelvetia1 = new Match(Cryptography.digestOasiNumber("756.9874.5778.56"),
                "CHE-223.471.073",
                "CHE-109.537.519", LocalDate.of(2018, 7, 1), LocalDate.of(2018, 6, 30));

        // when
        List<Match> matches = hubService.matchAndNotify();

        // then
        assertThat(matches)
                .containsExactlyInAnyOrder(
                        expectedMatchFromHelvetia1ToBaloise1,
                        expectedMatchFromHelvetia1ToBaloise2,
                        expectedMatchFromHelvetia2ToBaloise1,
                        expectedMatchFromBaloise1ToHelvetia1,
                        expectedMatchFromBaloise1ToHelvetia2,
                        expectedMatchFromBaloise2ToHelvetia1
                );
        verify(nodeService).notifyMatches(matches);
        verify(notificationDAO, times(6)).saveMatchForCommencement(any());
        verify(notificationDAO, times(6)).saveMatchForTermination(any());
    }
}
