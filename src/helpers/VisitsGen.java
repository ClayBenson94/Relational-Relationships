package helpers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.Date;

import objects.RelationshipController;

public class VisitsGen {

    public void genereateVisits(int count) {
        String selectUserQuery = "SELECT username FROM user";
        String insertVisitQuery = "INSERT INTO visit(visited, visitor, timestamp) VALUES %s;";
        Connection conn = RelationshipController.getConnection();
        ResultSet resultSet = SQLHelper.executeQuery(conn, selectUserQuery);
        ArrayList<String> usernames = new ArrayList<>();

        try {
            while (resultSet.next()) {
                usernames.add(resultSet.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Random random = new Random();
        int first;
        int second;
        int usernameLength = usernames.size();
        ArrayList<String> visitsToInsert = new ArrayList<>();

        Date referenceDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(referenceDate);
        c.add(Calendar.MONTH, -3);
        c.getTime();

        for (int i = 0; i < count; i++) {
            first = random.nextInt(usernameLength);
            second = random.nextInt(usernameLength);

            long timestamp = RandomNumberHelper.randBetween(c.getTime().getTime(), System.currentTimeMillis());
            visitsToInsert.add("(\'" + usernames.get(first) + "\',\'" + usernames.get(second) + "\'," + timestamp + ")");
        }
        insertVisitQuery = String.format(insertVisitQuery, String.join(",", visitsToInsert));
        SQLHelper.execute(conn, insertVisitQuery);
    }
}
