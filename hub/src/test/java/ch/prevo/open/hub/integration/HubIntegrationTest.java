package ch.prevo.open.hub.integration;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.mockito.Mockito.when;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import javax.inject.Inject;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;

import ch.prevo.open.hub.HubService;
import ch.prevo.open.hub.nodes.NodeConfiguration;
import ch.prevo.open.hub.nodes.NodeRegistry;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HubIntegrationTest {

    private static final int NODE_PORT = 8080;

    @Inject
    private HubService hubService;

    @MockBean
    private NodeRegistry nodeRegistry;

    @ClassRule
    public static DockerComposeContainer environment =
            new DockerComposeContainer(Paths.get("../docker/docker-compose.yml").toAbsolutePath().normalize().toFile())
                    .withExposedService("node_baloise", NODE_PORT, Wait.forHttp("/job-start").forStatusCode(200))
                    .withExposedService("node_helvetia", NODE_PORT, Wait.forHttp("/job-start").forStatusCode(200))
                    .withPull(false);

    @Before
    public void setup() {

        NodeConfiguration nodeBaloise = new NodeConfiguration(getBaseUrlForNode("node_baloise"), singletonList(""));
        NodeConfiguration nodeHelvetia = new NodeConfiguration(getBaseUrlForNode("node_helvetia"), singletonList(""));

        when(nodeRegistry.getCurrentNodes()).thenReturn(asList(nodeBaloise, nodeHelvetia));
    }

    private String getBaseUrlForNode(String nodeName) {
        String serviceHost = environment.getServiceHost(nodeName, NODE_PORT);
        Integer servicePort = environment.getServicePort(nodeName, NODE_PORT);
        return serviceHost + ":" + servicePort;
    }

    @Test
    public void testMatchingService() {
        hubService.matchAndNotify();
    }
}
