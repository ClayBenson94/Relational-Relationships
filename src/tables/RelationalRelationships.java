package tables;

import helpers.SQLHelper;
import objects.Visit;

import java.sql.*;

public class RelationalRelationships {

    //The connection to the database
    private Connection conn;

    /**
     * Create a database connection with the given params
     * @param user: user name for the owner of the database
     * @param password: password of the database owner
     */
    private void createConnection(String user,
                                 String password){
        try {

            //This needs to be on the front of your location
            String location = "~/RR/relationships";
            String url = "jdbc:h2:" + location;

            //This tells it to use the h2 driver
            Class.forName("org.h2.Driver");

            //creates the connection
            conn = DriverManager.getConnection(url,
                                               user,
                                               password);
        } catch (SQLException | ClassNotFoundException e) {
            //You should handle this better
            e.printStackTrace();
        }
    }

    /**
     * just returns the connection
     * @return: returns class level connection
     */
    public Connection getConnection(){
        return conn;
    }

    /**
     * When your database program exits
     * you should close the connection
     */
    public void closeConnection(){
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts and runs the database
     */
    public void createDB() {
        String user = "relations";
        String password = "password";

        //http://h2database.com/html/grammar.html#drop_all_objects
        //Have to create the conn object twice, because technically it's dropped by this command
        createConnection(user, password);
        SQLHelper.execute(conn, "DROP ALL OBJECTS;");

        //Create the database connections, basically makes the database
        createConnection(user, password);

        //Create location table and populate it with data
        LocationTable.createLocationTable(conn);
        LocationTable.populateFromCSV(conn);

        //Create User table and populate it with data
        UserTable.createUserTable(conn);
        UserTable.populateFromCSV(conn);

        //Create the InterestCategories table and populate it with data
        InterestCategoriesTable.createInterestCategoriesTable(conn);
        InterestCategoriesTable.populateFromCSV(conn);

        //Create the interests table and populate it with data
        InterestTable.createInterestTable(conn);
        InterestTable.populateFromCSV(conn);

        //Create the visits table and populate it with data
        VisitTable.createVisitTable(conn);
        VisitTable.populateFromCSV(conn);

        //Create the likes table and populate it with data
        LikesTable.createLikesTable(conn);
        LikesTable.populateFromCSV(conn);

        //Create the user interest table and populate it with data
        UserInterestsTable.createUserInterestTable(conn);
        UserInterestsTable.populateFromCSV(conn);

        //Create the user photos table and populate it with data
        UserPhotosTable.createUserPhotosTable(conn);
        UserPhotosTable.populateFromCSV(conn);
    }
}
