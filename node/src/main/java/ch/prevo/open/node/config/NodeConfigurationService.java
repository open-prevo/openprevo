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
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static ch.prevo.open.encrypted.services.DataEncrypter.ASYMMETRIC_TRANSFORMATION_ALGORITHM;

@Service
public class NodeConfigurationService {

    private static Logger LOG = LoggerFactory.getLogger(NodeConfigurationService.class);

    private final ResourceLoader loader;
    private final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    @Value("${open.prevo.node.config.file}")
    private String configFile;
    private NodeConfigurationRawData config;

    private final Map<String, PublicKey> otherRetirementFundsKeys = new HashMap<>();
    private final Map<String, PrivateKey> ownRetirementFundKeys = new HashMap<>();

    @Inject
    public NodeConfigurationService(ResourceLoader loader) {
        this.loader = loader;
    }

    @PostConstruct
    public void init() {
        try {
            final Resource resource = loader.getResource(configFile);
            NodeConfigurationRawData rawConfig = mapper.readValue(resource.getInputStream(), new TypeReference<NodeConfigurationRawData>() {
            });
            rawConfig.otherRetirementFunds.forEach((uid, publicKeyString) -> otherRetirementFundsKeys.put(uid, convertPublicKey(uid, publicKeyString)));
            //Note: we ignore own public keys for now, they're not in use
            rawConfig.ownRetirementFunds.forEach((uid, privateKeyStrings) -> ownRetirementFundKeys.put(uid, convertPrivateKey(uid, privateKeyStrings)));
            LOG.info("Node configuration with encryption keys ok");
        } catch (IOException e) {
            LOG.warn("Unable to read bootstrap-data from " + configFile, e);
        }
    }

    public PrivateKey getPrivateKey(String uid) {
        return ownRetirementFundKeys.get(uid);
    }

    public PublicKey getPublicKey(String uid) {
        return otherRetirementFundsKeys.get(uid);
    }

    private PublicKey convertPublicKey(String uid, PublicKeyString publicKey) {
        if (publicKey == null) {
            throw new IllegalStateException("Configuration error: cannot locate public key for pension fund " + uid);
        }
        try {
            return KeyFactory.getInstance(ASYMMETRIC_TRANSFORMATION_ALGORITHM).generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey.base64PublicKey)));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new IllegalStateException("Configuration error: cannot read private key", e);
        }
    }

    private PrivateKey convertPrivateKey(String uid, PublicPrivateKeyStrings publicPrivateKeyStrings) {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(publicPrivateKeyStrings.base64PrivateKey));
            return KeyFactory.getInstance(ASYMMETRIC_TRANSFORMATION_ALGORITHM).generatePrivate(keySpec);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new IllegalStateException("Configuration error, cannot read private key for pension fund " + uid, e);
        }

    }

    private static class NodeConfigurationRawData {
        public Map<String, PublicPrivateKeyStrings> ownRetirementFunds = new HashMap<>();
        public Map<String, PublicKeyString> otherRetirementFunds = new HashMap<>();
    }

    private static class PublicPrivateKeyStrings {
        public String base64PrivateKey;
        public String base64PublicKey;
    }

    private static class PublicKeyString {
        public String base64PublicKey;
    }

}