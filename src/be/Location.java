package be;

public class Location {
    private String street;
    private String additional;
    private String postalCode;
    private String country;
    private String city;


    public Location(String street, String additional, String postalCode, String country, String city) {
        this.street = street;
        this.additional = additional;
        this.postalCode = postalCode;
        this.country = country;
        this.city = city;

    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


}
