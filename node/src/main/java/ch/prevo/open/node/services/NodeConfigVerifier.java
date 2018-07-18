package ch.prevo.open.node.services;

import ch.prevo.open.node.config.NodeConfigurationService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class NodeConfigVerifier implements ApplicationListener<ApplicationReadyEvent> {

    @Inject
    private NodeConfigurationService nodeConfigService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        for (String uid : nodeConfigService.getOwnRetirementFundUids()) {
            try {
                nodeConfigService.getPrivateKey(uid);
            } catch (Exception e) {
                throw new RuntimeException("Configuration error: cannot read private key for UID " + uid, e);
            }
        }
        for (String uid : nodeConfigService.getOtherRetirementFundUids()) {
            try {
                nodeConfigService.getPublicKey(uid);
            } catch (Exception e) {
                throw new RuntimeException("Configuration error: cannot read public key for UID " + uid, e);
            }
        }
    }
}
