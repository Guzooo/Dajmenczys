package pl.Guzooo.Dajmenczys;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import pl.Guzooo.Dajmenczys.Objects.Record;

public class AdapterRecords extends RecyclerView.Adapter<AdapterRecords.ViewHolder> {
    private Listener listener;
    private Cursor cursor;

    public interface Listener{
        void onClick(int id);
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private View mainView;
        private TextView title;

        public ViewHolder(View v){
            super(v);
            mainView = v;
            title = v.findViewById(R.id.title);
        }

        private Context getContext(){
            return mainView.getContext();
        }

        private void setTitle(Record record){
            String text = record.getDate();
            title.setText(text);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_records, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(cursor.moveToPosition(position)){
            Record record = getRecord();
            holder.setTitle(record);
            setOnClickThisView(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public AdapterRecords(Cursor cursor){
        this.cursor = cursor;
    }

    public void changeData(Cursor cursor){
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    private Record getRecord(){
        Record record = new Record();
        record.setVariablesOfCursor(cursor);
        return record;
    }

    private void setOnClickThisView(ViewHolder holder, final int position){
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null && cursor.moveToPosition(position))
                    listener.onClick(cursor.getInt(0));
            }
        });
    }
}
