package com.example.folium_nav.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.example.folium_nav.Adapters.AddressAdapter;
import com.example.folium_nav.Domain.Address;
import com.example.folium_nav.Interfaces.AddressUpdateListener;
import com.example.folium_nav.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AddressFragment extends Fragment implements AddressUpdateListener {
    private RecyclerView addressRecyclerView;
    private AddressAdapter addressAdapter;
    private List<Address> addressList;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private Button paymentBtn, addAddressBtn;

    @Override
    public void onAddressUpdated() {
        loadAddresses();
    }

    public AddressFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

        getParentFragmentManager().setFragmentResultListener("addressUpdated", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                // Reload the addresses when a new address is added
                loadAddresses();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_address, container, false);

        //Set the detail page title
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Addresses");

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        addressRecyclerView = view.findViewById(R.id.addressRecycler);
        addressList = new ArrayList<>();
        addressAdapter = new AddressAdapter(requireContext(), addressList);
        addressRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        addressRecyclerView.setAdapter(addressAdapter);

        paymentBtn = view.findViewById(R.id.paymentBtn);
        addAddressBtn = view.findViewById(R.id.addAddressBtn);

        loadAddresses();


        addAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Replace the current fragment with the AddAddressFragment
                Fragment addAddressFragment = new AddAddressFragment();
                // Replace the current fragment with AddAddressFragment
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.add_list_page, addAddressFragment)
                        .commit();
            }
        });

        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "This would lead to a Payment Processor", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void loadAddresses() {
        db.collection("User").document(auth.getCurrentUser().getUid())
                .collection("Address").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            addressList.clear();
                            for(DocumentSnapshot doc:task.getResult()){
                                Address address = doc.toObject(Address.class);
                                addressList.add(address);
                            }
                            addressAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

}