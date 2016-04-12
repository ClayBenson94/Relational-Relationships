package objects;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.*;

import helpers.SQLHelper;

import tables.InterestCategoriesTable;
import tables.InterestTable;
import tables.LikesTable;
import tables.LocationTable;
import tables.RelationalRelationships;
import tables.UserInterestsTable;
import tables.UserPhotosTable;
import tables.UserTable;
import tables.VisitTable;

import ui.ErrorView;
import ui.LoginView;
import ui.SearchView;
import ui.VisitingUserView;

public class RelationshipController {

    private static Connection conn;
    private User activeUser;
    private User visitingUser;

    private Stack<JFrame> visitedPages;

    public RelationshipController() {
        visitedPages = new Stack<JFrame>();
    }

    public static Connection getConnection() {
        return conn;
    }

    public enum Sexuality {
        Heterosexual, Homosexual, Something
    }

    public static Sexuality getSexuality(String sexualityStr) {
        for (Sexuality sexuality : Sexuality.values()) {
            if (sexuality.toString().toLowerCase().equals(sexualityStr.toLowerCase())) {
                return sexuality;
            }
        }
        return Sexuality.Something;
    }

    public enum Gender {
        Male, Female, Something
    }

    public static Gender getGender(String genderStr) {
        for (Gender gender : Gender.values()) {
            if (gender.toString().toLowerCase().equals(genderStr.toLowerCase())) {
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
        VisitTable.createVisit(conn, visited.getUsername(), visitor.getUsername());
        visitingUser = visited;
        JFrame nextPage = VisitingUserView.init(this);
        addPageToVistedPages(nextPage);
    }

    //UI methods
    public void startUI() {
        addPageToVistedPages(LoginView.init(this));
    }

    public void login(String username, String password) {
        boolean loginSuccess = UserTable.isValidLogin(conn, username, password);

        if (loginSuccess) {
            activeUser = UserTable.getUserObject(conn, username);
            //page transition
            JFrame nextPage = SearchView.init(this);
            addPageToVistedPages(nextPage);
        } else {
            //error popup
            createErrorView("Username/Password combination is incorrect.");
        }

    }

    public void register(User user) {
        //TODO validation on fields
        UserTable.addUser(conn, user);
        this.back();
    }

    public void addPageToVistedPages(JFrame nextPage) {
        if (!visitedPages.isEmpty()) {
            visitedPages.peek().setVisible(false);
            nextPage.setLocationRelativeTo(visitedPages.peek());
        } else {
            nextPage.setLocationRelativeTo(null);
        }
        nextPage.setVisible(true);
        visitedPages.push(nextPage);
    }

    public void createErrorView(String error) {
        JFrame nextPage = ErrorView.init(this, visitedPages.peek(), error);
        visitedPages.push(nextPage);
    }

    public void back() {
        visitedPages.peek().dispose();
        visitedPages.pop();
        visitedPages.peek().setVisible(true);
    }

    public ActionListener backListener(RelationshipController controller) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.back();
            }
        };
    }

    public ArrayList<User> search(String zipCode) {
        return UserTable.search(conn, zipCode, activeUser);
    }


    public static void main(String args[]) throws SQLException {
        RelationalRelationships relationalRelationships = new RelationalRelationships();

        //Check Arguments
        relationalRelationships.createConnection();
        for (String argument : args) {
            if (argument.equals("-n")) relationalRelationships.createPopulatedTables();
        }

        conn = relationalRelationships.getConnection();

        //Check if it's a first time run (Does USERS table exist?)
        ResultSet tableResults = SQLHelper.executeQuery(conn, "SELECT count(TABLE_NAME) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME='USER';");
        if (tableResults.next()) {
            if (tableResults.getInt("count(TABLE_NAME)") != 1) {
                relationalRelationships.createPopulatedTables();
            }
        }
        //UI
        RelationshipController controllerInstance = new RelationshipController();
        controllerInstance.startUI();
        //TODO close connection correclty
        //relationalRelationships.closeConnection();
    }
}
