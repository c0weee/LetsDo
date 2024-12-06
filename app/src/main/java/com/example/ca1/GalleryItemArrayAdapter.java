package com.example.ca1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class GalleryItemArrayAdapter extends RecyclerView.Adapter<GalleryItemArrayAdapter.MyViewHolder> {
    private final ArrayList<GalleryItem> galleryitem;
    private final MyRecyclerViewItemClickListener mItemClickListener;

    public GalleryItemArrayAdapter(ArrayList<GalleryItem> galleryitem, MyRecyclerViewItemClickListener itemClickListener) {
        this.galleryitem = galleryitem;
        this.mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate RecyclerView row
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item, parent, false);

        //Create View Holder
        final MyViewHolder myViewHolder = new MyViewHolder(view);

        //Item Clicks
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClicked(galleryitem.get(myViewHolder.getLayoutPosition()));
            }
        });

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //Set Image
        holder.image.setImageResource(galleryitem.get(position).getImgName());
        holder.label.setText(galleryitem.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return galleryitem.size();
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
        private final ImageView image;
        private final TextView label;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.galimg);
            label = itemView.findViewById(R.id.label);
        }
    }

    //RecyclerView Click Listener
    public interface MyRecyclerViewItemClickListener {
        void onItemClicked(GalleryItem image);
    }

}
