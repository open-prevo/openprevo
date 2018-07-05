package ch.prevo.open.data.api;

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

	public Address getAddress() {
		return address;
	}
	public CapitalTransferInformation setAddress(Address address) {
		this.address = address;
	return this;}
	public String getName() {
		return name;
	}
	public CapitalTransferInformation setName(String name) {
		this.name = name;
	return this;}
	public String getIban() {
		return iban;}

	public CapitalTransferInformation setIban(String iban) {
		this.iban = iban;
	return this;}

    public String getAdditionalName() {
        return additionalName;
    }

    public void setAdditionalName(String additionalName) {
        this.additionalName = additionalName;
    }

    @Override
    public String toString() {
        return "CapitalTransferInformation{" +
                "name='" + name + '\'' +
                "additionalName='" + additionalName + '\'' +
                ", iban='" + iban + '\'' +
                ", address=" + address +
                ", iban=" + iban +
                '}';
    }
}
