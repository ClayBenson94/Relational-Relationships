package objects;

import java.sql.*;
import java.util.ArrayList;
import java.util.Stack;

import oracle.jvm.hotspot.jfr.JFR;
import tables.*;

import ui.*;

import javax.swing.*;


public class RelationshipController {

  private static Connection conn;
  private User activeUser;
  private User visitingUser;

  private Stack<JFrame> visitedPages;

  public RelationshipController() {
      visitedPages = new Stack<JFrame>();
  }

  public static Connection getConnection(){
        return conn;
    }

  public enum Sexuality {
    Heterosexual, Homosexual, Something
  }

  public static Sexuality getSexuality(String sexualityStr){
    for (Sexuality sexuality : Sexuality.values()) {
        if (sexuality.toString().toLowerCase().equals(sexualityStr.toLowerCase())){
            return sexuality;
        }
    }
    return Sexuality.Something;
  }

  public enum Gender {
    Male, Female, Something
  }

  public static Gender getGender(String genderStr){
    for (Gender gender : Gender.values()) {
        if (gender.toString().toLowerCase().equals(genderStr.toLowerCase())){
            return gender;
        }
    }
    return Gender.Something;
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
    LikesTable.createLike(conn, sender.getUsername(), receiver.getUsername());
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

  //UI methods
  public void startUI() {
      visitedPages.push(LoginView.init(this, null));
  }

  public void login(String username, String password) {
      //TODO login
      //System.out.println("Open Login Page");

      //page transition
      JFrame nextPage = LoginView.init(this, visitedPages.peek());
      visitedPages.peek().setVisible(false);
      visitedPages.push(nextPage);
  }

  public void register(String username, String password) {
      //TODO register
      //System.out.println("Open Register Page");
  }


  public static void main(String args[]) throws SQLException {
    RelationalRelationships relationalRelationships = new RelationalRelationships();
    relationalRelationships.createDB();
    conn = relationalRelationships.getConnection();

    try {
      Statement stmt = conn.createStatement();
      ResultSet resultSet = stmt.executeQuery("select * from user;");

      ResultSetMetaData rsmd = resultSet.getMetaData();
      int columnsNumber = rsmd.getColumnCount();
      while (resultSet.next()) {
          for (int i = 1; i <= columnsNumber; i++) {
              if (i > 1) System.out.print(",  ");
              String columnValue = resultSet.getString(i);
              System.out.print(rsmd.getColumnName(i) + ": " + columnValue);
          }
          System.out.println("");
      }
    } catch(SQLException e) {
          e.printStackTrace();
    }
    relationalRelationships.closeConnection();

    //UI
    RelationshipController controllerInstance = new RelationshipController();
    controllerInstance.startUI();

    }
}
