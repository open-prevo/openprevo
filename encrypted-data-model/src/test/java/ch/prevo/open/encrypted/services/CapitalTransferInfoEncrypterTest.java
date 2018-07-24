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
