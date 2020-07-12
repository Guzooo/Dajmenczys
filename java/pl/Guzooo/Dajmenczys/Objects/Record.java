package pl.Guzooo.Dajmenczys.Objects;

import android.content.ContentValues;
import android.database.Cursor;

import pl.Guzooo.DatabaseObject;

public class Record extends DatabaseObject {
    public static final String DATE = "DATE";

    public static final String DATABASE_NAME = "RECORD";
    public static final String[] ON_CURSOR = new String[]{
            ID,
            DATE
    };

    private final String SPLITTER = "/";

    private String date;

    @Override
    public String[] onCursor() {
        return ON_CURSOR;
    }

    @Override
    public String databaseName() {
        return DATABASE_NAME;
    }

    /*@Override
    public void setExtendedVariablesOfCursor(Cursor cursor) {

    }*/

    @Override
    public void setVariablesOfCursor(Cursor cursor) {
        template(cursor.getInt(0),
                cursor.getString(1));
    }

    @Override
    public void setVariablesEmpty() {
        template(0,
                "0"/*TODO: dzisiejsza date*/);
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATE, date);
        return contentValues;
    }

    public String getDate(){
        int day = getDay();
        int month = getMonth();//TODO: bardziej atraktcyjne wyswietlanie
        return day + "."
                + month + "."
                + getYear() + " "
                + getHour() + ":"
                + getMinute();
    }

    public int getYear(){
        String year = splittedDate()[0];
        return Integer.valueOf(year);
    }

    public int getMonth(){
        String month = splittedDate()[1];
        return Integer.valueOf(month);
    }

    public int getDay(){
        String day = splittedDate()[2];
        return Integer.valueOf(day);
    }

    public int getHour(){
        String hour = splittedDate()[3];
        return Integer.valueOf(hour);
    }

    public int getMinute(){
        String minute = splittedDate()[4];
        return Integer.valueOf(minute);
    }

    public void setDate(int year, int month, int day, int hour, int minute){
        date = year + SPLITTER
                + month + SPLITTER
                + day + SPLITTER
                + hour + SPLITTER
                + minute;
    }

    private void template (int id,
                           String date){
        setId(id);
        setDate(date);
    }

    private void setDate(String date){
        this.date = date;
    }

    private String[] splittedDate(){
        return date.split(SPLITTER);
    }
}
