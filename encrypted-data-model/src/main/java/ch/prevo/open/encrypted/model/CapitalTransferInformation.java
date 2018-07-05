package ch.prevo.open.encrypted.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@SuppressWarnings("unused")
public class CapitalTransferInformation {

    private String name;
    private String additionalName;
    private Address address;
    private String iban;

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
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .append(additionalName)
                .append(address)
                .append(iban)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("name", name)
                .append("additionalName", additionalName)
                .append("address", address)
                .append("iban", iban)
                .toString();
    }
}
