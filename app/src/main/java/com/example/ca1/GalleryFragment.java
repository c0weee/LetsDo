package com.example.ca1;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class GalleryFragment extends Fragment {

    private RecyclerView recyclerView;
    private final ArrayList<GalleryItem> galleryItems = new ArrayList<>();
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.label_gallery);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gallery, container, false);
        getReward();
        return view;
    }

    private void setUIRef() {
        recyclerView = view.findViewById(R.id.galleryview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        GalleryItemArrayAdapter myRecyclerViewAdapter = new GalleryItemArrayAdapter(galleryItems, new GalleryItemArrayAdapter.MyRecyclerViewItemClickListener() {
            //Handling clicks
            @Override
            public void onItemClicked(GalleryItem image) {
                Toast.makeText(view.getContext(), image.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        //Set adapter to RecyclerView
        recyclerView.setAdapter(myRecyclerViewAdapter);
    }

    private void bindImgData() {
        galleryItems.add(new GalleryItem("Wahale", R.drawable.whale));
        galleryItems.add(new GalleryItem("Dark", R.drawable.dark));
        galleryItems.add(new GalleryItem("Beee", R.drawable.beee));
        galleryItems.add(new GalleryItem("Not Dumbo", R.drawable.dumbo));
    }


    public void getReward() {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("rewards/"+userID);
        // get tasks belonging to user
//        Query q = myRef.orderByChild("userID").equalTo(userID);

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                galleryItems.clear();

                HashMap<String, String> t = new HashMap<String, String>();
                t = (HashMap<String, String>) dataSnapshot.getValue();

                Log.d("Dark says", "Value is: " + t.toString());

                for (String i : t.values()) {
                    Log.d("Dark says", "Value is: " + i);


                        int resID = getResources().getIdentifier(i , "drawable", view.getContext().getPackageName());
                        galleryItems.add(new GalleryItem(i.substring(0, 1).toUpperCase() + i.substring(1), resID));
//                        galleryItems.add(new GalleryItem("Wahale", R.drawable.whale));
//                        galleryItems.add(new GalleryItem("Dark", R.drawable.dark));
//                        galleryItems.add(new GalleryItem("Beee", R.drawable.beee));
//                        galleryItems.add(new GalleryItem("Not Dumbo", R.drawable.dumbo));


                }

                //display gallery item
                setUIRef();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Dark says", "Failed to read value.", error.toException());
            }
        });
    }
}