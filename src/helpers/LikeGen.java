package helpers;

import helpers.SQLHelper;
import objects.RelationshipController;
import objects.User;
import tables.LikesTable;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

public class LikeGen {

    public void generateLikes(ArrayList<User> users){
        int receiverIndex = 0;
        
        for (int i = 0; i < users.size(); i++){
            receiverIndex = RandomNumberHelper.randBetween(0,users.size());
            while(receiverIndex != i){
                receiverIndex = RandomNumberHelper.randBetween(0,users.size());
            }
            for (int j=RandomNumberHelper.randBetween(0,30); j > 0; j--) {
                String query = "INSERT INTO likes VALUES (\'"
                + users.get(i).getUsername() + "\',\'"
                + users.get(receiverIndex).getUsername() +"\',\'" 
                + System.currentTimeMillis() + "\');";
                Statement stmt = null;
                try {
                    stmt = RelationshipController.getConnection().createStatement();
                    stmt.executeUpdate(query);
                } catch (SQLException e) {
                    if (e.getSQLState().equals("23505")){
                        // user already has that interest, do nothing
                    } else {
                    e.printStackTrace();
                }
                }
            }
        }
    }

}
