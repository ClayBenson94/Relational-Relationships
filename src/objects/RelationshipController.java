package objects;

import java.awt.event.ActionListener;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.*;

import helpers.*;

import tables.*;

import ui.*;

/**
 * The controller for the entire RelationalRelationships program.
 *
 * This class houses all of the methods that are called to from UI classes which need to update tables in the database
 * or perform logic outside of the UI.
 *
 * This class also instantiates the UI, and makes calls to begin data (user, like, etc.) generation.
 */
public class RelationshipController {

    public static final int OFFSET_COUNT = 100;

    private static Connection conn;
    private User activeUser;
    private User visitingUser;

    private Stack<JFrame> visitedPages;

    public RelationshipController() {
        visitedPages = new Stack<>();
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

    /**
     * Helper for UI to update a user in the DB
     * @param user - The user object to update
     */
    public void updateUserObject(User user) {
        UserTable.updateUser(conn, user);
    }

    /**
     * Helper for UI to add a user to the DB
     * @param user - The user object to create
     */
    public void createUserObject(User user) {
        UserTable.addUser(conn, user);
    }

    /**
     * Helper for UI to get an ArrayList of Like objects representing who likes them
     * @param user - The user to query for likes
     * @return An ArrayList of Like objects
     */
    public ArrayList<Like> getLikes(User user) {
        return LikesTable.getLikesForUser(conn, user.getUsername());
    }

    /**
     * Helper for UI to get an ArrayList of Like objects representing the user's matches
     * @param user
     * @return
     */
    public ArrayList<Like> getMatches(User user) {
        return LikesTable.getMatchesForUser(conn, user.getUsername());
    }

    /**
     * Helper for UI to create a Like in the database
     * @param sender - The sender of the Like
     * @param receiver - The receiver of the Like
     */
    public void createLike(User sender, User receiver) {
        LikesTable.createLike(conn, sender.getUsername(), receiver.getUsername());
    }

    /**
     * Helper for UI to get an ArrayList of URLs representing the photos of a User
     * @param user - The user to get the associated photos of
     * @return An ArrayList of strings representing the URLs of the photos
     */
    public ArrayList<String> getUserPhotos(User user) {
        return UserPhotosTable.getUserPhotos(conn, user);
    }

    /**
     * Helper for UI to delete a user's photo
     * @param user - The corresponding user who's photo will be deleted
     * @param photoURL - The URL of the photo to delete
     */
    public void deleteUserPhoto(User user, String photoURL) {
        UserPhotosTable.deleteUserPhoto(conn, user, photoURL);
    }

    /**
     * Helper for UI to get an ArrayList of Interest objects for a certain user
     * @param user - The user to get the interests of
     * @return - An ArrayList of Interest objects
     */
    public ArrayList<Interest> getUserInterests(User user) {
        return UserInterestsTable.getUserInterests(conn, user.getUsername());
    }

    /**
     * Helper for UI to get an ArrayList of Interests. These are ALL of the Interests in the DB
     * @return An ArrayList of every Interest in the DB
     */
    public ArrayList<Interest> getInterests() {
        return InterestTable.getInterests(conn);
    }

    /**
     * Helper for UI to remove an Interest from a User
     * @param user - The user for which the interest wil be removed
     * @param interest - The interest to remove
     */
    public void removeInterestFromUser(User user, Interest interest) {
        UserInterestsTable.deleteUserInterest(conn, user, interest);
    }

    /**
     * Method called by UI to add a new Interest to a user in the DB
     * @param user - The user who will have the interest
     * @param interest - The interest to add
     */
    public void submitInterest(User user, Interest interest) {
        InterestCategoriesTable.addCategoryWithCheck(conn, interest.getCategory());
        InterestTable.createInterestWithCheck(conn, interest);
        UserInterestsTable.addInterestToUserWithCheck(conn, user.getUsername(), interest);
        back();
    }

    /**
     * Method called by UI to add a photo to a user in the DB
     * @param user - The user to add a photo to
     * @param photo - URL of the photo to add
     */
    public void submitPhoto(User user, String photo) {
        UserPhotosTable.addPhotoToUserWithCheck(conn, user.getUsername(), photo);
        back();
    }

    /**
     * Helper for UI to get an ArrayList of Visit objects
     * @param currentUser - The user to get the visits for
     * @param offset - Will offset the list of visits to include a subset of visits. Used for pagination
     * @return An ArrayList of Visit objects
     */
    public ArrayList<Visit> getVisitsForUser(User currentUser, int offset) {
        return VisitTable.getVisitsForUser(conn, currentUser, offset);
    }

    /**
     * Helper for UI to create a visit between two users
     * @param visited - The visited user
     * @param visitor - The visiting user
     */
    public void createVisit(User visited, User visitor) {
        VisitTable.createVisit(conn, visited.getUsername(), visitor.getUsername());
        visitingUser = visited;
        JFrame nextPage = VisitingUserView.init(this);
        addPageToVistedPages(nextPage);
    }

    /**
     * Helper for UI to get the number of Visits in the DB
     * @param timeDelta - The amount of time to check for visits ("Hour", "Day", "Week", "Month", "Year")
     * @return The number of visits
     */
    public int getVisitCount(String timeDelta){
        return RelationalRelationshipsStats.getVisitCount(conn,timeDelta);
    }

    /**
     * Helper for UI to get the number of Likes in the DB
     * @param timeDelta - The amount of time to check for likes ("Hour", "Day", "Week", "Month", "Year")
     * @return The number of likes
     */
    public int getLikeCount(String timeDelta){
        return RelationalRelationshipsStats.getLikeCount(conn, timeDelta);
    }

    /**
     * Helper for UI to get the number of matches in the DB
     * @param timeDelta - The amount of time to check for matches ("Hour", "Day", "Week", "Month", "Year")
     * @return The number of matches
     */
    public int getMatchCount(String timeDelta){
        return RelationalRelationshipsStats.getMatchCount(conn, timeDelta);
    }

    //UI methods

    /**
     * Instantiates the UI by initializing the LoginView
     */
    public void startUI() {
        addPageToVistedPages(LoginView.init(this));
    }

    /**
     * UI Method to check to see if a login is valid. If it is, it will login the user based on username and password
     * @param username - The username to attempt a login with
     * @param password - The password associated with the username
     */
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

    /**
     * Logout the user and reopen the LoginView
     */
    public void logout(){
        activeUser = null;
        visitedPages.forEach(JFrame::dispose);
        visitedPages.clear();
        addPageToVistedPages(LoginView.init(this));
    }

    /**
     * Initializes and visits the administration page
     */
    public void openAdminPage() {
        JFrame nextPage = AdminView.init(this);
        addPageToVistedPages(nextPage);
    }

    /**
     * Initializes and visits the visited page
     */
    public void openVisitPage() {
        JFrame nextPage = VisitedView.init(this);
        addPageToVistedPages(nextPage);
    }

    /**
     * Initializes and visits the liked page
     */
    public void openLikedPage() {
        JFrame page = LikesView.init(this);
        addPageToVistedPages(page);
    }

    /**
     * Initializes and visits the preferences page
     */
    public void openPreferencesPage() {
        JFrame page = PreferencesView.init(this);
        addPageToVistedPages(page);
    }

    /**
     * Initializes and visits the interest submission page
     */
    public void openInterestSubmissionPage() {
        JFrame nextPage = CreateInterestView.init(this, visitedPages.peek());
        visitedPages.push(nextPage);
    }

    /**
     * Initializes and visits the interest creation page
     */
    public void openInterestCreationPage() {
        JFrame nextPage = AddInterestView.init(this, visitedPages.peek());
        visitedPages.push(nextPage);
    }

    /**
     * Initializes and visits the photo submission page
     */
    public void openPhotoSubmissionPage() {
        JFrame nextPage = AddPhotoView.init(this, visitedPages.peek());
        visitedPages.push(nextPage);
    }

    /**
     * Helper for UI to register a user and add them to the DB.
     * Will bring the user back to the login page when completed
     *
     * @param user - The user object to register
     */
    public void register(User user) {
        UserTable.addUser(conn, user);
        this.back();
    }

    /**
     * Adds a JFrame to the ArrayList of visited pages (used for back functionality)
     * @param nextPage - The JFrame to add
     */
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

    /**
     * Initializes the error view with the appropriate error
     * @param error - The error to display in the view
     */
    public void createErrorView(String error) {
        JFrame nextPage = ErrorView.init(this, visitedPages.peek(), error);
        visitedPages.push(nextPage);
    }

    /**
     * Disposes of the current page and sets the next page on the stack to be visible
     */
    public void back() {
        visitedPages.peek().dispose();
        visitedPages.pop();
        visitedPages.peek().setVisible(false);
        visitedPages.peek().setVisible(true);
    }

    /**
     * Reinitializes the search view
     * @param controller - A reference to the controller object
     */
    public void restartSearchView(RelationshipController controller) {
        visitedPages.clear();
        addPageToVistedPages(SearchView.init(controller));
    }

    /**
     * Returns an ActionListener used for back functionality
     * @param controller - A reference to the controller object
     * @return An ActionListener for back buttons
     */
    public ActionListener backListener(RelationshipController controller) {
        return e -> controller.back();
    }

    /**
     * Helper for UI to perform a search
     * @param zipCode - ZipCode to search in
     * @param offset - Offset used for pagination
     * @return An ArrayList of User objects representing the results of the search
     */
    public ArrayList<User> search(String zipCode, int offset) {
        return UserTable.search(conn, zipCode, offset, activeUser);
    }


    /**
     * The entry point of the program.
     * Takes in a list of command line arguments for ease of use
     *
     * @param args - Command line arguments. Can be found in the readme
     * @throws SQLException
     */
    public static void main(String args[]) throws SQLException {
        RelationalRelationships relationalRelationships = new RelationalRelationships();

        String username = "";
        String password = "";
        Boolean autoLogin = false;

        int numUsersToGenerate = 0;
        int numVisitsToGenerate = 0;
        Boolean generateContent = false;
        //Check Arguments
        relationalRelationships.createConnection();

        for (int i=0; i < args.length; i++){
            if (args[i].equals("-n")){
                relationalRelationships.createPopulatedTables();
            }
            if (args[i].equals("-l")){
                autoLogin = true;
                username = args[i+1];
                password = args[i+2];
            }
            if (args[i].equals("-g")){
                generateContent = true;
                numUsersToGenerate = Integer.parseInt(args[i+1]);
                numVisitsToGenerate = Integer.parseInt(args[i+2]);
            }
        }

        conn = relationalRelationships.getConnection();

        //Check if it's a first time run (Does USERS table exist?)
        ResultSet tableResults = SQLHelper.executeQuery(conn, "SELECT count(TABLE_NAME) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME='USER';");
        if (tableResults.next()) {
            if (tableResults.getInt("count(TABLE_NAME)") != 1) {
                relationalRelationships.createPopulatedTables();
            }
        }

        if (generateContent){
            UserGen userGen = new UserGen();
            ArrayList<User> users = new ArrayList<User>();

            // Add in the users that were created from CSV parsing
            users.addAll(UserTable.getAllUserObjects(conn));

            // Generate new users and add them to users array
            users.addAll(userGen.generateUsers(numUsersToGenerate));

            InterestGen interestGen = new InterestGen();
            interestGen.generateUserInterests(users);
            VisitsGen visitsGen = new VisitsGen();
            visitsGen.generateVisits(numVisitsToGenerate);
            LikeGen likeGen = new LikeGen();
            likeGen.generateLikes(users);
        }

        //UI
        RelationshipController controllerInstance = new RelationshipController();
        if (autoLogin){
            controllerInstance.login(username, password);
        }
        else{
            controllerInstance.startUI();
        }
    }
}
