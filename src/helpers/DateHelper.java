package helpers;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Clay on 4/5/2016.
 */
public class DateHelper {
    //This will hold the method to help convert between datestrings(in CSV and DB) and SQLDate objects (used when constructing User objects)

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



}