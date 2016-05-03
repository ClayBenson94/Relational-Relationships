package helpers;


import objects.RelationshipController;
import objects.User;
import tables.UserInterestsTable;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

public class InterestGen {

    public void generateUserInterests(ArrayList<User> users) {
        for (User u : users) {
            for (int i = RandomNumberHelper.randBetween(2, 30); i > 0; i--) {
                String query = "insert into user_interests values(\'" + u.getUsername() +
                        "\', (select interest_name from interests ORDER BY RAND() LIMIT 1));";
                Statement stmt = null;
                try {
                    stmt = RelationshipController.getConnection().createStatement();
                    stmt.executeUpdate(query);
                } catch (SQLException e) {
                    if (e.getSQLState().equals("23505")) {
                        // user already has that interest, do nothing
                    } else {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
