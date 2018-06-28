package ch.prevo.open.data.api;

public class CapitalTransferInformation {
	private String name;
	private String iban;
	private Address address;

	public CapitalTransferInformation(String name, String iban) {
		this.name = name;
		this.iban = iban;
	}

	public Address getAddress() {
		return address;
	}
	public CapitalTransferInformation setAddress(Address address) {
		this.address = address;
		return this;
	}
	public String getName() {
		return name;
	}
	public CapitalTransferInformation setName(String name) {
		this.name = name;
		return this;
	}
	public String getIban() {
		return iban;
	}
	public CapitalTransferInformation setIban(String iban) {
		this.iban = iban;
		return this;
	}

	@Override
	public String toString() {
		return "CapitalTransferInformation{" +
				"name='" + name + '\'' +
				", iban='" + iban + '\'' +
				", address=" + address +
				'}';
	}
}
