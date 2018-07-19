package ch.prevo.open.encrypted.services;

import ch.prevo.open.encrypted.model.CapitalTransferInformation;
import ch.prevo.open.encrypted.model.EncryptedData;
import org.junit.Before;
import org.junit.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CapitalTransferInfoEncrypterTest {

    private KeyPair keys;

    private final KeyPairGenerator rsaGenerator;

    public CapitalTransferInfoEncrypterTest() throws NoSuchAlgorithmException {
        rsaGenerator = KeyPairGenerator.getInstance(DataEncrypter.ASYMMETRIC_TRANSFORMATION_ALGORITHM);
    }

    @Before
    public void setup() {
        keys = rsaGenerator.generateKeyPair();
    }

    @Test
    public void encryptAndDecrypt() {

        CapitalTransferInformation info = new CapitalTransferInformation("Name", "Iban");
        CapitalTransferInfoEncrypter capitalTransferInfoEncrypter = new CapitalTransferInfoEncrypter();

        EncryptedData encryptedData = capitalTransferInfoEncrypter.encrypt(info, keys.getPublic());
        CapitalTransferInformation decryptedInfo = capitalTransferInfoEncrypter.decrypt(encryptedData, keys.getPrivate());

        assertEquals(info, decryptedInfo);
    }

    @Test
    public void inputIsNull() {
        CapitalTransferInfoEncrypter capitalTransferInfoEncrypter = new CapitalTransferInfoEncrypter();
        EncryptedData encryptedData = capitalTransferInfoEncrypter.encrypt(null, keys.getPublic());

        CapitalTransferInformation decryptedInfo = capitalTransferInfoEncrypter.decrypt(encryptedData, keys.getPrivate());

        assertNull(decryptedInfo);
    }

}
