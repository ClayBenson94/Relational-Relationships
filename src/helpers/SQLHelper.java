package helpers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * A class to execute SQL statements
 */
public class SQLHelper {

    /**
     * Execute the given sql statement against the db with no data return
     * @param conn - the connection object to the database
     * @param query - the sql statement to run against the db
     * @return True if the sql statement executed successfully, else false
     */
    public static boolean execute(Connection conn, String query) {
        try {
            Statement stmt = conn.createStatement();
            return stmt.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Execute the given sql statement against the db with data return
     * @param conn - the connection object to the database
     * @param query - the sql statement to run against the db
     * @return The result statement created by the sql transaction
     */
    public static ResultSet executeQuery(Connection conn, String query) {
        try {
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ResultSet nullSet;
        nullSet = null;

        //noinspection ConstantConditions
        return nullSet;
    }
}
