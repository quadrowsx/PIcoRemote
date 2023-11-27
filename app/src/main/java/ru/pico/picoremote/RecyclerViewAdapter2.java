package ru.pico.picoremote;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter2 extends RecyclerView.Adapter<RecyclerViewAdapter2.ViewHolder>{

    private List<DevicesModel> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    RecyclerViewAdapter2(Context context, List<DevicesModel> data, ItemClickListener clickListener) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mClickListener = clickListener;
    }


    @NonNull
    @Override
    public RecyclerViewAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.view_item2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter2.ViewHolder holder, int position) {
        String animal = "Pico VR: #"+mData.get(position).getId();
        holder.name.setText(animal);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.radioButton.isChecked()){
                    holder.radioButton.setChecked(false);
                }
                else{
                    holder.radioButton.setChecked(true);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        RadioButton radioButton;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textView4);
            radioButton = itemView.findViewById(R.id.radio);
        }


    }


    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void checkPos(View view, int position, int id);
    }

}
