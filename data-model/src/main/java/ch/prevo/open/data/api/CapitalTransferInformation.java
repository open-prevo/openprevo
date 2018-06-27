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

	@Override
	public String toString() {
		return "CapitalTransferInformation{" +
				"name='" + name + '\'' +
				", iban='" + iban + '\'' +
				", address=" + address +
				'}';
	}
}
