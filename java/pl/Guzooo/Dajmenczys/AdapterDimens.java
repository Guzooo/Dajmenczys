package pl.Guzooo.Dajmenczys;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import pl.Guzooo.Dajmenczys.Objects.Dimen;

public class AdapterDimens extends RecyclerView.Adapter<AdapterDimens.ViewHolder> {
    private Listener listener;
    private Cursor cursor;

    public interface Listener{
        void onClickDelete(int id);
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private View mainView;
        private TextView title;
        private ImageView del;

        public ViewHolder(View v){
            super(v);
            mainView = v;
            title = v.findViewById(R.id.title);
            del = v.findViewById(R.id.del);
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_dimens, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(cursor.moveToPosition(position)){
            Dimen dimen = getDimen();
            holder.setTitle(dimen);
            setOnClickThisView(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public AdapterDimens(Cursor cursor){
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

    private void setOnClickThisView(ViewHolder holder, final int position){
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null && cursor.moveToPosition(position))
                    listener.onClickDelete(cursor.getInt(0));
            }
        });
    }
}
