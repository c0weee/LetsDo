package com.example.ca1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class PopupItemArrayAdapter extends RecyclerView.Adapter<PopupItemArrayAdapter.MyViewHolder> {
    private final ArrayList<SubTaskitem> subTasks;
    private final PopupItemArrayAdapter.MyRecyclerViewItemClickListener mItemClickListener;

    public PopupItemArrayAdapter(ArrayList<SubTaskitem> subTasks, PopupItemArrayAdapter.MyRecyclerViewItemClickListener itemClickListener) {
        this.subTasks = subTasks;
        this.mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public PopupItemArrayAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate RecyclerView row
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popup_taskitem, parent, false);

        //Create View Holder
        final PopupItemArrayAdapter.MyViewHolder myViewHolder = new PopupItemArrayAdapter.MyViewHolder(view);

        //Item Clicks
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClicked(subTasks.get(myViewHolder.getLayoutPosition()));
            }
        });

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PopupItemArrayAdapter.MyViewHolder holder, int position) {
        //Set Data
        holder.subtask.setText(subTasks.get(position).getName());
        holder.subtask.setChecked(subTasks.get(position).getCompleted());
    }

    @Override
    public int getItemCount() {
        return subTasks.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //RecyclerView View Holder
    class MyViewHolder extends RecyclerView.ViewHolder {
        private final CheckBox subtask;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            subtask = itemView.findViewById(R.id.popup_taskitem);
        }
    }

    //RecyclerView Click Listener
    public interface MyRecyclerViewItemClickListener {
        void onItemClicked(SubTaskitem item);
    }

}
