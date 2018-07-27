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
package ch.prevo.open.encrypted.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class EncryptedData implements Serializable {

    private static final long serialVersionUID = 7722287937301328033L;
    private byte[] encryptedData;
    private byte[] encryptedSymmetricKeyBundle;

    public EncryptedData() {
    }

    public EncryptedData(byte[] encryptedData, byte[] encryptedSymmetricKeyBundle) {
        this.encryptedData = encryptedData;
        this.encryptedSymmetricKeyBundle = encryptedSymmetricKeyBundle;
    }

    public byte[] getEncryptedData() {
        return encryptedData;
    }

    public byte[] getEncryptedSymmetricKeyBundle() {
        return encryptedSymmetricKeyBundle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        EncryptedData that = (EncryptedData) o;

        return new EqualsBuilder()
                .append(encryptedData, that.encryptedData)
                .append(encryptedSymmetricKeyBundle, that.encryptedSymmetricKeyBundle)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(encryptedData)
                .append(encryptedSymmetricKeyBundle)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("encryptedData", encryptedData)
                .append("encryptedSymmetricKeyBundle", encryptedSymmetricKeyBundle)
                .toString();
    }
}
