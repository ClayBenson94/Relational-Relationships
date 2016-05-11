package objects;

/**
 * A class used to make in memory manipulation of locations easier
 */
public class Location {

    private Integer zipCode;
    private String state;
    private String city;

    /**
     * Construct a Location object
     * @param zipCode - The ZipCode to use in the Location
     * @param state - The State (e.g. NY, AL) corresponding to this Location
     * @param city - The City corresponding to this location
     */
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

    /**
     * Convert this object to a string
     * @return A space separated string of city, state, and zipcode.
     */
    public String toString() {
        return city + " " + state + ", " + zipCode;
    }
}
