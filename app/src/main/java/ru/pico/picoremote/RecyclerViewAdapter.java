package ru.pico.picoremote;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private List<DevicesModel> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    RecyclerViewAdapter(Context context, List<DevicesModel> data, ItemClickListener clickListener) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mClickListener = clickListener;
    }


    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        String animal = "Pico VR: #"+mData.get(position).getId();
        holder.myTextView.setText(animal);

        if(mData.get(position).getState()==0){
            holder.textView5.setText("Статус: Проигрывается видео");
            holder.textView5.setTextColor(Color.parseColor("#FF4CAF50"));
        }
        if(mData.get(position).getState()==1){
            holder.textView5.setText("Статус: В покое");
            holder.textView5.setTextColor(Color.parseColor("#FFA69609"));
        }
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.delete(v, mData.get(holder.getAdapterPosition() ).getId());
            }
        });
        holder.exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.exit(v, mData.get(holder.getAdapterPosition() ).getId());
            }
        });
        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.play(v, mData.get(holder.getAdapterPosition() ).getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView myTextView, textView5;
        ImageView play, exit, delete;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.textView4);
            textView5 = itemView.findViewById(R.id.textView5);
            play = itemView.findViewById(R.id.play);
            exit = itemView.findViewById(R.id.exit);
            delete = itemView.findViewById(R.id.delete);
        }


    }


    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void play(View view, int position);
        void exit(View view, int position);
        void delete(View view, int position);
    }

}
