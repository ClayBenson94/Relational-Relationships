package helpers;

import objects.RelationshipController;
import objects.User;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * LikeGen objects generate likes between users. 
 */ 
public class LikeGen {

    /**
     * Generates likes for a list of users. Users who have been
     * liked will like the other user back 50% of the time.
     * 
     * @param users  the list of users to create likes for
     */ 
    public void generateLikes(ArrayList<User> users){
        String insertVisitQuery = "INSERT INTO visit(visited, visitor, timestamp) VALUES %s;";
        String likeQuery = "INSERT INTO likes VALUES %s;";
        Connection conn = RelationshipController.getConnection();
        ArrayList<String> likesToInsert = new ArrayList<>();
        ArrayList<String> visitsToInsert = new ArrayList<>();
        HashMap<Integer,ArrayList<Integer>> likeMap = new HashMap<>();
        int numUsers = users.size();
        int receiverIndex;
        long timestamp;

        for (int i = 0; i < numUsers; i++){

            for (int j=RandomNumberHelper.randBetween(0,30); j > 0; j--) {
                
                receiverIndex = RandomNumberHelper.randBetween(0,numUsers-1);

                // This while loop prevents liking yourself
                while(receiverIndex == i){
                    receiverIndex = RandomNumberHelper.randBetween(0,numUsers-1);
                }

                ArrayList<Integer>  currentUsersLikes;

                // If the user has already liked other users
                if (likeMap.get(i) != null) {

                    // Keep the user from liking the same user again
                    while (likeMap.get(i).contains(receiverIndex)) {
                        receiverIndex = RandomNumberHelper.randBetween(0, numUsers - 1);
                    }

                    currentUsersLikes = likeMap.get(i);

                } else{
                   currentUsersLikes = new ArrayList<>();
                }

                // Add the like to the likeMap Arraylist for this user
                currentUsersLikes.add(receiverIndex);
                likeMap.put(i, currentUsersLikes);

                timestamp = DateHelper.getRandomTimeStamp();

                // Add the like to the array list
                likesToInsert.add("(\'" + users.get(i).getUsername() + "\',\'" + 
                users.get(receiverIndex).getUsername() + "\'," + timestamp + ")");

                // Add a visit to the array list, because a like is not possible without a visit. The visit will
                // be within the last five minutes of the like
                visitsToInsert.add("(\'" + users.get(i).getUsername() + "\',\'" +
                        users.get(receiverIndex).getUsername() + "\'," +
                        (timestamp - RandomNumberHelper.randBetween(0,300000)) + ")");

                // Get the liked user's likes array list or create a new one for that user
                ArrayList<Integer>  gettingLikedUsersLikes;
                if (likeMap.get(receiverIndex) != null){
                    gettingLikedUsersLikes = likeMap.get(receiverIndex);
                }
                else{
                    gettingLikedUsersLikes = new ArrayList<>();
                }

                // If the liked user does not already like the user that is liking them
                if (!gettingLikedUsersLikes.contains(i)){
                    //like user back
                    if(RandomNumberHelper.randBetween(0, 1) == 1){
                        timestamp = DateHelper.getRandomTimeStamp();
                        likesToInsert.add("(\'" + users.get(receiverIndex).getUsername() + "\',\'" +
                                users.get(i).getUsername() + "\'," + timestamp + ")");
                        gettingLikedUsersLikes.add(i);
                        likeMap.put(receiverIndex, gettingLikedUsersLikes);

                        // Add a visit to the array list, because a like is not possible without a visit. The visit will
                        // be within the last five minutes of the like
                        visitsToInsert.add("(\'" + users.get(receiverIndex).getUsername() + "\',\'" +
                                users.get(i).getUsername() + "\'," +
                                (timestamp - RandomNumberHelper.randBetween(0,300000)) + ")");
                    }
                }
            }
        }

        // Add the likes to the db
        likeQuery = String.format(likeQuery, String.join(",", likesToInsert));
        SQLHelper.execute(conn, likeQuery);

        // Add the visits to the db
        insertVisitQuery = String.format(insertVisitQuery, String.join(",", visitsToInsert));
        SQLHelper.execute(conn, insertVisitQuery);
    }
}
