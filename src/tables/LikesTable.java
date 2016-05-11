package tables;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import helpers.SQLHelper;
import objects.Like;

/**
 * Allows creation of likes table and adding new likes to the table.
 */ 
public class LikesTable {

    /**
     * Creates the likes table.
     * 
     * @param conn the connection to the database
     */ 
    public static void createLikesTable(Connection conn) {
        String query = "CREATE TABLE likes("
                + "sender VARCHAR(20),"
                + "receiver VARCHAR(20),"
                + "timestamp BIGINT UNSIGNED,"
                + "PRIMARY KEY (sender, receiver),"
                + "FOREIGN KEY (sender) REFERENCES user(username) ON DELETE CASCADE,"
                + "FOREIGN KEY (receiver) REFERENCES user(username) ON DELETE CASCADE"
                + ");";

        SQLHelper.execute(conn, query);
    }

    /**
     * Adds a like to the likes table.
     * 
     * @param conn the connection to the database
     * @param sender the username liking reciever
     * @param receiver the username being liked by sender
     */
    public static void createLike(Connection conn, String sender, String receiver) {
        String query = "INSERT INTO likes VALUES (\'"
                + sender + "\',\'"
                + receiver + "\',\'"
                + System.currentTimeMillis() + "\');";

        SQLHelper.execute(conn, query);
    }

    /**
     * Deletes a like from the likes table.
     * 
     * @param conn the connection to the database
     * @param sender the person who sent the like
     * @param receiver the person who received the like
     */
    public static void deleteLike(Connection conn, String sender, String receiver) {
        String query = "delete from likes where sender= \'"
                + sender + "\' and receiver=\'"
                + receiver + "\';";

        SQLHelper.execute(conn, query);
    }

    /**
     * Get who likes you
     *
     * @param conn
     * @param username - The user to do the query for
     * @return An array list of usernames that like you
     */
    public static ArrayList<Like> getLikesForUser(Connection conn, String username) {
        ArrayList<Like> likes = new ArrayList<Like>();
        try {
            String query = "SELECT sender, timestamp FROM likes WHERE receiver=\'" + username + "\' ;";

            ResultSet resultSet = SQLHelper.executeQuery(conn, query);

            while (resultSet.next()) {
                Like like = new Like(resultSet.getString("sender"), resultSet.getLong("timestamp"));
                likes.add(like);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return likes;
    }

    /**
     * Get matches
     *
     * @param conn the connection to the database
     * @param username - The user to do the query for
     * @return An array list of usernames that match
     */
    public static ArrayList<Like> getMatchesForUser(Connection conn, String username) {
        ArrayList<Like> likes = new ArrayList<Like>();
        try {
            String query = "SELECT l1.sender, 11.timestamp "
            + "FROM likes AS l1 JOIN likes AS l2 ON l1.receiver = l2.sender "
            + "WHERE l1.receiver=\'" + username + "\' AND l1.sender = l2.receiver;";
            ResultSet resultSet = SQLHelper.executeQuery(conn, query);

            while (resultSet.next()) {
                Like like = new Like(resultSet.getString("sender"), resultSet.getLong("timestamp"));
                likes.add(like);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return likes;
    }

    /**
     * Checks if a user has liked a given user.
     *
     * @param conn the connection to the database
     * @param username - The user to do the query for
     * @param otherUser the user who was liked
     * @return true or false
     */
    public static Boolean doesUserLike(Connection conn, String username, String otherUser) {
        try {
            String query = "SELECT * FROM likes WHERE sender=\'" + username +
                    "\' and receiver= \'" + otherUser + "\';";
            ResultSet resultSet = SQLHelper.executeQuery(conn, query);

            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
