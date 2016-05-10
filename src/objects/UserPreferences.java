package objects;

/**
 * A class to represent a User Preference.
 * Holds all the appropriate information in a single object
 */
public class UserPreferences {

    private Integer preferredAgeMin;
    private Integer preferredAgeMax;
    private RelationshipController.Sexuality preferredSexuality;

    /**
     * Construct a UserPreferences object
     * @param preferredAgeMin - The minimum age that the user wishes to see when searching for other users
     * @param preferredAgeMax - The maximum age that the user wishes to see when searching for other users
     * @param preferredSexuality - The preferred sexuality that another user wishes to see when searching for users
     */
    public UserPreferences(Integer preferredAgeMin, Integer preferredAgeMax,
                           RelationshipController.Sexuality preferredSexuality) {
        this.preferredAgeMin = preferredAgeMin;
        this.preferredAgeMax = preferredAgeMax;
        this.preferredSexuality = preferredSexuality;
    }

    public Integer getPreferredAgeMin() {
        return preferredAgeMin;
    }

    public void setPreferredAgeMin(Integer preferredAgeMin) {
        this.preferredAgeMin = preferredAgeMin;
    }

    public Integer getPreferredAgeMax() {
        return preferredAgeMax;
    }

    public void setPreferredAgeMax(Integer preferredAgeMax) {
        this.preferredAgeMax = preferredAgeMax;
    }

    public RelationshipController.Sexuality getPreferredSexuality() {
        return preferredSexuality;
    }

    public void setPreferredSexuality(RelationshipController.Sexuality preferredSexuality) {
        this.preferredSexuality = preferredSexuality;
    }
}
