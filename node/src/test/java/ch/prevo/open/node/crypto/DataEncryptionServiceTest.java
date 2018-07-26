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

import ch.prevo.open.data.api.CapitalTransferInformation;
import ch.prevo.open.data.api.Address;
import ch.prevo.open.encrypted.model.EncryptedData;
import org.junit.Before;
import org.junit.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThat;

public class DataEncryptionServiceTest {

    private KeyPair encryptionKeys;
    private KeyPair signingKeys;

    private final KeyPairGenerator rsaGenerator;
    private DataEncryptionService dataEncryptionService;
    private CapitalTransferInformation capitalTransferInformation;


    public DataEncryptionServiceTest() throws NoSuchAlgorithmException {
        rsaGenerator = KeyPairGenerator.getInstance(DataEncryptionService.ASYMMETRIC_TRANSFORMATION_ALGORITHM);
        dataEncryptionService = new DataEncryptionService();
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
        EncryptedData encryptedData = dataEncryptionService.encryptAndSign(capitalTransferInformation, encryptionKeys.getPublic(), signingKeys.getPrivate());
        CapitalTransferInformation decryptedInfo = dataEncryptionService.decryptAndVerify(encryptedData, CapitalTransferInformation.class, encryptionKeys.getPrivate(), signingKeys.getPublic());

        // then
        assertThat(decryptedInfo).isEqualTo(capitalTransferInformation);
    }

    @Test
    public void inputIsNull() {
        EncryptedData encryptedData = dataEncryptionService.encryptAndSign(null, encryptionKeys.getPublic(), signingKeys.getPrivate());

        CapitalTransferInformation decryptedInfo = dataEncryptionService.decryptAndVerify(encryptedData, CapitalTransferInformation.class, encryptionKeys.getPrivate(), signingKeys.getPublic());

        assertThat(decryptedInfo).isNull();
    }
}
