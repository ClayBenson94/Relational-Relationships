package helpers;

import objects.Interest;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Clay on 4/4/2016.
 */
public class SQLHelper {

    public static boolean execute(Connection conn, String query) {
        try {
            Statement stmt = conn.createStatement();
            return stmt.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

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
