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
package ch.prevo.open.node.crypto;

import ch.prevo.open.encrypted.model.EncryptedData;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.*;
import java.util.Base64;

/**
 * Uses AES/RSA to encrypt data using a public key, and decryption using a private key.
 *
 * @param <T> type of the data to encrypt
 */
public abstract class DataEncrypter<T> {

    private static final String SYMMETRIC_TRANSFORMATION_ALGORITHM = "AES";
    private static final String SYMMETRIC_TRANSFORMATION = "AES/CBC/PKCS5Padding";
    public static final String ASYMMETRIC_TRANSFORMATION_ALGORITHM = "RSA";
    private static final String ASYMMETRIC_TRANSFORMATION = "RSA/ECB/OAEPWithSHA-1AndMGF1Padding";
    private static final int SYMMETRIC_KEY_SIZE = 256;
    private static final int SYMMETRIC_BLOCK_SIZE_BYTES = 16;


    public EncryptedData encrypt(T data, PublicKey publicKey) {
        try {
            SecretKey symmetricKey = createSymmetricKey();
            IvParameterSpec iv = createIV();
            String encryptedDataBase64 = toBase64(encryptSymmetrically(data, symmetricKey, iv));
            String encryptedSymmetricKeyBase64 = toBase64(encryptAsymmetrically(symmetricKey, publicKey));
            String ivBase64 = toBase64(iv.getIV());
            return new EncryptedData(encryptedDataBase64, encryptedSymmetricKeyBase64, ivBase64);
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
        Cipher asymmetricCipher = Cipher.getInstance(ASYMMETRIC_TRANSFORMATION);
        asymmetricCipher.init(Cipher.ENCRYPT_MODE, publicEncodingKey);
        return asymmetricCipher.doFinal(symmetricKeyToEncrypt.getEncoded());
    }

    private byte[] encryptSymmetrically(T info, SecretKey symmetricKey, IvParameterSpec iv) throws GeneralSecurityException, IOException {
        Cipher symmetricCipher = Cipher.getInstance(SYMMETRIC_TRANSFORMATION);
        symmetricCipher.init(Cipher.ENCRYPT_MODE, symmetricKey, iv);
        return symmetricCipher.doFinal(toByteArray(info));
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

    private byte[] decryptData(EncryptedData data, byte[] symmetricKey) throws GeneralSecurityException {
        SecretKeySpec symmetricKeySpec = new SecretKeySpec(symmetricKey, SYMMETRIC_TRANSFORMATION_ALGORITHM);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(fromBase64(data.getIvBase64()));
        Cipher symmetricCipher = Cipher.getInstance(SYMMETRIC_TRANSFORMATION);
        symmetricCipher.init(Cipher.DECRYPT_MODE, symmetricKeySpec, ivParameterSpec);
        return symmetricCipher.doFinal(fromBase64(data.getEncryptedDataBase64()));
    }

    private byte[] decryptSymmentricKey(EncryptedData data, PrivateKey privateKey) throws GeneralSecurityException {
        Cipher asymmetricCipher = Cipher.getInstance(ASYMMETRIC_TRANSFORMATION);
        asymmetricCipher.init(Cipher.DECRYPT_MODE, privateKey);
        return asymmetricCipher.doFinal(fromBase64(data.getEncryptedSymmetricKeyBase64()));
    }

}
