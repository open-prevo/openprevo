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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Cryptography {

    private static final Logger LOG = LoggerFactory.getLogger(Cryptography.class);

    private Cryptography() {}

    static {
        try {
            // Check digestOasiNumber-function
            final MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.digest("Hello World".getBytes(StandardCharsets.UTF_8));

            LOG.info("Setup of OASI cryptography ok");
        } catch (NoSuchAlgorithmException e) {
            LOG.error("Setup of cryptography failed", e);
        }
    }

    public static String digestOasiNumber(String value) {
        final String normalizedValue = value.trim().replace(".", "");
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA-512");
            final byte[] bytes = digest.digest(normalizedValue.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(bytes);
        } catch (NoSuchAlgorithmException e) {
            // Should not happen
            return "";
        }
    }

}
