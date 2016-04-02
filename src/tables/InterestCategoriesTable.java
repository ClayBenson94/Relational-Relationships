package tables;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class InterestCategoriesTable {

  public static void createInterestCategoriesTable(Connection conn) {
    try {
      String query = "CREATE TABLE interest_categories("
        + "category_name VARCHAR(20),"
        + "PRIMARY KEY (category_name),"
        + ");";

      /**
       * Create a query and execute
       */
      Statement stmt = conn.createStatement();
      stmt.execute(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static boolean addInterestCategory(Connection conn, String category) {
    try {
      String query = "INSERT INTO interest_categories "
        + "VALUES (category_name=\'" + category
        + "\');";

      Statement stmt = conn.createStatement();
      return stmt.execute(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public static ArrayList<String> getInterestCategoryNames(Connection conn) {
    ArrayList<String> returnList = new ArrayList<String>();
    String categoryToAdd;

    try {
      String query = "SELECT category_name FROM interest_categories;";

      Statement stmt = conn.createStatement();
      ResultSet resultSet = stmt.executeQuery(query);
      while (resultSet.next()) {
        categoryToAdd = resultSet.getString("category_name");
        returnList.add(categoryToAdd);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return returnList;
  }

}
