package ch.prevo.open.data.api;

public class Address {
    private String street;
    private String postalCode;
    private String city;

    public void setStreet(String street) {
        this.street = street;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                "postalCode='" + postalCode + '\'' +
                "city='" + city + '\'' +
                '}';
    }
}
