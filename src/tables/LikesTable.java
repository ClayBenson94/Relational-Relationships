package tables;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import helpers.CSVHelper;
import helpers.SQLHelper;
import objects.Like;
import objects.User;

public class LikesTable {

    public static void createLikesTable(Connection conn) {
        String query = "CREATE TABLE likes("
            + "sender VARCHAR(20),"
            + "receiver VARCHAR(20),"
            + "timestamp BIGINT UNSIGNED,"
            + "PRIMARY KEY (sender, receiver),"
            + "FOREIGN KEY (sender) REFERENCES user(username),"
            + "FOREIGN KEY (receiver) REFERENCES user(username)"
            + ");";

        SQLHelper.execute(conn, query);
    }

    public static void createLike(Connection conn, String sender, String receiver) {
        String query = "INSERT INTO likes VALUES (\'"
            + sender + "\',\'"
            + receiver + "\',\'"
            + System.currentTimeMillis() + "\');";

        SQLHelper.execute(conn, query);
    }

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
            String query = "SELECT sender, timestamp FROM likes WHERE receiver=\'" + username + "\';";
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
     * Has user liked given user
     *
     * @param conn
     * @param username - The user to do the query for
     * @return An array list of usernames that like you
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

    public static boolean populateFromCSV(Connection conn) {

        CSVHelper reader = new CSVHelper();
        String sender, receiver;

        reader.openCSV("resources/csv/likes.csv");
        while (reader.readRow()) {
            sender = reader.currentRow.get(0);
            receiver = reader.currentRow.get(1);

            createLike(conn, sender, receiver);
        }
        reader.closeCSV();

        return true;
    }

}
