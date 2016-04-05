package tables;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import helpers.CSVHelper;
import helpers.SQLHelper;

public class InterestCategoriesTable {

  public static void createInterestCategoriesTable(Connection conn) {
     String query = "CREATE TABLE interest_categories("
        + "category_name VARCHAR(20),"
        + "PRIMARY KEY (category_name),"
        + ");";
     SQLHelper.execute(conn, query);
  }

  public static boolean addInterestCategory(Connection conn, String category) {
    String query = "INSERT INTO interest_categories "
            + "(category_name) VALUES (\'" + category
            + "\');";
    return SQLHelper.execute(conn, query);
  }

  public static ArrayList<String> getInterestCategoryNames(Connection conn) {
    ArrayList<String> returnList = new ArrayList<String>();
    String categoryToAdd;

    String query = "SELECT category_name FROM interest_categories;";
    ResultSet resultSet = SQLHelper.executeQuery(conn, query);

    try {
        while (resultSet.next()) {
          categoryToAdd = resultSet.getString("category_name");
          returnList.add(categoryToAdd);
        }
    } catch (SQLException | NullPointerException e) {
      e.printStackTrace();
    }
      return returnList;
  }

  public static boolean populateFromCSV(Connection conn) {

    CSVHelper reader = new CSVHelper();
    String categoryToAdd;

    reader.openCSV("resources/interestcategories.csv");
    while (reader.readRow()) {
      categoryToAdd = reader.currentRow.get(0);
      addInterestCategory(conn,categoryToAdd);
    }
    reader.closeCSV();

    return true;
  }

}
