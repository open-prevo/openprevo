/*******************************************************************************
 * Copyright (c) 2018 - Prevo-System AG and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License, v. 2.0 are satisfied: GNU General Public License, version 3
 * with the GNU Classpath Exception which is
 * available at https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-3.0 WITH Classpath-exception-2.0
 *
 * Contributors:
 *     Prevo-System AG - initial API and implementation
 ******************************************************************************/
package ch.prevo.open.encrypted.services;

import ch.prevo.open.encrypted.model.EncryptedData;
import ch.prevo.open.encrypted.model.SignedEncryptionContainer;
import ch.prevo.open.encrypted.model.SymmetricKeyBundle;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Base64;

/**
 * Uses AES/RSA to encrypt data using a public key, and decryption using a private key.
 * In addition the encrypted data is signed with SHA256withRSA before it is encrypted to provide a signature that will be
 * validated during decryption of the data.
 *
 */
public class DataEncryptionService {

    private static final String SYMMETRIC_TRANSFORMATION_ALGORITHM = "AES";
    private static final String SYMMETRIC_TRANSFORMATION = "AES/CBC/PKCS5Padding";
    public static final String ASYMMETRIC_TRANSFORMATION_ALGORITHM = "RSA";
    private static final String ASYMMETRIC_TRANSFORMATION = "RSA/ECB/OAEPWithSHA-1AndMGF1Padding";
    private static final int SYMMETRIC_KEY_SIZE = 256;
    private static final int SYMMETRIC_BLOCK_SIZE_BYTES = 16;
    private static final String SIGNING_ALGORITHM = "SHA256withRSA";
    private final ObjectMapper objectMapper = new ObjectMapper();


    public <T> EncryptedData encryptAndSign(T data, PublicKey publicKey, PrivateKey privateKey) {
        try {
            SecretKey symmetricKey = createSymmetricKey();
            IvParameterSpec iv = createIV();

            String signature = sign(toByteArray(data), privateKey);
            SignedEncryptionContainer<T> signedEncryptionContainer = new SignedEncryptionContainer<>(data, signature);
            String encryptedDataBase64 = toBase64(encryptSymmetrically(signedEncryptionContainer, symmetricKey, iv));

            SymmetricKeyBundle symmetricKeyBundle = new SymmetricKeyBundle(toBase64(symmetricKey.getEncoded()), toBase64(iv.getIV()));
            String encryptedSymmetricKeyBase64 = toBase64(encryptAsymmetrically(symmetricKeyBundle, publicKey));
            return new EncryptedData(encryptedDataBase64, encryptedSymmetricKeyBase64);
        } catch (GeneralSecurityException | IOException e) {
            throw new IllegalStateException("Could not encrypt data", e);
        }
    }

    public <T> T decryptAndVerify(EncryptedData data, Class<T> clazz, PrivateKey privateKey, PublicKey signKey) throws InvalidSignatureException {
        try {
            SymmetricKeyBundle symmetricKeyBundle = decryptSymmetricKeyBundle(data, privateKey);
            byte[] decryptedData = decryptData(data, symmetricKeyBundle);

            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(SignedEncryptionContainer.class, clazz);
            SignedEncryptionContainer<T> signedEncryptionContainer = fromByteArray(decryptedData, javaType);

            boolean verify = verify(signedEncryptionContainer, signKey);
            if (!verify) {
                throw new InvalidSignatureException("Invalid signature provided");
            }
            return signedEncryptionContainer.getData();
        } catch (GeneralSecurityException | IOException e) {
            throw new IllegalStateException("Could not decrypt data", e);
        }
    }

    private String sign(byte[] data, PrivateKey privateKey) {
        try {
            Signature privateSignature = Signature.getInstance(SIGNING_ALGORITHM);
            privateSignature.initSign(privateKey);
            privateSignature.update(data);
            return toBase64(privateSignature.sign());
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            throw new IllegalStateException("Could not sign data", e);
        }
    }

