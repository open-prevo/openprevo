package ch.prevo.open.node.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {NodeConfigurationService.class })
public class NodeConfigurationServiceTest {

    @Inject
    private NodeConfigurationService nodeConfigService;

    @Test
    public void readConfig() {
        assertNotNull(nodeConfigService.getPrivateKey("CHE-109.537.519"));
        assertNotNull(nodeConfigService.getPublicKey("CHE-109.740.084"));
    }

    @Test
    public void attemptReadConfigForUnknownUID() {
        assertNull(nodeConfigService.getPublicKey("12345"));
    }

}