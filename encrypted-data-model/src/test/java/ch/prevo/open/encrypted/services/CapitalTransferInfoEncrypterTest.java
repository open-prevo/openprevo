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

import ch.prevo.open.encrypted.model.Address;
import ch.prevo.open.encrypted.model.CapitalTransferInformation;
import ch.prevo.open.encrypted.model.EncryptedData;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

public class CapitalTransferInfoEncrypterTest {

    private KeyPair encryptionKeys;
    private KeyPair signingKeys;

    private final KeyPairGenerator rsaGenerator;
    private CapitalTransferInfoEncrypter capitalTransferInfoEncrypter;
    private CapitalTransferInformation capitalTransferInformation;


    public CapitalTransferInfoEncrypterTest() throws NoSuchAlgorithmException {
        rsaGenerator = KeyPairGenerator.getInstance(DataEncrypter.ASYMMETRIC_TRANSFORMATION_ALGORITHM);
        capitalTransferInfoEncrypter = new CapitalTransferInfoEncrypter();
        capitalTransferInformation = new CapitalTransferInformation("Fund Name", "Additional Name", new Address("Street 1", "4000", "Basel"), "CH42 1344 1244 1344 133");
    }

    @Before
    public void setup() {
        encryptionKeys = rsaGenerator.generateKeyPair();
        signingKeys = rsaGenerator.generateKeyPair();
    }

    @Test
    public void encryptAndDecrypt() {
        // when
        EncryptedData encryptedData = capitalTransferInfoEncrypter.encrypt(capitalTransferInformation, encryptionKeys.getPublic());
        CapitalTransferInformation decryptedInfo = capitalTransferInfoEncrypter.decrypt(encryptedData, encryptionKeys.getPrivate());

        // then
        assertThat(decryptedInfo).isEqualTo(capitalTransferInformation);
    }

    @Test
    public void inputIsNull() {
        EncryptedData encryptedData = capitalTransferInfoEncrypter.encrypt(null, encryptionKeys.getPublic());

        CapitalTransferInformation decryptedInfo = capitalTransferInfoEncrypter.decrypt(encryptedData, encryptionKeys.getPrivate());

        assertThat(decryptedInfo).isNull();
    }

    @Test
    public void encryptSignAndDecryptVerifyTest() {

        // when
        EncryptedData encryptedData = capitalTransferInfoEncrypter.encryptAndSign(capitalTransferInformation, encryptionKeys.getPublic(), signingKeys.getPrivate());
        Pair<CapitalTransferInformation, Boolean> decryptionResult = capitalTransferInfoEncrypter.decryptAndVerify(encryptedData, encryptionKeys.getPrivate(), signingKeys.getPublic());

        // then
        assertThat(decryptionResult.getRight()).isTrue();
        assertThat(decryptionResult.getLeft()).isEqualTo(capitalTransferInformation);
    }

    @Test
    public void encryptWithoutSignAndDecryptAndTryVerifyTest() {
        // given
        CapitalTransferInformation capitalTransferInformation = new CapitalTransferInformation("Fund Name", "Additional Name", new Address("Street 1", "4000", "Basel"), "CH42 1344 1244 1344 133");

        // when
        EncryptedData encryptedData = capitalTransferInfoEncrypter.encrypt(capitalTransferInformation, encryptionKeys.getPublic());
        try {
            capitalTransferInfoEncrypter.decryptAndVerify(encryptedData, encryptionKeys.getPrivate(), signingKeys.getPublic());
            fail();
        } catch (IllegalStateException e) {

            // then
            assertThat(e.getMessage()).isEqualTo("No signature provided");
        }
    }
}
