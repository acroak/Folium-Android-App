package com.example.folium_nav.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.folium_nav.Interfaces.AddressUpdateListener;
import com.example.folium_nav.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class AddAddressFragment extends Fragment {
    private AddressUpdateListener mListener;
    private TextInputEditText addName, addAddress, addCity, addPostCode, addPhone;
    private Button addressBtn;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private ActivityResultLauncher<Intent> addressResultLauncher;

    public AddAddressFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_address, container, false);

        addName = view.findViewById(R.id.addName);
        addAddress = view.findViewById(R.id.addAddress);
        addCity = view.findViewById(R.id.addCity);
        addPostCode = view.findViewById(R.id.addPostalCode);
        addPhone = view.findViewById(R.id.addPhone);

        addressBtn = view.findViewById(R.id.addAddressButton);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        addressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAddress();
            }
        });



        // Inflate the layout for this fragment
        return view;
    }

    public void addAddress(){
        String name = addName.getText().toString().trim();
        String address = addAddress.getText().toString().trim();
        String city = addCity.getText().toString().trim();
        String postCode = addPostCode.getText().toString().trim();
        String phone = addPhone.getText().toString().trim();

        if (name.isEmpty() || address.isEmpty() || city.isEmpty() || postCode.isEmpty() || phone.isEmpty()) {
            Toast.makeText(getContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return; // Do not proceed if any field is empty
        }

        String completeAddress = name + ", " + address + ", " + city + ", " + postCode + ", " + phone;

        Map<String, String> map = new HashMap<>();
        map.put("completeAddress", completeAddress);
        map.put("name", name);
        map.put("street", address);
        map.put("city", city);
        map.put("postcode", postCode);
        map.put("phone", phone);

        db.collection("User").document(auth.getCurrentUser().getUid())
                .collection("Address").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            //tell parent fragment there is newly updated data
                            getParentFragmentManager().setFragmentResult("addressUpdated", new Bundle());
                            //close out this fragment and navigate back to parent
                            getParentFragmentManager().beginTransaction()
                                    .remove(AddAddressFragment.this)
                                    .commit();
                        } else {
                            Toast.makeText(getContext(), "Error adding address", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}