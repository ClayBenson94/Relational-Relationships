package objects;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import tables.RelationalRelationships;


public class RelationshipController {

  private static Connection conn;
  private User activeUser;
  private User visitingUser;

    public static Connection getConnection(){
        return conn;
    }

  public enum Sexuality {
    Heterosexual, Homosexual
  }

  public enum Gender {
    Male, Female
  }
  //private stack visitedPages

  public static void main(String args[]) throws SQLException {
      RelationalRelationships relationalRelationships = new RelationalRelationships();
      relationalRelationships.createDB();
      conn = relationalRelationships.getConnection();

      try {
          Statement stmt = relationalRelationships.getConnection().createStatement();
          ResultSet resultSet = stmt.executeQuery("select * from location;");

          while (resultSet.next()) {
              // 0 is the username, 1 is the photo url
              System.out.println(resultSet.getString("zip_code") + ", " + resultSet.getString("State") + ", " +
                      resultSet.getString("City"));

          }
      }catch(SQLException e){
              e.printStackTrace();
      }
    }
}
