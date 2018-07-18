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
import java.util.Base64;
import java.util.Objects;

/**
 * Uses AES/RSA to encrypt data using a public key, and decryption using a private key.
 *
 * @param <T> type of the data to encrypt
 */
public abstract class EncryptedData<T> implements Serializable {

    public static final String SYMMETRIC_TRANSFORMATION_ALGORITHM = "AES";
    public static final String ASYMMETRIC_TRANSFORMATION_ALGORITHM = "RSA";

    private String encryptedDataBase64;
    private String encryptedSymmetricKeyBase64;

    protected EncryptedData() {
        // required for serialisation
    }

    public EncryptedData(T info, PublicKey publicEncodingKey) {
        try {
            SecretKey symmetricKey = createSymmetricKey();
            encryptedDataBase64 = toBase64(encryptSymmetrically(info, symmetricKey));
            encryptedSymmetricKeyBase64 = toBase64(encryptAsymmetrically(symmetricKey, publicEncodingKey));
        } catch (GeneralSecurityException | IOException e) {
            throw new IllegalStateException("Could not encrypt data", e);
        }
    }

    public String getEncryptedDataBase64() {
        return encryptedDataBase64;
    }

    public String getEncryptedSymmetricKeyBase64() {
        return encryptedSymmetricKeyBase64;
    }

    public void setEncryptedDataBase64(String encryptedDataBase64) {
        this.encryptedDataBase64 = encryptedDataBase64;
    }

    public void setEncryptedSymmetricKeyBase64(String encryptedSymmetricKeyBase64) {
        this.encryptedSymmetricKeyBase64 = encryptedSymmetricKeyBase64;
    }

    private String toBase64(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    private byte[] fromBase64(String data) {
        return Base64.getDecoder().decode(data);
    }

    public T decryptData(PrivateKey privateKey) {
        try {
            byte[] symmetricKey = decryptSymmentricKey(privateKey);
            byte[] decryptedData = decryptData(symmetricKey);
            return fromByteArray(decryptedData);
        } catch (GeneralSecurityException | IOException e) {
            throw new IllegalStateException("Could not decrypt data", e);
        }
    }

    protected abstract byte[] toByteArray(T data) throws IOException;

    protected abstract T fromByteArray(byte[] data) throws IOException;

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

    private byte[] decryptData(byte[] symmetricKey) throws GeneralSecurityException {
        SecretKeySpec symmetricKeySpec = new SecretKeySpec(symmetricKey, SYMMETRIC_TRANSFORMATION_ALGORITHM);
        Cipher symmetricCipher = Cipher.getInstance(SYMMETRIC_TRANSFORMATION_ALGORITHM);
        symmetricCipher.init(Cipher.DECRYPT_MODE, symmetricKeySpec);
        return symmetricCipher.doFinal(fromBase64(encryptedDataBase64));
    }

    private byte[] decryptSymmentricKey(PrivateKey privateKey) throws GeneralSecurityException {
        Cipher asymmetricCipher = Cipher.getInstance(ASYMMETRIC_TRANSFORMATION_ALGORITHM);
        asymmetricCipher.init(Cipher.DECRYPT_MODE, privateKey);
        return asymmetricCipher.doFinal(fromBase64(encryptedSymmetricKeyBase64));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EncryptedData<?> that = (EncryptedData<?>) o;
        return Objects.equals(encryptedDataBase64, that.encryptedDataBase64) &&
                Objects.equals(encryptedSymmetricKeyBase64, that.encryptedSymmetricKeyBase64);
    }

    @Override
    public int hashCode() {
        return Objects.hash(encryptedDataBase64, encryptedSymmetricKeyBase64);
    }

    @Override
    public String toString() {
        return "EncryptedData{" +
                "encryptedDataBase64='" + encryptedDataBase64 + '\'' +
                ", encryptedSymmetricKeyBase64='" + encryptedSymmetricKeyBase64 + '\'' +
                '}';
    }
}
