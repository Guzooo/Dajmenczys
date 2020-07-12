package pl.Guzooo.Dajmenczys;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import pl.Guzooo.Dajmenczys.Objects.Data;
import pl.Guzooo.Dajmenczys.Objects.Dimen;
import pl.Guzooo.Dajmenczys.Objects.Record;

public class Database extends SQLiteOpenHelper {

    private static final String DB_NAME = "dajmenczys";
    private static final int DB_VERSION = 1;

    private SQLiteDatabase db;

    Database(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        initialization(db);
        createTableRecord();
        createTableData();
        createTableDimen();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        initialization(db);
        /*if(oldVersion < 2)
            ; */
    }

    public static SQLiteDatabase getToWriting(Context context){
        SQLiteOpenHelper openHelper = new Database(context);
        return openHelper.getWritableDatabase();
    }

    public static SQLiteDatabase getToReading(Context context){
        SQLiteOpenHelper openHelper = new Database(context);
        return openHelper.getReadableDatabase();
    }

    public static void errorToast(Context context){
        Toast.makeText(context, R.string.error_database, Toast.LENGTH_SHORT).show();
    }

    private void initialization(SQLiteDatabase db){
        this.db = db;
    }

    private void createTableRecord(){
        db.execSQL("CREATE TABLE " + Record.TABLE_NAME + " ("
                + Record.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Record.DATE + " TEXT)");
    }

    private void createTableData(){
        db.execSQL("CREATE TABLE " + Data.TABLE_NAME + " ("
                + Data.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Data.ID_RECORD + " INTEGER,"
                + Data.ID_DIMEN + " INTEGER,"
                + Data.VALUE + " INTEGER)");
    }

    private void createTableDimen(){
        db.execSQL("CREATE TABLE " + Dimen.TABLE_NAME + " ("
                + Dimen.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Dimen.NAME + " TEXT)");
    }

    public static boolean delAllTable(String tableName, Context context){
        return delTable(tableName, null, null, context);
    }

    public static boolean delTable(String tableName, String whereClause, String[] whereArgs, Context context){
        try {
            SQLiteDatabase db = getToWriting(context);
            db.delete(tableName, whereClause, whereArgs);
            db.close();
            return true;
        } catch (SQLException e){
            errorToast(context);
            return false;
        }
    }
}