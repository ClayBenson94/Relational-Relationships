package objects;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

import tables.LocationTable;
import tables.UserInterestsTable;
import tables.UserTable;

/**
 * @author Clay Benson cmb3602@g.rit.edu
 */
public class User {

    private String username;
    private String password;
    private String name;
    private String bio;
    private String email;
    private Date dob;
    private RelationshipController.Gender gender;
    private RelationshipController.Sexuality sexuality;
    private int location;
    private UserPreferences userPreferences;
    private Boolean isAdmin;

    public User(String username, String password, String name, String bio, String email, Date dob,
                RelationshipController.Gender gender, RelationshipController.Sexuality sexuality, int location, Integer preferredAgeMin, Integer preferredAgeMax,
                RelationshipController.Sexuality preferredSexuality, Boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.bio = bio;
        this.email = email;
        this.dob = dob;
        this.gender = gender;
        this.sexuality = sexuality;
        this.location = location;
        this.userPreferences = new UserPreferences(preferredAgeMin, preferredAgeMax, preferredSexuality);
        this.isAdmin = isAdmin;

    }

    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }

    public Boolean getIsAdmin(){
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin){
        this.isAdmin = isAdmin;
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

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
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

    public int getAge() {
        return Period.between(dob.toLocalDate(), LocalDate.now()).getYears();
    }

    public void updateUser() {
        UserTable.updateUser(RelationshipController.getConnection(), this);
    }

    public String getUserString() {
        String userString = "";

        userString = userString + username + "\n\n";
        userString = userString + "Gender: " + gender + "\n\n";
        userString = userString + "Age: " + getAge() + "\n\n";
        userString = userString + "Sexuality: " + sexuality + "\n\n";
        userString = userString + "Looking for: " + userPreferences.getPreferredSexuality() + " ages " +
            userPreferences.getPreferredAgeMin() + " to " + userPreferences.getPreferredAgeMax() + "\n\n";
        userString = userString + LocationTable.getInformationViaZip(RelationshipController.getConnection(), location)
            + "\n\n";
        userString = userString + bio + "\n\n";

        ArrayList<Interest> userInterests = UserInterestsTable.getUserInterests(RelationshipController.getConnection(),
            username);

        if (userInterests.size() > 0) {
            String currentCategory = userInterests.get(0).getCategory();
            userString = userString + "Interests:\n    " + currentCategory + ":\n";
            for (Interest interest : userInterests) {
                if (interest.getCategory().equals(currentCategory)) {
                    userString = userString + "        " + interest.getName() + " - " + interest.getDescription() + "\n";
                } else {
                    currentCategory = interest.getCategory();
                    userString = userString + "    " + currentCategory + ":\n        " + interest.getName() + " - " + interest.getDescription() + "\n";
                }
            }
        }
        return userString;
    }
}
