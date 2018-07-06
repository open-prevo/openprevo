package ch.prevo.open.hub.integration;

import ch.prevo.open.hub.HubService;
import ch.prevo.open.hub.nodes.NodeConfiguration;
import ch.prevo.open.hub.nodes.NodeRegistry;
import ch.prevo.open.hub.nodes.NodeService;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import javax.inject.Inject;
import java.nio.file.Paths;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HubIntegrationTest {

    private static final int NODE_PORT = 8080;

    @Inject
    private HubService hubService;

    @SpyBean
    private NodeService nodeService;

    @MockBean
    private NodeRegistry nodeRegistry;

    @ClassRule
    public static DockerComposeContainer environment =
            new DockerComposeContainer(Paths.get("../docker-compose-nodes.yml").toAbsolutePath().normalize().toFile())
                    .withExposedService("node_baloise", NODE_PORT, Wait.forHttp("/job-start").forStatusCode(200))
                    .withExposedService("node_helvetia", NODE_PORT, Wait.forHttp("/job-start").forStatusCode(200))
                    .withPull(false);

    @Before
    public void setup() {

        NodeConfiguration nodeBaloise = new NodeConfiguration(getBaseUrlForNode("node_baloise"),
                singletonList("CHE-109.740.084-Baloise-Sammelstiftung"));

        NodeConfiguration nodeHelvetia = new NodeConfiguration(getBaseUrlForNode("node_helvetia"),
                singletonList("CHE-109.537.488-Helvetia-Prisma-Sammelstiftung"));

        when(nodeRegistry.getCurrentNodes()).thenReturn(asList(nodeBaloise, nodeHelvetia));
    }

    private String getBaseUrlForNode(String nodeName) {
        String serviceHost = environment.getServiceHost(nodeName, NODE_PORT);
        Integer servicePort = environment.getServicePort(nodeName, NODE_PORT);
        return "http://" + serviceHost + ":" + servicePort;
    }

    // TODO: Fix test
//    @Test
//    public void testMatchingService() {
//        //given
//        Match expectedMatchFromHelvetiaToBaloise = new Match("756.1234.5678.97",
//                "CHE-109.537.488-Helvetia-Prisma-Sammelstiftung",
//                "CHE-109.740.084-Baloise-Sammelstiftung", entryDate, exitDate);
//        Match expectedMatchFromBaloiseToHelvetia = new Match("756.1335.5778.23",
//                "CHE-109.740.084-Baloise-Sammelstiftung",
//                "CHE-109.537.488-Helvetia-Prisma-Sammelstiftung", entryDate, exitDate);
//
//        // when
//        List<Match> matches = hubService.matchAndNotify();
//
//        // then
//        assertThat(matches).hasSize(2)
//                .containsExactlyInAnyOrder(expectedMatchFromHelvetiaToBaloise, expectedMatchFromBaloiseToHelvetia);
//        verify(nodeService).notifyMatches(matches);
//    }
}
