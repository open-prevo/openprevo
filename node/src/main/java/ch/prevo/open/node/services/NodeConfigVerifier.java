package ch.prevo.open.node.services;

import ch.prevo.open.node.config.NodeConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class NodeConfigVerifier implements ApplicationListener<ApplicationReadyEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(NodeConfigVerifier.class);

    @Inject
    private NodeConfigurationService nodeConfigService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        for (String uid : nodeConfigService.getOwnRetirementFundUids()) {
            try {
                nodeConfigService.getPrivateKey(uid);
            } catch (Exception e) {
                LOG.error("Configuration error: cannot read private key for UID " + uid);
            }
        }
        for (String uid : nodeConfigService.getOtherRetirementFundUids()) {
            try {
                nodeConfigService.getPublicKey(uid);
            } catch (Exception e) {
                LOG.error("Configuration error: cannot read public key for UID " + uid);
            }
        }
    }
}
