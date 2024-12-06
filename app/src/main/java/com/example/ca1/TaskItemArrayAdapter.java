package com.example.ca1;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class TaskItemArrayAdapter extends RecyclerView.Adapter<TaskItemArrayAdapter.MyViewHolder> {
    private final ArrayList<Taskitem> mTasks;
    private final MyRecyclerViewItemClickListener mItemClickListener;

    public TaskItemArrayAdapter(ArrayList<Taskitem> tasks, MyRecyclerViewItemClickListener itemClickListener) {
        this.mTasks = tasks;
        this.mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate RecyclerView row
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        //Create View Holder
        final MyViewHolder myViewHolder = new MyViewHolder(view);
        //Item Clicks
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClicked(mTasks.get(myViewHolder.getLayoutPosition()));
            }
        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //Set Tasks Name
        holder.textViewName.setText(mTasks.get(position).getName());
        //Set Date
        holder.textViewDate.setText(mTasks.get(position).getStartDateTime());

        //Set Image and color
        holder.imageViewImage.setImageResource(R.drawable.a);
        holder.imageViewImage.setColorFilter(Color.parseColor(mTasks.get(position).getColor()));
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
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
        private final TextView textViewName;
        private final TextView textViewDate;
        private final ImageView imageViewImage;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.taskName);
            textViewDate = itemView.findViewById(R.id.taskDate);
            imageViewImage = itemView.findViewById(R.id.taskIcon);
        }
    }

    public interface MyRecyclerViewItemClickListener {
        void onItemClicked(Taskitem task);
    }


}
