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
package ch.prevo.open.node.config;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

import static ch.prevo.open.encrypted.services.DataEncryptionService.ASYMMETRIC_TRANSFORMATION_ALGORITHM;

public class TestKeyGenerator {

    public static void main(String... a) {
        try {
            for (int i = 0; i < 5; i++) {
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ASYMMETRIC_TRANSFORMATION_ALGORITHM);
                KeyPair keyPair = keyPairGenerator.generateKeyPair();
                System.out.println("Generated private key (base64 encoded PKCS#8 byte array):");
                System.out.println(Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
                System.out.println("Generated public key (base64 encoded x.509 byte array):");
                System.out.println(Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
