package ch.prevo.open.node.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class Cryptography {

    private static final Logger LOG = LoggerFactory.getLogger(Cryptography.class);

    @PostConstruct
    public void checkAvailableFunctionality() {
        try {
            // Check hash-function
            final MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.digest("Hello World".getBytes(StandardCharsets.UTF_8));

            LOG.info("Setup of cryptography ok");
        } catch (NoSuchAlgorithmException e) {
            LOG.error("Setup of cryptography failed", e);
        }
    }

    public String hash(String value) {
        final String normalizedValue = value.replace(".", "");
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA-512");
            final byte[] bytes = digest.digest(normalizedValue.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(bytes);
        } catch (NoSuchAlgorithmException e) {
            // Should not happen
            return "";
        }
    }

}
