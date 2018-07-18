package ch.prevo.open.encrypted.services;

import ch.prevo.open.encrypted.model.EncryptedData;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

/**
 * Uses AES/RSA to encrypt data using a public key, and decryption using a private key.
 *
 * @param <T> type of the data to encrypt
 */
public abstract class DataEncrypter<T> {

    private static final String SYMMETRIC_TRANSFORMATION_ALGORITHM = "AES";
    public static final String ASYMMETRIC_TRANSFORMATION_ALGORITHM = "RSA";


    public EncryptedData encrypt(T data, PublicKey publicKey) {
        try {
            SecretKey symmetricKey = createSymmetricKey();
            String encryptedDataBase64 = toBase64(encryptSymmetrically(data, symmetricKey));
            String encryptedSymmetricKeyBase64 = toBase64(encryptAsymmetrically(symmetricKey, publicKey));
            return new EncryptedData(encryptedDataBase64, encryptedSymmetricKeyBase64);
        } catch (GeneralSecurityException | IOException e) {
            throw new IllegalStateException("Could not encrypt data", e);
        }
    }

    public T decrypt(EncryptedData data, PrivateKey privateKey) {
        try {
            byte[] symmetricKey = decryptSymmentricKey(data, privateKey);
            byte[] decryptedData = decryptData(data, symmetricKey);
            return fromByteArray(decryptedData);
        } catch (GeneralSecurityException | IOException e) {
            throw new IllegalStateException("Could not decrypt data", e);
        }
    }

    protected abstract byte[] toByteArray(T data) throws IOException;

    protected abstract T fromByteArray(byte[] data) throws IOException;

    private String toBase64(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    private byte[] fromBase64(String data) {
        return Base64.getDecoder().decode(data);
    }

    private byte[] encryptAsymmetrically(SecretKey symmetricKeyToEncrypt, PublicKey publicEncodingKey) throws GeneralSecurityException {
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

    private byte[] decryptData(EncryptedData data, byte[] symmetricKey) throws GeneralSecurityException {
        SecretKeySpec symmetricKeySpec = new SecretKeySpec(symmetricKey, SYMMETRIC_TRANSFORMATION_ALGORITHM);
        Cipher symmetricCipher = Cipher.getInstance(SYMMETRIC_TRANSFORMATION_ALGORITHM);
        symmetricCipher.init(Cipher.DECRYPT_MODE, symmetricKeySpec);
        return symmetricCipher.doFinal(fromBase64(data.getEncryptedDataBase64()));
    }

    private byte[] decryptSymmentricKey(EncryptedData data, PrivateKey privateKey) throws GeneralSecurityException {
        Cipher asymmetricCipher = Cipher.getInstance(ASYMMETRIC_TRANSFORMATION_ALGORITHM);
        asymmetricCipher.init(Cipher.DECRYPT_MODE, privateKey);
        return asymmetricCipher.doFinal(fromBase64(data.getEncryptedSymmetricKeyBase64()));
    }

}
