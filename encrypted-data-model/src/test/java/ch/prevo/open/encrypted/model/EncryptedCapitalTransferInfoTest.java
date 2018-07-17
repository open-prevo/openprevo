package ch.prevo.open.encrypted.model;

import ch.prevo.open.encrypted.services.EncryptedData;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class EncryptedCapitalTransferInfoTest {

    private KeyPair keys;

    private final KeyPairGenerator rsaGenerator;

    public EncryptedCapitalTransferInfoTest() throws NoSuchAlgorithmException {
        rsaGenerator = KeyPairGenerator.getInstance(EncryptedData.ASYMMETRIC_TRANSFORMATION_ALGORITHM);
    }

    @Before
    public void setup() {
        keys = rsaGenerator.generateKeyPair();
    }

    @Test
    public void encryptAndDecrypt() throws GeneralSecurityException, IOException {

        CapitalTransferInformation info = new CapitalTransferInformation("Name", "Iban");
        EncryptedCapitalTransferInfo encryptedInfo = new EncryptedCapitalTransferInfo(info, keys.getPublic());

        CapitalTransferInformation decryptedInfo = encryptedInfo.decryptData(keys.getPrivate());

        assertEquals(info, decryptedInfo);
    }

    @Test
    public void inputIsNull() throws GeneralSecurityException, IOException {

        EncryptedCapitalTransferInfo encryptedInfo = new EncryptedCapitalTransferInfo(null, keys.getPublic());
        CapitalTransferInformation decryptedInfo = encryptedInfo.decryptData(keys.getPrivate());

        assertNull(decryptedInfo);
    }

}