    private boolean verify(SignedEncryptionContainer<?> signedEncryptionContainer, PublicKey publicKey) {
        try {
            Signature publicSignature = Signature.getInstance(SIGNING_ALGORITHM);
            publicSignature.initVerify(publicKey);
            publicSignature.update(toByteArray(signedEncryptionContainer.getData()));
            byte[] signatureBytes = fromBase64(signedEncryptionContainer.getSignatureBase64());
            return publicSignature.verify(signatureBytes);
        } catch (NoSuchAlgorithmException | SignatureException | IOException | InvalidKeyException e) {
            throw new IllegalStateException("Could not verify data", e);
        }
    }

    protected <T> byte[] toByteArray(T data) throws IOException {
        String json = objectMapper.writeValueAsString(data);
        return json.getBytes(StandardCharsets.UTF_8);
    }

    protected <T> T fromByteArray(byte[] data, Class<T> clazz) throws IOException {
        return objectMapper.readValue(data, clazz);
    }

    protected <T> T fromByteArray(byte[] data, JavaType type) throws IOException {
        return objectMapper.readValue(data, type);
    }

    private String toBase64(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    private byte[] fromBase64(String data) {
        return Base64.getDecoder().decode(data);
    }

    private byte[] encryptAsymmetrically(SymmetricKeyBundle keyBundle, PublicKey publicEncodingKey) throws GeneralSecurityException, IOException {
        Cipher asymmetricCipher = Cipher.getInstance(ASYMMETRIC_TRANSFORMATION);
        asymmetricCipher.init(Cipher.ENCRYPT_MODE, publicEncodingKey);
        return asymmetricCipher.doFinal(toByteArray(keyBundle));
    }

    private byte[] encryptSymmetrically(SignedEncryptionContainer signedEncryptionContainer, SecretKey symmetricKey, IvParameterSpec iv) throws GeneralSecurityException, IOException {
        Cipher symmetricCipher = Cipher.getInstance(SYMMETRIC_TRANSFORMATION);
        symmetricCipher.init(Cipher.ENCRYPT_MODE, symmetricKey, iv);
        return symmetricCipher.doFinal(toByteArray(signedEncryptionContainer));
    }

    private IvParameterSpec createIV() {
        SecureRandom secureRandom = new SecureRandom();
        return new IvParameterSpec(secureRandom.generateSeed(SYMMETRIC_BLOCK_SIZE_BYTES));
    }

    private SecretKey createSymmetricKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(SYMMETRIC_TRANSFORMATION_ALGORITHM);
        keyGenerator.init(SYMMETRIC_KEY_SIZE);
        return keyGenerator.generateKey();
    }

    private byte[] decryptData(EncryptedData data, SymmetricKeyBundle symmetricKey) throws GeneralSecurityException {
        SecretKeySpec symmetricKeySpec = new SecretKeySpec(fromBase64(symmetricKey.getKey()), SYMMETRIC_TRANSFORMATION_ALGORITHM);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(fromBase64(symmetricKey.getIv()));
        Cipher symmetricCipher = Cipher.getInstance(SYMMETRIC_TRANSFORMATION);
        symmetricCipher.init(Cipher.DECRYPT_MODE, symmetricKeySpec, ivParameterSpec);
        return symmetricCipher.doFinal(fromBase64(data.getEncryptedDataBase64()));
    }

    private SymmetricKeyBundle decryptSymmetricKeyBundle(EncryptedData data, PrivateKey privateKey) throws GeneralSecurityException, IOException {
        Cipher asymmetricCipher = Cipher.getInstance(ASYMMETRIC_TRANSFORMATION);
        asymmetricCipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedKeyBundle = asymmetricCipher.doFinal(fromBase64(data.getEncryptedSymmetricKeyBundleBase64()));
        return fromByteArray(decryptedKeyBundle, SymmetricKeyBundle.class);
    }
}
