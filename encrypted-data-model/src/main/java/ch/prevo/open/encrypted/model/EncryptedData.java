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
package ch.prevo.open.encrypted.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

public class EncryptedData implements Serializable {

    private String encryptedDataBase64;
    private String encryptedSymmetricKeyBase64;
    private String ivBase64;
    private String signatureBase64;

    public EncryptedData() {
    }

    public EncryptedData(String encryptedDataBase64, String encryptedSymmetricKeyBase64, String ivBase64) {
        this.encryptedDataBase64 = encryptedDataBase64;
        this.encryptedSymmetricKeyBase64 = encryptedSymmetricKeyBase64;
        this.ivBase64 = ivBase64;
    }

    public EncryptedData(EncryptedData encryptedData, String signatureBase64) {
        this.encryptedDataBase64 = encryptedData.encryptedDataBase64;
        this.encryptedSymmetricKeyBase64 = encryptedData.encryptedSymmetricKeyBase64;
        this.ivBase64 = encryptedData.ivBase64;
        this.signatureBase64 = signatureBase64;
    }

    public String getEncryptedDataBase64() {
        return encryptedDataBase64;
    }

    public String getEncryptedSymmetricKeyBase64() {
        return encryptedSymmetricKeyBase64;
    }

    public String getIvBase64() {
        return ivBase64;
    }

    public Optional<String> getSignatureBase64() {
        return Optional.ofNullable(signatureBase64);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        EncryptedData that = (EncryptedData) o;

        return new EqualsBuilder()
                .append(encryptedDataBase64, that.encryptedDataBase64)
                .append(encryptedSymmetricKeyBase64, that.encryptedSymmetricKeyBase64)
                .append(ivBase64, that.ivBase64)
                .append(signatureBase64, that.signatureBase64)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(encryptedDataBase64)
                .append(encryptedSymmetricKeyBase64)
                .append(ivBase64)
                .append(signatureBase64)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("encryptedDataBase64", encryptedDataBase64)
                .append("encryptedSymmetricKeyBase64", encryptedSymmetricKeyBase64)
                .append("ivBase64", ivBase64)
                .append("signatureBase64", signatureBase64)
                .toString();
    }
}
