package helpers;

import helpers.SQLHelper;
import objects.RelationshipController;
import objects.User;
import tables.LikesTable;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Random;

public class LikeGen {

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
        c.getTime();
        
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
