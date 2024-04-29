package com.example.folium_nav.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.folium_nav.Adapters.CategoryAdapter;
import com.example.folium_nav.Adapters.FeaturedAdapter;
import com.example.folium_nav.Domain.Category;
import com.example.folium_nav.Domain.Items;
import com.example.folium_nav.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeFragment extends Fragment {

    private FirebaseFirestore db;
    private String TAG = "HomeFragment";

    private Button learnMoreBtn;

    //Category Selection
    private List<Category> categoryList;
    private CategoryAdapter categoryAdapter;
    private RecyclerView categoryRecycler;

    //Featured/Popular Selection
    private FeaturedAdapter featuredAdapter;
    private RecyclerView featuredRecycler;
    private List<Items> itemsList;

    private TextView seeAllPop;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // connect to the database
        db = FirebaseFirestore.getInstance();

        //Banner
        learnMoreBtn = view.findViewById(R.id.learnMoreBtn);

        categoryRecycler = view.findViewById(R.id.categoryRecycler);
        featuredRecycler = view.findViewById(R.id.featuredRecycler);

        //Category View
        categoryList = new ArrayList<>();
        //Send the list of categories to the adapter to create individual category instances/cards
        categoryAdapter = new CategoryAdapter(getContext(), categoryList);
        //inflate the category views
        categoryRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        categoryRecycler.setAdapter(categoryAdapter);

        //Featured View
        itemsList = new ArrayList<>();
        featuredAdapter = new FeaturedAdapter(getContext(), itemsList);
        featuredRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        featuredRecycler.setAdapter(featuredAdapter);
        seeAllPop = view.findViewById(R.id.seeAllPop);

        //get category collection
        db.collection("Category")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //make data into java object
                                Category category = document.toObject(Category.class);
                                //add each category instance to the list
                                categoryList.add(category);

                                // Sort the categoryList alphabetically
                                Collections.sort(categoryList, new Comparator<Category>() {
                                    @Override
                                    public int compare(Category o1, Category o2) {
                                        return o1.getType().compareToIgnoreCase(o2.getType());
                                    }
                                });
                                categoryAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.w(TAG, "Error getting Category Collection", task.getException());
                        }
                    }
                });

        //get popular typed plants
        db.collection("Featured").whereEqualTo("type","popular").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc:task.getResult().getDocuments()){
                        Log.i(TAG, "onComplete: "+doc.toString());
                        Items items=doc.toObject(Items.class);
                        itemsList.add(items);
                        Collections.sort(itemsList, new Comparator<Items>() {
                            @Override
                            public int compare(Items o1, Items o2) {
                                return o1.getName().compareToIgnoreCase(o2.getName());
                            }
                        });
                        featuredAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        learnMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LightSensorFragment lightFragment = new LightSensorFragment();
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, lightFragment)
                        .commit();

            }

        });

        seeAllPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("type", "popular");
                ItemsFragment itemsFragment = new ItemsFragment();
                itemsFragment.setArguments(bundle);

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_page, itemsFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }
}