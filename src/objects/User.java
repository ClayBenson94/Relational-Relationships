package objects;

/**
 * @author Clay Benson cmb3602@g.rit.edu
 */
public class User {

    private String username;
    private String password;
    private String name;
    private String bio;
    private String email;
    private long dob;
    private RelationshipController.Gender gender;
    private RelationshipController.Sexuality sexuality;
    private int location;
    private UserPreferences userPreferences;

    public User(String username, String password, String name, String bio, String email, long dob,
                RelationshipController.Gender gender, RelationshipController.Sexuality sexuality, int location) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.bio = bio;
        this.email = email;
        this.dob = dob;
        this.gender = gender;
        this.sexuality = sexuality;
        this.location = location;
    }

    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDob() {
        return dob;
    }

    public void setDob(long dob) {
        this.dob = dob;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RelationshipController.Gender getGender() {
        return gender;
    }

    public void setGender(RelationshipController.Gender gender) {
        this.gender = gender;
    }

    public RelationshipController.Sexuality getSexuality() {
        return sexuality;
    }

    public void setSexuality(RelationshipController.Sexuality sexuality) {
        this.sexuality = sexuality;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public UserPreferences getUserPreferences() {
        return userPreferences;
    }

    public void setUserPreferences(UserPreferences userPreferences) {
        this.userPreferences = userPreferences;
    }
}
