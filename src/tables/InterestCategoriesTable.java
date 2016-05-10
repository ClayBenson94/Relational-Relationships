package tables;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import helpers.CSVHelper;
import helpers.SQLHelper;

/**
 * Allows creation of InterestCategories table and 
 * adding new interest categories.
 */
public class InterestCategoriesTable {
    
    /**
     * Creates the InterestCategories table.
     * 
     * @param conn the connection to the database
     */ 
    public static void createInterestCategoriesTable(Connection conn) {
        String query = "CREATE TABLE interest_categories("
                + "category_name VARCHAR(20),"
                + "PRIMARY KEY (category_name),"
                + ");";
        SQLHelper.execute(conn, query);
    }

    /**
     * Adds an interest category to the interest_categories table.
     * 
     * @param conn the connection to the database
     * @param category the category to add to the table
     * @return true or false
     */ 
    public static boolean addInterestCategory(Connection conn, String category) {
        String query = "INSERT INTO interest_categories "
                + "(category_name) VALUES (\'" + category
                + "\');";
        return SQLHelper.execute(conn, query);
    }

    /**
     * Adds an interest category to the interest_categories table
     * with check of current categories in the table.
     * 
     * @param conn the connection to the database
     * @param category the category to add to the table
     * @return true or false
     */ 
    public static boolean addCategoryWithCheck(Connection conn, String category) {
        String query = "INSERT INTO interest_categories "
                + "(category_name) SELECT \'" + category
                + "\' FROM dual WHERE NOT EXISTS "
                + "( SELECT 1 "
                + "FROM interest_categories "
                + "WHERE category_name = \'" + category + "\'"
                + ");";
        return SQLHelper.execute(conn, query);
    }

    /**
     * Returns a list of interest category names from the
     * interest_categories table.
     * 
     * @param conn the connection to the database
     * @return arraylist of interest_category strings 
     */ 
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

    /**
     * Populates the interest_categories table using
     * the interestcategories.csv file in resources/csv/
     * 
     * @param conn the connection to the database
     */ 
    public static boolean populateFromCSV(Connection conn) {

        CSVHelper reader = new CSVHelper();
        String categoryToAdd;

        reader.openCSV("resources/csv/interestcategories.csv");
        while (reader.readRow()) {
            categoryToAdd = reader.currentRow.get(0);
            addInterestCategory(conn, categoryToAdd);
        }
        reader.closeCSV();

        return true;
    }

}
