package objects;

/**
 * A class used to make in memory manipulation of locations easier
 */
public class Location {

    private Integer zipCode;
    private String state;
    private String city;

    public Location(Integer zipCode, String state, String city) {
        this.zipCode = zipCode;
        this.state = state;
        this.city = city;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }
}
