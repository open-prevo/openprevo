package ch.prevo.open.node.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static ch.prevo.open.encrypted.services.DataEncrypter.ASYMMETRIC_TRANSFORMATION_ALGORITHM;

@Service
public class NodeConfigurationService {

    private static Logger LOG = LoggerFactory.getLogger(NodeConfigurationService.class);

    private final ResourceLoader loader;
    private final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    @Value("${open.prevo.node.config.file}")
    private String configFile;
    private NodeConfigurationData config;

    @Inject
    public NodeConfigurationService(ResourceLoader loader) {
        this.loader = loader;
    }

    @PostConstruct
    public void init() {
        try {
            final Resource resource = loader.getResource(configFile);
            config = mapper.readValue(resource.getInputStream(), new TypeReference<NodeConfigurationData>() {
            });
        } catch (IOException e) {
            LOG.warn("Unable to read bootstrap-data from " + configFile, e);
        }
    }

    public Set<String> getOwnRetirementFundUids() {
        return config.ownRetirementFunds.keySet();
    }

    public Set<String> getOtherRetirementFundUids() {
        return config.otherRetirementFunds.keySet();
    }

    public PrivateKey getPrivateKey(String uid) {
        try {
            return KeyFactory.getInstance(ASYMMETRIC_TRANSFORMATION_ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(config.ownRetirementFunds.get(uid).base64PrivateKey)));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new IllegalStateException("Configuration error, cannot read private key", e);
        }
    }

    public java.security.PublicKey getPublicKey(String uid) {
        PublicKey publicKey = config.otherRetirementFunds.get(uid);
        if (publicKey == null) {
            throw new IllegalStateException("Configuration error: cannot locate public key for pension fund " + uid);
        }
        try {
            return KeyFactory.getInstance(ASYMMETRIC_TRANSFORMATION_ALGORITHM).generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey.base64PublicKey)));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new IllegalStateException("Configuration error: cannot read private key", e);
        }
    }

    private static class NodeConfigurationData {
        public Map<String, PublicPrivateKeys> ownRetirementFunds = new HashMap<>();
        public Map<String, PublicKey> otherRetirementFunds = new HashMap<>();
    }

    private static class PublicPrivateKeys {
        public String base64PrivateKey;
        public String base64PublicKey;
    }

    private static class PublicKey {
        public String base64PublicKey;
    }

}
