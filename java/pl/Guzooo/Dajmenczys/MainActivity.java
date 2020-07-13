package pl.Guzooo.Dajmenczys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
                //TODO:Alert dzialog;
                // dodaj wymiary;
                // dodaj nowy wymiar
            }
        };
    }
}
