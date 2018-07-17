package ch.prevo.open.node.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NodeConfigurationTest {

    @Inject
    private NodeConfiguration nodeConfiguration;

    @Test
    public void readConfig() {
        assertNotNull(nodeConfiguration.getPrivateKey("CHE-109.537.519"));
        assertNotNull(nodeConfiguration.getPublicKey("CHE-109.740.084"));
    }

    @Test(expected = IllegalStateException.class)
    public void attemptReadConfigForUnknownUID() {
        assertNull(nodeConfiguration.getPublicKey("12345"));
    }

}
