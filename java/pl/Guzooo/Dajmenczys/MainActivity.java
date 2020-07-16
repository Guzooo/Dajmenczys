package pl.Guzooo.Dajmenczys;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
                Record.DATE);
    }

    private void setAdapter(){
        adapter = new AdapterRecords(cursor);
        adapter.setListener(getAdapterListener());
    }

    private AdapterRecords.Listener getAdapterListener(){
        return new AdapterRecords.Listener() {
            @Override
            public void onClick(int id) {
                //todo:POKAŻ TE WYMIARY W JAKIMS DIALOGU
            }
        };
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

            }
        };
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
