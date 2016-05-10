package tables;

import helpers.SQLHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

/**
 * Supports retrieval of statistics in the system including number of
 * visits, likes and matches during a given time interval
 */ 
public class RelationalRelationshipsStats {
    
    /**
     * Returns the total visit count of visits occurring in the system
     * given a time delta.
     * 
     * @param conn the connection to the database
     * @param timeDelta the time range to check for
     * @return the number of the visits that occurred in the given range
     */ 
    public static int getVisitCount(Connection conn, String timeDelta){

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(System.currentTimeMillis()));

        switch (timeDelta) {
            case "Hour":
                cal.add(Calendar.HOUR, -1);
                break;
            case "Day":
                cal.add(Calendar.DAY_OF_MONTH, -1);
                break;
            case "Week":
                cal.add(Calendar.WEEK_OF_MONTH, -1);
                break;
            case "Month":
                cal.add(Calendar.MONTH, -1);
                break;
            case "Year":
                cal.add(Calendar.YEAR, -1);
                break;
            default:
                System.out.println("Time Delta not recognized, no query could be constructed");
                break;
        }
        String query = "SELECT count(*) FROM visit WHERE timestamp > " + cal.getTimeInMillis() + ";";

    ResultSet resultSet = SQLHelper.executeQuery(conn, query);
    try {
        if (resultSet.next()) {
            return resultSet.getInt("Count(*)");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return 0;
    }

    /**
     * Returns the total like count of likes occurring in the system
     * given a time delta.
     * 
     * @param conn the connection to the database
     * @param timeDelta the time range to check for
     * @return the number of the likes that occurred in the given range
     */
    public static int getLikeCount(Connection conn, String timeDelta){

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(System.currentTimeMillis()));

        switch (timeDelta) {
            case "Hour":
                cal.add(Calendar.HOUR, -1);
                break;
            case "Day":
                cal.add(Calendar.DAY_OF_MONTH, -1);
                break;
            case "Week":
                cal.add(Calendar.WEEK_OF_MONTH, -1);
                break;
            case "Month":
                cal.add(Calendar.MONTH, -1);
                break;
            case "Year":
                cal.add(Calendar.YEAR, -1);
                break;
            default:
                System.out.println("Time Delta not recognized, no query could be constructed");
                break;
        }
        String query = "SELECT count(*) FROM likes WHERE timestamp > " + cal.getTimeInMillis() + ";";;

        ResultSet resultSet = SQLHelper.executeQuery(conn, query);
        try {
            if (resultSet.next()) {
                return resultSet.getInt("Count(*)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Returns the total match count of matches occurring in the system
     * given a time delta.
     * 
     * @param conn the connection to the database
     * @param timeDelta the time range to check for
     * @return the number of the matches that occurred in the given range
     */
    public static int getMatchCount(Connection conn, String timeDelta){

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(System.currentTimeMillis()));

        switch (timeDelta) {
            case "Hour":
                cal.add(Calendar.HOUR, -1);
                break;
            case "Day":
                cal.add(Calendar.DAY_OF_MONTH, -1);
                break;
            case "Week":
                cal.add(Calendar.WEEK_OF_MONTH, -1);
                break;
            case "Month":
                cal.add(Calendar.MONTH, -1);
                break;
            case "Year":
                cal.add(Calendar.YEAR, -1);
                break;
            default:
                System.out.println("Time Delta not recognized, no query could be constructed");
                break;
        }

        String query = "select count(*)/2 from likes l " +
                "join likes l2 on l.sender=l2.receiver and l.receiver = l2.sender where l.timestamp > "
                + cal.getTimeInMillis() + "or l2.timestamp > " + cal.getTimeInMillis() + ";";

        ResultSet resultSet = SQLHelper.executeQuery(conn, query);
        try {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
