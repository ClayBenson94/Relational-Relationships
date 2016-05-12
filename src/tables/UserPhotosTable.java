package tables;

import helpers.CSVHelper;
import helpers.SQLHelper;
import objects.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Allows creation of user_photos table and adding photo URLS to users.
 */ 
public class UserPhotosTable {

    /**
     * Creates the user_photos table.
     * @param conn the connection to the database
     */ 
    public static void createUserPhotosTable(Connection conn) {
        String query = "CREATE TABLE user_photos("
                + "username VARCHAR(20),"
                + "photo_url VARCHAR(1024),"
                + "PRIMARY KEY (username, photo_url),"
                + "FOREIGN KEY (username) REFERENCES user(username) ON DELETE CASCADE,"
                + ");";

        SQLHelper.execute(conn, query);
    }

    /**
     * Adds a photo URL to a user.
     * 
     * @param conn the connection to the database
     * @param username the username receiving the photo
     * @param photoURL the URL of the image
     * @return true or false
     */ 
    public static boolean addPhotoToUser(Connection conn, String username, String photoURL) {
        String query = "INSERT INTO user_photos (username, photo_url) "
                + "VALUES (\'" + username
                + "\',\'" + photoURL + "\');";

        return SQLHelper.execute(conn, query);
    }

    /**
     * Adds a photo URL to a user.
     * 
     * @param conn the connection to the database
     * @param username the username receiving the photo
     * @param photoURL the URL of the image
     * @return true or false
     */ 
    public static boolean addPhotoToUserWithCheck(Connection conn, String username, String photoURL) {
        String query = "INSERT INTO user_photos (username, photo_url) "
                + "SELECT \'" + username
                + "\', \'" + photoURL + "\' FROM dual WHERE NOT EXISTS "
                + "( SELECT 1 "
                + "FROM user_photos "
                + "WHERE username = \'" + username + "\'"
                + " AND photo_url = \'" + photoURL + "\'"
                + ");";

        return SQLHelper.execute(conn, query);
    }

    /**
     * Deletes a photo URL for a user.
     * 
     * @param conn the connection to the database
     * @param username the username with the photo
     * @param photoURL the URL of the image
     */ 
    public static void deleteUserPhoto(Connection conn, User user, String photoURL) {
        String query = "DELETE FROM user_photos "
                + "WHERE username=\'" + user.getUsername() + "\' AND photo_url=\'" + photoURL + "\';";

        SQLHelper.execute(conn, query);
    }

    /**
     * Returns a list of photo URLs for a given user
     * 
     * @param conn the connection to the database
     * @param user the username receiving the photo
     * @return the arraylist of photo URLs for the user
     */ 
    public static ArrayList<String> getUserPhotos(Connection conn, User user) {
        ArrayList<String> userPhotos = new ArrayList<String>();

        String query = "SELECT * FROM user_photos "
                + "WHERE username=\'" + user.getUsername() + "\';";

        ResultSet resultSet = SQLHelper.executeQuery(conn, query);
        try {
            while (resultSet.next()) {
                // 1 is the username, 2 is the photo url
                userPhotos.add(resultSet.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userPhotos;
    }

    /**
     * Populates the user_photos table with data from the
     * userphotos.csv file in resources/csv/
     * 
     * @param conn the connection to the database
     * @return true or false
     */ 
    public static boolean populateFromCSV(Connection conn) {
        CSVHelper reader = new CSVHelper();
        String username, photoURL;

        reader.openCSV("resources/csv/userphotos.csv");
        while (reader.readRow()) {
            username = reader.currentRow.get(0);
            photoURL = reader.currentRow.get(1);

            addPhotoToUser(conn, username, photoURL);
        }
        reader.closeCSV();

        return true;
    }
}
