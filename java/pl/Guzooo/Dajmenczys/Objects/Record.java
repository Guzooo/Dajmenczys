package pl.Guzooo.Dajmenczys.Objects;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import pl.Guzooo.CalendarUtils;
import pl.Guzooo.Dajmenczys.Database;
import pl.Guzooo.DatabaseObject;

public class Record extends DatabaseObject {
    public static final String DATE = "DATE";

    public static final String TABLE_NAME = "RECORD";
    public static final String[] ON_CURSOR = new String[]{
            ID,
            DATE
    };

    private String date;

    @Override
    public String[] onCursor() {
        return ON_CURSOR;
    }

    @Override
    public String tableName() {
        return TABLE_NAME;
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
    public boolean delete(Context context) {
        String[] whereArgs = new String[] {Integer.toString(getId())};
        boolean isDataDelete = Database.delTable(Data.TABLE_NAME,
                                                Data.ID_RECORD + " = ?",
                                                whereArgs,
                                                context);
        if(!isDataDelete)
            return false;
        return super.delete(context);
    }

    @Override
    public void setVariablesEmpty() {
        template(0,
                CalendarUtils.getTodayWithTimeToWrite());
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATE, date);
        return contentValues;
    }

    public String getDate(){
        return CalendarUtils.getDateToRead(date);
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
        return Integer.valueOf(minute)-1;
    }

    public void setDate(int year, int month, int day){
        setDate(year, month, day, getHour(), getMinute());
    }

    public void  setDate(int hour, int minute){
        setDate(getYear(), getMonth(), getDay(), hour, minute);
    }

    private void setDate(int year, int month, int day, int hour, int minute){
        date = year + CalendarUtils.DATA_SPLITTER
                + month + CalendarUtils.DATA_SPLITTER
                + day + CalendarUtils.DATA_SPLITTER
                + hour + CalendarUtils.DATA_SPLITTER
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
        return date.split(CalendarUtils.DATA_SPLITTER);
    }
}
