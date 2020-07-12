package pl.Guzooo.Dajmenczys;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    public static final String ID = "_id";

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

    private void initialization(SQLiteDatabase db){
        this.db = db;
    }

    private void createTableRecord(){

    }

    private void createTableData(){

    }

    private void createTableDimen(){

    }
}
