package objects;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by nickj_000 on 3/16/2016.
 */
public class RelationshipController {

  private static Connection connection;
  private User activeUser;
  private User visitingUser;

  public RelationshipController() {
    createConnection("~/h2demo/h2demo", "scj", "password");
  }

  public enum Sexuality {
    Heterosexual, Homosexual
  }

  public enum Gender {
    Male, Female
  }

  public static Connection getConnection() {
    return connection;
  }

  private void createConnection(String location,
                                String user,
                                String password){
    try {
      String url = "jdbc:h2:" + location;
      Class.forName("org.h2.Driver");
      connection = DriverManager.getConnection(url, user, password);
    } catch (SQLException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  //private stack visitedPages
}
