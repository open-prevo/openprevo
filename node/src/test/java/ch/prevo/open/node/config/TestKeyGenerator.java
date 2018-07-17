package ch.prevo.open.node.config;

import ch.prevo.open.encrypted.services.EncryptedData;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

public class TestKeyGenerator {

    public static void main(String... a) {
        try {
            for (int i = 0; i < 5; i++) {
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(EncryptedData.ASYMMETRIC_TRANSFORMATION_ALGORITHM);
                KeyPair keyPair = keyPairGenerator.generateKeyPair();
                System.out.println("Generated private key (base64 encoded PKCS#8 byte array):");
                System.out.println(Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
                System.out.println("Generated public key (base64 encoded x.509 byte array):");
                System.out.println(Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
