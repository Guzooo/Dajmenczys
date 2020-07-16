package pl.Guzooo;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CalendarUtils {
    public static final String DATA_SPLITTER = "/";

    public static String getTodayWithTimeToWrite(){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy" + DATA_SPLITTER
                + "MM" + DATA_SPLITTER
                + "dd" + DATA_SPLITTER
                + "HH" + DATA_SPLITTER
                + "mm");
        return formatter.format(date);
    }

    public static String getDateToRead(String dateWrite){
        String[] dateElements = dateWrite.split(DATA_SPLITTER);
        String dateForReading = dateElements[2] + " ";
        dateForReading += getMonthToRead(dateElements[1]) + " ";
        dateForReading += dateElements[0] + " ";
        dateForReading += dateElements[3] + ":";
        dateForReading += dateElements[4];
        return dateForReading;
    }

    private static String getMonthToRead(String monthWriter){
        DateFormatSymbols symbols = new DateFormatSymbols();
        int monthNumber = Integer.valueOf(monthWriter) -1;
        return symbols.getMonths()[monthNumber];
    }
}
