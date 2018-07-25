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
package ch.prevo.open.encrypted.services;

import ch.prevo.open.data.api.CapitalTransferInformation;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CapitalTransferInfoEncrypter extends DataEncrypter<CapitalTransferInformation> {
    @Override
    protected byte[] toByteArray(CapitalTransferInformation data) throws IOException {
        String json = new ObjectMapper().writeValueAsString(data);
        return json.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    protected CapitalTransferInformation fromByteArray(byte[] data) throws IOException {
        return new ObjectMapper().readValue(data, CapitalTransferInformation.class);
    }
}