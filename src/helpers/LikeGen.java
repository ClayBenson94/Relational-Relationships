package helpers;

import objects.RelationshipController;
import objects.User;

import java.sql.Connection;
import java.util.Date;
import java.util.Calendar;
import java.util.ArrayList;

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
        String likeQuery = "INSERT INTO likes VALUES %s;";
        Connection conn = RelationshipController.getConnection();
        ArrayList<String> likesToInsert = new ArrayList<>();
        int numUsers = users.size();
        int receiverIndex = 0;
        long timestamp = 0;
        
        Date referenceDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(referenceDate);
        cal.add(Calendar.MONTH, -3);
        cal.getTime();
        
        for (int i = 0; i < numUsers; i++){
            for (int j=RandomNumberHelper.randBetween(0,30); j > 0; j--) {
                
                receiverIndex = RandomNumberHelper.randBetween(0,numUsers-1);
                while(receiverIndex == i){
                    receiverIndex = RandomNumberHelper.randBetween(0,numUsers-1);
                }
                
                timestamp = RandomNumberHelper.randBetween(c.getTime().getTime(), System.currentTimeMillis());
                
                likesToInsert.add("(\'" + users.get(i).getUsername() + "\',\'" + 
                users.get(receiverIndex).getUsername() + "\'," + timestamp + ")");
                
                //like user back
                if(RandomNumberHelper.randBetween(0,1) == 1){
                    timestamp = RandomNumberHelper.randBetween(c.getTime().getTime(), System.currentTimeMillis());
                    likesToInsert.add("(\'" + users.get(receiverIndex).getUsername() + "\',\'" + 
                    users.get(i).getUsername() + "\'," + timestamp + ")");
                }
            }
        }
        
        likeQuery = String.format(likeQuery, String.join(",", likesToInsert));
        SQLHelper.execute(conn, likeQuery);
    }
}
