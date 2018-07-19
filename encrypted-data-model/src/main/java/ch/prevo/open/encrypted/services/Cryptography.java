package ch.prevo.open.encrypted.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Cryptography {

    private static final Logger LOG = LoggerFactory.getLogger(Cryptography.class);

    private Cryptography() {}

    static {
        try {
            // Check digestOasiNumber-function
            final MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.digest("Hello World".getBytes(StandardCharsets.UTF_8));

            LOG.info("Setup of OASI cryptography ok");
        } catch (NoSuchAlgorithmException e) {
            LOG.error("Setup of cryptography failed", e);
        }
    }

    public static String digestOasiNumber(String value) {
        final String normalizedValue = value.trim().replace(".", "");
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
