package pl.Guzooo.Dajmenczys.Objects;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import pl.Guzooo.Dajmenczys.Database;
import pl.Guzooo.DatabaseObject;

public class Data extends DatabaseObject {
    public static final String ID_RECORD = "ID_RECORD";
    public static final String ID_DIMEN = "ID_DIMEN";
    public static final String VALUE = "VALUE";

    public static final String TABLE_NAME = "DATE";
    public static final String[] ON_CURSOR = new String[]{
            ID,
            ID_RECORD,
            ID_DIMEN,
            VALUE
    };

    private int idRecord;
    private int idDimen;
    private int value;

    @Override
    public String[] onCursor() {
        return ON_CURSOR;
    }

    @Override
    public String tableName() {
        return TABLE_NAME;
    }

    @Override
    public void setVariablesOfCursor(Cursor cursor) {
        template(cursor.getInt(0),
                cursor.getInt(1),
                cursor.getInt(2),
                cursor.getInt(3));
    }

    @Override
    public void setVariablesEmpty() {
        template(0, 0, 0, 0);
    }

    @Override
    public boolean isWrongValue(Context context) {
        if(idRecord == 0 || idDimen == 0){
            Database.errorToast(context);
            return true;
        }
        return false;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_RECORD, idRecord);
        contentValues.put(ID_DIMEN, idDimen);
        contentValues.put(VALUE, value);
        return contentValues;
    }

    public int getIdRecord() {
        return idRecord;
    }

    public void setIdRecord(int idRecord) {
        this.idRecord = idRecord;
    }

    public int getIdDimen() {
        return idDimen;
    }

    public void setIdDimen(int idDimen) {
        this.idDimen = idDimen;
    }

    public int getValue() {
        return value;
    }

    public void setValue(String value){
        int v = 0;
        if(!value.isEmpty())
            v = Integer.valueOf(value);
        setValue(v);
    }

    private void setValue(int value) {
        this.value = value;
    }

    private void template (int id,
                           int idRecord,
                           int idDimen,
                           int value){
        setId(id);
        setIdRecord(idRecord);
        setIdDimen(idDimen);
        setValue(value);
    }
}
