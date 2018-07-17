package ch.prevo.open.encrypted.services;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Uses AES/RSA to encrypt data using a public key, and decryption using a private key.
 *
 * @param <T> type of the data to encrypt
 */
public abstract class EncryptedData<T> implements Serializable {

    public static final String SYMMETRIC_TRANSFORMATION_ALGORITHM = "AES";
    public static final String ASYMMETRIC_TRANSFORMATION_ALGORITHM = "RSA";

    private byte[] encryptedData;
    private byte[] encryptedSymmetricKey;

    protected EncryptedData() {
        // required for serialisation
    }

    public EncryptedData(T info, PublicKey publicEncodingKey) throws GeneralSecurityException, IOException {
        SecretKey symmetricKey = createSymmetricKey();
        encryptedData = encryptSymmetrically(info, symmetricKey);
        encryptedSymmetricKey = encryptAsymmetrically(publicEncodingKey, symmetricKey);
    }

    public T decryptData(PrivateKey privateKey) throws GeneralSecurityException, IOException {
        byte[] symmetricKey = decryptSymmentricKey(privateKey);
        byte[] decryptedData = decryptData(symmetricKey);
        return fromByteArray(decryptedData);
    }

    protected abstract byte[] toByteArray(T data) throws IOException;

    protected abstract T fromByteArray(byte[] data) throws IOException;

    private byte[] encryptAsymmetrically(PublicKey publicEncodingKey, SecretKey symmetricKeyToEncrypt) throws GeneralSecurityException {
        Cipher asymmetricCipher = Cipher.getInstance(ASYMMETRIC_TRANSFORMATION_ALGORITHM);
        asymmetricCipher.init(Cipher.ENCRYPT_MODE, publicEncodingKey);
        return asymmetricCipher.doFinal(symmetricKeyToEncrypt.getEncoded());
    }

    private byte[] encryptSymmetrically(T info, SecretKey symmetricKey) throws GeneralSecurityException, IOException {
        Cipher symmetricCipher = Cipher.getInstance(SYMMETRIC_TRANSFORMATION_ALGORITHM);
        symmetricCipher.init(Cipher.ENCRYPT_MODE, symmetricKey);
        return symmetricCipher.doFinal(toByteArray(info));
    }

    private SecretKey createSymmetricKey() throws NoSuchAlgorithmException {
        return KeyGenerator.getInstance(SYMMETRIC_TRANSFORMATION_ALGORITHM).generateKey();
    }

    private byte[] decryptData(byte[] symmetricKey) throws GeneralSecurityException {
        SecretKeySpec symmetricKeySpec = new SecretKeySpec(symmetricKey, SYMMETRIC_TRANSFORMATION_ALGORITHM);
        Cipher symmetricCipher = Cipher.getInstance(SYMMETRIC_TRANSFORMATION_ALGORITHM);
        symmetricCipher.init(Cipher.DECRYPT_MODE, symmetricKeySpec);
        return symmetricCipher.doFinal(encryptedData);
    }

    private byte[] decryptSymmentricKey(PrivateKey privateKey) throws GeneralSecurityException {
        Cipher asymmetricCipher = Cipher.getInstance(ASYMMETRIC_TRANSFORMATION_ALGORITHM);
        asymmetricCipher.init(Cipher.DECRYPT_MODE, privateKey);
        return asymmetricCipher.doFinal(encryptedSymmetricKey);
    }

}
