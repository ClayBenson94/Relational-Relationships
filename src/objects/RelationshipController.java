package objects;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import tables.*;


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

  public User getActiveUser() {
    return activeUser;
  }

  public User getVisitingUser() {
    return visitingUser;
  }

  public void updateUserObject(User user) {
    UserTable.updateUser(conn, user);
  }

  public void createUserObject(User user) {
    UserTable.addUser(conn, user);
  }

  public ArrayList<Like> getLikes(User user) {
    return LikesTable.getLikesForUser(conn, user.getUsername());
  }

  public void createLike(User sender, User receiver) {
    LikesTable.createLike(conn, sender, receiver);
  }

  public ArrayList<String> getUserPhotos(User user) {
    return UserPhotosTable.getUserPhotos(conn, user);
  }

  public void deleteUserPhoto(User user, String photoURL) {
    UserPhotosTable.deleteUserPhoto(conn, user, photoURL);
  }

  public ArrayList<String> getInterestCategoryNames() {
    return InterestCategoriesTable.getInterestCategoryNames(conn);
  }

  public Location getInformationViaZip(int zip) {
    return LocationTable.getInformationViaZip(conn, zip);
  }

  public ArrayList<Interest> getUserInterests(User user) {
    return UserInterestsTable.getUserInterests(conn, user.getUsername());
  }

  public void createInterest(Interest interest) {
    InterestTable.createInterest(conn, interest);
  }

  public boolean addInterestToUser(User user, Interest interest) {
    return UserInterestsTable.addInterestToUser(conn, user.getUsername(), interest);
  }

  public void createVisit(User visitor, User visited) {
    VisitTable.createVisit(conn, visitor.getUsername(), visited.getUsername());
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
      } catch(SQLException e) {
              e.printStackTrace();
      }
    }
}
