package ch.prevo.open.data.api;

public class CapitalTransferInformation {
    private String name;
    private String additionalName;
    private Address address;
    private String reference;
    private String iban;
    private String isrReference;

    public CapitalTransferInformation() {
    }

    public CapitalTransferInformation(String name, String iban) {
        this.name = name;
        this.iban = iban;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getAdditionalName() {
        return additionalName;
    }

    public void setAdditionalName(String additionalName) {
        this.additionalName = additionalName;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getIsrReference() {
        return isrReference;
    }

    public void setIsrReference(String isrReference) {
        this.isrReference = isrReference;
    }

    @Override
    public String toString() {
        return "CapitalTransferInformation{" +
                "name='" + name + '\'' +
                "additionalName='" + additionalName + '\'' +
                ", iban='" + iban + '\'' +
                ", address=" + address +
                ", reference=" + reference +
                ", iban=" + iban +
                ", isrReference=" + isrReference +
                '}';
    }
}
