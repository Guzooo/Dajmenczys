package pl.Guzooo.Dajmenczys;

import android.content.Context;
import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import pl.Guzooo.Dajmenczys.Objects.Dimen;

public class AdapterData extends RecyclerView.Adapter<AdapterData.ViewHolder> {
    private Listener listener;
    private Cursor cursor;

    public interface Listener{
        void endEdit(int position, EditText editText);
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private View mainView;
        private TextView title;
        private EditText value;

        public ViewHolder(View v){
            super(v);
            mainView = v;
            title = v.findViewById(R.id.title);
            value = v.findViewById(R.id.edit_text);
        }

        private Context getContext(){
            return mainView.getContext();
        }

        private void setTitle(Dimen dimen){
            String text = dimen.getName();
            title.setText(text);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_dates, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       if(cursor.moveToPosition(position)){
           Dimen dimen = getDimen();
           holder.setTitle(dimen);
           setOnEditText(holder, position);
       }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public AdapterData(Cursor cursor){
        this.cursor = cursor;
    }

    public void changeData(Cursor cursor){
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    private Dimen getDimen(){
        Dimen dimen = new Dimen();
        dimen.setVariablesOfCursor(cursor);
        return dimen;
    }

    private void setOnEditText(ViewHolder holder, int position){
        holder.value.addTextChangedListener(getTextListener(holder, position));
    }

    private TextWatcher getTextListener(final ViewHolder holder, final int position){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(listener != null)
                    listener.endEdit(position, holder.value);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }
}
