package pl.Guzooo.Dajmenczys;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import pl.Guzooo.Dajmenczys.Objects.Data;
import pl.Guzooo.Dajmenczys.Objects.Dimen;
import pl.Guzooo.Dajmenczys.Objects.Record;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private Cursor cursor;
    private AdapterRecords adapter;

    private RecyclerView recycler;
    private FloatingActionButton addFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialization();
        setDatabaseElements();
        setAddFAB();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        refreshDatabaseElements();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDatabaseElements();
    }

    private void initialization() {
        recycler = findViewById(R.id.recycler);
        addFAB = findViewById(R.id.fab_add);
    }

    private void setDatabaseElements(){
        try {
            db = Database.getToWriting(this);
            setCursor();
            setAdapter();
            setRecycler();
        } catch (SQLiteException e){
            Database.errorToast(this);
        }
    }

    private void setAddFAB(){
        addFAB.setOnClickListener(getAddFABClickListener());
    }

    private void refreshDatabaseElements(){
        try {
            cursor.close();
            setCursor();
            adapter.changeData(cursor);
        } catch (SQLiteException e){
            Database.errorToast(this);
        }
    }

    private void closeDatabaseElements(){
        cursor.close();
        db.close();
    }

    private void setCursor(){
        cursor = db.query(Record.TABLE_NAME,
                Record.ON_CURSOR,
                null, null, null, null,
                Record.DATE + " DESC");
    }

    private void setAdapter(){
        adapter = new AdapterRecords(cursor);
        adapter.setListener(getAdapterListener());
    }

    private AdapterRecords.Listener getAdapterListener(){
        return new AdapterRecords.Listener() {
            @Override
            public void onClick(int id) {
                showRecordAlertDialog(id);
            }
        };
    }

    private void showRecordAlertDialog(int id){
        new AlertDialog.Builder(this)
                .setView(getRecordInfoLayout(id))
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private View getRecordInfoLayout(int id){
        View layout = LayoutInflater.from(this).inflate(R.layout.view_records, null);
        TextView textView = layout.findViewById(R.id.title);
        Cursor cursor = getRecordInfoCursor(id);
        String text = "";
        if(cursor.moveToFirst())
            do {
                text += cursor.getString(1) + ": " + cursor.getInt(0) + "\n";
            } while (cursor.moveToNext());
        textView.setText(text);
        return layout;
    }

    private Cursor getRecordInfoCursor(int id){
        return db.rawQuery("SELECT " + Data.VALUE + ", " + Dimen.NAME
                + " FROM " + Data.TABLE_NAME
                + " LEFT JOIN " + Dimen.TABLE_NAME + " ON " + Dimen.TABLE_NAME + "." + Dimen.ID + " = " + Data.ID_DIMEN
                + " WHERE " + Data.ID_RECORD + " = " + id, null);
    }

    private void setRecycler(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(adapter);
    }

    private View.OnClickListener getAddFABClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenuAlertDialog();
            }
        };
    }

    private void showMenuAlertDialog(){
         new AlertDialog.Builder(this)
                 .setTitle("MENU")
                 .setView(getMenuLayout())
                 .setNegativeButton(android.R.string.cancel, null)
                 .show();
    }

    private View getMenuLayout(){
        View layout = LayoutInflater.from(this).inflate(R.layout.add_menu, null);
        layout.findViewById(R.id.add_record).setOnClickListener(getAddRecordClickListener());
        layout.findViewById(R.id.manage_dimen).setOnClickListener(getManageDimenClickListener());
        layout.findViewById(R.id.add_dimen).setOnClickListener(getAddDimenClickListener());
        return layout;
    }

    private View.OnClickListener getAddRecordClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddRecordClickListener();
            }
        };
    }

    private void showAddRecordClickListener(){
        new AlertDialog.Builder(this)
                .setTitle("DODAJ REKORD")
                .setView(getAddRecordLayout())
                .setPositiveButton(android.R.string.ok, getSaveRecordListener())
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    public Record addRecord = new Record();
    public ArrayList<Data> addDatas = new ArrayList<>();

    private View getAddRecordLayout(){
        View layout = LayoutInflater.from(this).inflate(R.layout.view_record, null);
        RecyclerView recycler = layout.findViewById(R.id.recycler);
        TextView title = layout.findViewById(R.id.title);
        title.setText(addRecord.getDate());
        title.setOnLongClickListener(getAddRecordChangeDataListener(layout.getContext()));
        Cursor cursor = getAddRecordCursor();
        setAddDatas(cursor);
        AdapterData adapter = getAddRecordAdapter(cursor);
        setAddRecordRecycler(adapter, recycler);
        return layout;
    }

    private View.OnLongClickListener getAddRecordChangeDataListener(final Context context){
        return new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                final TextView text = (TextView) v;
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        addRecord.setDate(hourOfDay, minute);
                        text.setText(addRecord.getDate());
                    }
                };
                DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        addRecord.setDate(year, month+1, dayOfMonth);
                        text.setText(addRecord.getDate());
                    }
                };
                new TimePickerDialog(context, onTimeSetListener, addRecord.getHour(), addRecord.getMinute(), true).show();
                new DatePickerDialog(context, onDateSetListener, addRecord.getYear(), addRecord.getMonth()-1, addRecord.getDay()).show();
                Log.d("G LOG", addRecord.getDate() + "  " + addRecord.getYear());
                return true;
            }
        };
    }

    private Cursor getAddRecordCursor(){
        return db.query(Dimen.TABLE_NAME,
        Dimen.ON_CURSOR,
                null, null, null, null,
                Dimen.NAME);
    }

    private void setAddDatas(Cursor cursor){
        if(cursor.moveToFirst())
            do{
                Dimen dimen = new Dimen();
                dimen.setVariablesOfCursor(cursor);
                Data data = new Data();
                data.setIdDimen(dimen.getId());
                addDatas.add(data);
            } while (cursor.moveToNext());
        }

    private AdapterData getAddRecordAdapter(Cursor cursor){
        AdapterData adapter = new AdapterData(cursor);
        adapter.setListener(getAddRecordAdapterListener());
        return adapter;
    }

    private AdapterData.Listener getAddRecordAdapterListener(){
        return new AdapterData.Listener() {
            @Override
            public void endEdit(int position, EditText editText) {
                String value = editText.getText().toString().trim();
                addDatas.get(position).setValue(value);
            }
        };
    }

    private void setAddRecordRecycler(AdapterData adapter, RecyclerView recycler){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(adapter);
    }

    private DialogInterface.OnClickListener getSaveRecordListener(){
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addRecord.insert(getApplicationContext());
                int recordId = getRecordID();
                for(Data data : addDatas){
                    data.setIdRecord(recordId);
                    data.insert(getApplicationContext());
                }
            }
        };
    }

    private int getRecordID(){
        int lastRecordId = 0;
        Cursor cursor = db.query(Record.TABLE_NAME,
                Record.ON_CURSOR,
                null, null, null, null,null);
        if(cursor.moveToLast())
            lastRecordId = cursor.getInt(0);
        return lastRecordId;
    }

    private View.OnClickListener getManageDimenClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllDimenAlertDialog();
            }
        };
    }

    private void showAllDimenAlertDialog(){
        new AlertDialog.Builder(this)
                .setTitle("LISTA WYMIARÓW")
                .setView(getAllDimenLayout())
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private View getAllDimenLayout(){
        View layout = LayoutInflater.from(this).inflate(R.layout.recycler_view, null);
        RecyclerView recycler = layout.findViewById(R.id.recycler);
        Cursor cursor = getAllDimenCursor();
        AdapterDimens adapter = getAllDimenAdapter(cursor);
        setAllDimenRecycler(adapter, recycler);
        return layout;
    }

    private Cursor getAllDimenCursor(){
        return db.query(Dimen.TABLE_NAME,
                Dimen.ON_CURSOR,
                null, null, null, null,
                Dimen.NAME);
    }

    private AdapterDimens getAllDimenAdapter(Cursor cursor){
        AdapterDimens adapter = new AdapterDimens(cursor);
        adapter.setListener(getAllDimenAdapterListener());
        return adapter;
    }

    private AdapterDimens.Listener getAllDimenAdapterListener(){
        return new AdapterDimens.Listener() {
            @Override
            public void onClickDelete(int id) {
                showDelDimenDialogAlert(id);
            }
        };
    }

    private void showDelDimenDialogAlert(int id){
        new AlertDialog.Builder(this)
                .setTitle("CHCESZ USUNĄĆ TEN WYMIAR?")
                .setPositiveButton(android.R.string.yes, getDelDimenPositiveListener(id))
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private DialogInterface.OnClickListener getDelDimenPositiveListener(final int id){
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Dimen dimen = new Dimen();
                dimen.setVariablesOfId(id, getApplicationContext());
                dimen.delete(getApplicationContext());
            }
        };
    }

    private void setAllDimenRecycler(AdapterDimens adapter, RecyclerView recycler){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(adapter);
    }

    private View.OnClickListener getAddDimenClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDimenAlertDialog();
            }
        };
    }

    private void showAddDimenAlertDialog(){
        View layout = getAddDimenLayout();
        new AlertDialog.Builder(this)
                .setTitle("DODAJ NOWY WYMIAR")
                .setView(layout)
                .setPositiveButton(android.R.string.ok, getSaveDimenListener(layout))
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private View getAddDimenLayout(){
        View layout = LayoutInflater.from(this).inflate(R.layout.add_dimen, null);
        return layout;
    }

    private DialogInterface.OnClickListener getSaveDimenListener(final View layout){
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText editText = layout.findViewById(R.id.edit_text);
                String name = editText.getText().toString().trim();
                Dimen dimen = new Dimen();
                dimen.setName(name);
                dimen.insert(getApplicationContext());
            }
        };
    }
}
