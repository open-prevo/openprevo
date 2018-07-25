/*============================================================================*
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
 *===========================================================================*/
package ch.prevo.open.node.services;

import ch.prevo.open.encrypted.services.Cryptography;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CryptographyTest {

    @Test
    public void hashFunctionShouldIgnoreDots() {
        // given
        final String oasiNumber1 = "756.3412.8844.97";
        final String oasiNumber2 = "7563412884497";

        // when
        final String hash1 = Cryptography.digestOasiNumber(oasiNumber1);
        final String hash2 = Cryptography.digestOasiNumber(oasiNumber2);

        // then
        assertThat(hash1).isEqualTo(hash2);
    }

}
