package helpers;

import objects.Interest;
import objects.RelationshipController;
import objects.User;
import tables.InterestTable;
import tables.UserInterestsTable;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * A class for generating user_interest rows in the database
 */
public class InterestGen {

    /**
     * Generate 2 to 30 interests for every passed user
     * @param users - an ArrayList of users to add interests to
     */
    public void generateUserInterests(ArrayList<User> users) {
        String insertUserInterestQuery = "INSERT INTO user_interests(username, interest) VALUES %s;";
        Connection conn = RelationshipController.getConnection();

        // Get all the interests that are currently in the database
        ArrayList<Interest> interests = InterestTable.getInterests(conn);

        ArrayList<String> userInterestsToInsert = new ArrayList<>();

        for (User u : users) {
            // Create a copy of the ArrayList representing interests in the db so we can manipulate it
            ArrayList<Interest> copyOfInterests = new ArrayList<>(interests);

            // Get the interests the user already has, if any
            ArrayList<Interest> userInterests = UserInterestsTable.getUserInterests(conn, u.getUsername());

            // Remove all interests from the copy that the user already has
            copyOfInterests.removeAll(userInterests);

            // Add 2 to 30 interests to the user
            for (int i = RandomNumberHelper.randBetween(2, 30); i > 0; i--) {

                // Get a random interest the user does not already have
                int randomInterestIndex = RandomNumberHelper.randBetween(0, copyOfInterests.size() -1);
                Interest interestToAdd = copyOfInterests.get(randomInterestIndex);

                // Remove that interest from the copy list to prevent adding it again later
                copyOfInterests.remove(interestToAdd);

                userInterestsToInsert.add("(\'" + u.getUsername() + "\',\'" + interestToAdd.getName() + "\')");
            }
        }
        insertUserInterestQuery = String.format(insertUserInterestQuery, String.join(",", userInterestsToInsert));
        SQLHelper.execute(conn, insertUserInterestQuery);
    }
}
