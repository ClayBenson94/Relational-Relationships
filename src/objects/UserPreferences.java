package objects;

/**
 * Created by nickj_000 on 3/16/2016.
 */
public class UserPreferences {

    private Integer preferredAgeMin;
    private Integer preferredAgeMax;
    private RelationshipController.Sexuality preferredSexuality;

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
