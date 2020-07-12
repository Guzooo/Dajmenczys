package pl.Guzooo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import pl.Guzooo.Dajmenczys.Database;

public abstract class DatabaseObject {
    public static final String ID = "_id";

    private int id;

    public abstract String[] onCursor();
    //public abstract String extendedQuery();
    public abstract String tableName();

    public DatabaseObject(){
        setVariablesEmpty();
    }

    //public abstract void setExtendedVariablesOfCursor(Cursor cursor);

    public abstract void setVariablesOfCursor(Cursor cursor);

    public void setVariablesOfId(int id, Context context){
        try {
            SQLiteDatabase db = Database.getToReading(context);
            Cursor cursor = db.query(tableName(),
                    onCursor(),
                    ID + " = ?",
                    new String[]{Integer.toString(id)},
                    null, null, null);

            if(cursor.moveToFirst())
                setVariablesOfCursor(cursor);
            else
                setVariablesEmpty();

            cursor.close();
            db.close();
        } catch (SQLiteException e){
            Database.errorToast(context);
        }
    }

    public abstract void setVariablesEmpty();

    public boolean insert(Context context){
        if(isWrongValue(context))
            return false;

        try {
            SQLiteDatabase db = Database.getToWriting(context);
            db.insert(tableName(), null, getContentValues());
            db.close();
            return true;
        } catch (SQLiteException e){
            Database.errorToast(context);
            return false;
        }
    }

    public boolean update(Context context){
        if(isWrongValue(context))
            return false;

        try {
            SQLiteDatabase db = Database.getToWriting(context);
            db.update(tableName(), getContentValues(), ID + " = ?", new String[]{Integer.toString(id)});
            db.close();
            return true;
        } catch (SQLiteException e){
            Database.errorToast(context);
            return false;
        }
    }

    public boolean delete(Context context){
        try {
            SQLiteDatabase db = Database.getToWriting(context);
            db.delete(tableName(), ID + " = ?", new String[]{Integer.toString(id)});
            db.close();
            return true;
        } catch (SQLException e){
            Database.errorToast(context);
            return false;
        }
    }

    public boolean isWrongValue(Context context){
        return false;
    }

    public abstract ContentValues getContentValues();

    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    }
}