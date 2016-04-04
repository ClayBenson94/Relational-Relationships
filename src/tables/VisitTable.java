package tables;

import helpers.CSVHelper;
import helpers.SQLHelper;
import objects.User;
import objects.Visit;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class VisitTable {

  public static void createVisitTable(Connection conn) {
    String query = "CREATE TABLE visit("
      + "visited VARCHAR(20),"
      + "visitor VARCHAR(20),"
      + "timestamp BIGINT UNSIGNED,"
      + "PRIMARY KEY (visited, visitor),"
      + "FOREIGN KEY (visited) REFERENCES user(username),"
      + "FOREIGN KEY (visitor) REFERENCES user(username),"
      + ");";

    SQLHelper.execute(conn, query);
  }

  public static void createVisit(Connection conn, String visited, String visitor) {
    String query = "INSERT INTO visit VALUES (\'" + visited + "\', \'" + visitor + "\', " + System.currentTimeMillis() + ");";

    SQLHelper.execute(conn, query);
  }

  public ArrayList<Visit> getVisitsForUser(Connection conn, User user) {
    ArrayList<Visit> userVisits = new ArrayList<Visit>();

    String query = "SELECT * FROM visit WHERE visitor = \'" + user.getUsername() + "\';";

    ResultSet resultSet = SQLHelper.executeQuery(conn, query);
    try {
        while (resultSet.next()) {
            Visit visit = new Visit(resultSet.getString("visitor"), resultSet.getLong("timestamp"));
            userVisits.add(visit);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return userVisits;
    }

    public static boolean populateFromCSV(Connection conn) {

        CSVHelper reader = new CSVHelper();
        String visited, visitor;

        reader.openCSV("resources/visits.csv");
        while (reader.readRow()) {
            visited = reader.currentRow.get(0);
            visitor = reader.currentRow.get(1);

            createVisit(conn,visited,visitor);
        }
        reader.closeCSV();

        return true;
    }
}
