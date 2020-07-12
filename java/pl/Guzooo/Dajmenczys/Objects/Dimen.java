package pl.Guzooo.Dajmenczys.Objects;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import pl.Guzooo.Dajmenczys.R;
import pl.Guzooo.DatabaseObject;

public class Dimen extends DatabaseObject {
    public static final String NAME = "NAME";

    public static final String DATABASE_NAME = "DIMEN";
    public static final String[] ON_CURSOR = new String[]{
            ID,
            NAME
    };

    private String name;

    @Override
    public String[] onCursor() {
        return ON_CURSOR;
    }

    @Override
    public String databaseName() {
        return DATABASE_NAME;
    }

    @Override
    public void setVariablesOfCursor(Cursor cursor) {
        template(cursor.getInt(0),
                cursor.getString(1));
    }

    @Override
    public void setVariablesEmpty() {
        template(0, "");
    }

    @Override
    public boolean isWrongValue(Context context) {
        if(name.isEmpty()){
            Toast.makeText(context, R.string.error_empty_dimen_name, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        return contentValues;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private void template (int id,
                           String name){
        setId(id);
        setName(name);
    }
}
