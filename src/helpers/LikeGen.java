package helpers;

import objects.Like;
import objects.RelationshipController;
import objects.User;
import tables.LikesTable;
import tables.UserTable;

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
        HashMap<User,ArrayList<User>> likeMap = new HashMap<>();
        int receiverIndex;
        long timestamp;

       for (User user: users){

            ArrayList<User> potentialLikes = UserTable.search(conn,Integer.toString(user.getLocation()),0,user);
            ArrayList<User> alreadyLikes = LikesTable.getWhoUserLikes(conn,user.getUsername());

            potentialLikes.removeAll(alreadyLikes);

            for (int j=RandomNumberHelper.randBetween(0,Math.min(10, potentialLikes.size()-1)); j > 0; j--) {
                
                receiverIndex = RandomNumberHelper.randBetween(0,potentialLikes.size()-1);
                User receivingUser = potentialLikes.get(receiverIndex);

                timestamp = DateHelper.getRandomTimeStamp();

                // Add the like to the array list
                likesToInsert.add("(\'" + user.getUsername() + "\',\'" + receivingUser.getUsername() +
                        "\'," + timestamp + ")");

                if (likeMap.get(user) != null) {
                    likeMap.get(user).add(receivingUser);
                }else{
                    ArrayList<User> templist = new ArrayList<>();
                    templist.add(receivingUser);
                    likeMap.put(user,templist);
                }

                // Add a visit to the array list, because a like is not possible without a visit. The visit will
                // be within the last five minutes of the like
                visitsToInsert.add("(\'" + user.getUsername() + "\',\'" + receivingUser.getUsername() + "\'," +
                        (timestamp - RandomNumberHelper.randBetween(0,300000)) + ")");

                potentialLikes.remove(receivingUser);

                boolean contains = false;

                if (likeMap.get(receivingUser) !=null){
                    contains = likeMap.get(receivingUser).contains(user);
                } else{
                    likeMap.put(receivingUser, new ArrayList<>());
                }

                if (!contains){
                    //like user back
                    if(RandomNumberHelper.randBetween(0, 1) == 1){
                        timestamp = DateHelper.getRandomTimeStamp();
                        likesToInsert.add("(\'" + receivingUser.getUsername() + "\',\'" +
                                user.getUsername() + "\'," + timestamp + ")");

                        likeMap.get(receivingUser).add(user);

                        // Add a visit to the array list, because a like is not possible without a visit. The visit will
                        // be within the last five minutes of the like
                        visitsToInsert.add("(\'" + receivingUser.getUsername() + "\',\'" +
                                user.getUsername() + "\'," +
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
