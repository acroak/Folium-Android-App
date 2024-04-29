package com.example.folium_nav.Fragments;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.folium_nav.Adapters.ItemsRecyclerAdapter;
import com.example.folium_nav.Domain.Items;
import com.example.folium_nav.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ItemsFragment extends Fragment {
    private String TAG = "ItemsFragment";
    private FirebaseFirestore db;
    List<Items> mItemsList;
    private RecyclerView itemRecyclerView;
    private ItemsRecyclerAdapter itemsRecyclerAdapter;
    private static final String KEY_ITEMS_LIST = "items_list";
    public ItemsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_items, container, false);
        String type = getArguments().getString("type");
        String title = getArguments().getString("title");
        db =FirebaseFirestore.getInstance();
        mItemsList=new ArrayList<>();

        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        if (toolbar != null) {
            ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(title);

        }
        itemRecyclerView=view.findViewById(R.id.items_recycler);
        // Determine the span count based on the screen orientation
        int spanCount = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 2;
        // Set layout manager and adapter for RecyclerView
        itemRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), spanCount));
        itemsRecyclerAdapter=new ItemsRecyclerAdapter(getContext(),mItemsList);
        itemRecyclerView.setAdapter(itemsRecyclerAdapter);

        if (type != null) {
            if (type.equalsIgnoreCase("foliage")) {
                    db.collection("All").whereEqualTo("type","foliage").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                updateItemsAdapter(task);
                            }
                        }
                    });
            } else if (type.equalsIgnoreCase("flowering")) {

                    db.collection("All").whereEqualTo("type","flowering").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                updateItemsAdapter(task);
                            }
                        }
                    });

            } else if (type.equalsIgnoreCase("succulents")) {

                    db.collection("All").whereEqualTo("type","succulent").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                updateItemsAdapter(task);
                            }
                        }
                    });

            } else if (type.equalsIgnoreCase("cacti")) {

                    db.collection("All").whereEqualTo("type","cacti").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                updateItemsAdapter(task);
                            }
                        }
                    });

            } else if (type.equalsIgnoreCase("Air-Purifing")) {
                    db.collection("All").whereEqualTo("type","airpurifying").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                updateItemsAdapter(task);
                            }
                        }
                    });

            } else if (type.equalsIgnoreCase("popular")) {
                    db.collection("Featured").whereEqualTo("type","popular").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                updateItemsAdapter(task);
                            }
                        }
                    });

            } else if (type.equalsIgnoreCase("all plants")) {
                db.collection("All").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            updateItemsAdapter(task);
                        }
                    }
                });
            }

        } else if (type==null || type.isEmpty()) {
                db.collection("All").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            updateItemsAdapter(task);
                        }
                    }
                });
        }


        // Inflate the layout for this fragment
        return view;
    }

    public void updateItemsAdapter(Task<QuerySnapshot> task){
        for(DocumentSnapshot doc:task.getResult().getDocuments()){
            Log.i(TAG, "onComplete: "+doc.toString());
            Items items=doc.toObject(Items.class);
            mItemsList.add(items);
            Collections.sort(mItemsList, new Comparator<Items>() {
                @Override
                public int compare(Items o1, Items o2) {
                    return o1.getName().compareToIgnoreCase(o2.getName());
                }
            });
            itemsRecyclerAdapter.notifyDataSetChanged();
        }
    }
}