package helpers;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * A Class made to help other class's with anything date related
 */
public class DateHelper {

    /**
     * This method converts between datestrings(in CSV and DB) and SQLDate objects (used when constructing User objects)
     * @param dateFormat - The format the date string is in
     * @param dateString - A string representing the date
     * @return an SQL Date
     */
    public static Date dateStringToSQLDate(String dateFormat, String dateString) {
        DateFormat format = new SimpleDateFormat(dateFormat, Locale.US);
        java.util.Date date = null;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date sqlDate = null;
        if (date != null) {
            sqlDate = new Date(date.getTime());
        }
        return sqlDate;
    }

    /**
     * Get a random timestamp between 2016-03-01 (when the website went live we are saying) and now
     * @return A Long timestamp
     */
    public static Long getRandomTimeStamp(){
        Long beginTime = Timestamp.valueOf("2016-03-01 00:00:00").getTime();
        Long endTime = System.currentTimeMillis();
        long diff = endTime - beginTime + 1;
        return beginTime + (long) (Math.random() * diff);
    }

}
