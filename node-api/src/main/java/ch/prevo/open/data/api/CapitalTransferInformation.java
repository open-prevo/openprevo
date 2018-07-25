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
package ch.prevo.open.data.api;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import ch.prevo.open.encrypted.model.Address;

@SuppressWarnings("unused")
public class CapitalTransferInformation {

    @NotNull
    private String name;

    private String additionalName;

    @Valid
    @NotNull
    private Address address;

    @NotNull
    private String iban;

    private String referenceId;

    public CapitalTransferInformation() {
    }

    public CapitalTransferInformation(String name, String iban) {
        this.name = name;
        this.iban = iban;
    }

    public CapitalTransferInformation(String name, String additionalName, Address address, String iban) {
        this.name = name;
        this.additionalName = additionalName;
        this.address = address;
        this.iban = iban;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdditionalName() {
        return additionalName;
    }

    public void setAdditionalName(String additionalName) {
        this.additionalName = additionalName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        CapitalTransferInformation that = (CapitalTransferInformation) o;

        return new EqualsBuilder()
                .append(name, that.name)
                .append(additionalName, that.additionalName)
                .append(address, that.address)
                .append(iban, that.iban)
                .append(referenceId, that.referenceId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .append(additionalName)
                .append(address)
                .append(iban)
                .append(referenceId)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("name", name)
                .append("additionalName", additionalName)
                .append("address", address)
                .append("iban", iban)
                .append("referenceId", referenceId)
                .toString();
    }
}
