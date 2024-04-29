package com.example.folium_nav.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.folium_nav.Domain.Address;
import com.example.folium_nav.Interfaces.AddressUpdateListener;
import com.example.folium_nav.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class EditAddressFragment extends Fragment {
    private AddressUpdateListener mListener;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private Object obj;
    TextInputEditText name, address, city, postCode, phone;
    Button editButton, cancelButton, deleteButton;

    public EditAddressFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_address, container, false);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        editButton = view.findViewById(R.id.updateAddressBtn);
        deleteButton = view.findViewById(R.id.deleteAddBtn);
        cancelButton = view.findViewById(R.id.cancelUpdateBtn);

        //Get textinput field
        name = view.findViewById(R.id.editNameInput);
        address = view.findViewById(R.id.editAddInput);
        city = view.findViewById(R.id.editCityInput);
        postCode = view.findViewById(R.id.editPostCodeInput);
        phone = view.findViewById(R.id.editPhoneInput);

        // Retrieve the data passed from the bundle
        Bundle bundle = getArguments();
        obj = getArguments().getSerializable("addressItem");
        Address selectedAddress = (Address) obj;

        //Set the text into the textinupts
        name.setText(selectedAddress.getName());
        address.setText(selectedAddress.getStreet());
        city.setText(selectedAddress.getCity());
        postCode.setText(selectedAddress.getPostcode());
        phone.setText(selectedAddress.getPhone());
        //Cancel the address update
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });

        //submit the edited address information
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get updated data from the TextInput fields
                String nameNew = name.getText().toString().trim();
                String addressNew = address.getText().toString().trim();
                String cityNew = city.getText().toString().trim();
                String postCodeNew = postCode.getText().toString().trim();
                String phoneNew = phone.getText().toString().trim();

                // Do not proceed if any field is empty
                if (nameNew.isEmpty() || addressNew.isEmpty() || cityNew.isEmpty() || postCodeNew.isEmpty() || phoneNew.isEmpty()) {
                    Toast.makeText(getContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Get the complete old address, used to get the correct documentID
                String completeOldAddress = selectedAddress.getCompleteAddress();

                // Get the collection reference
                CollectionReference addressCollectionRef = db.collection("User").document(auth.getCurrentUser().getUid()).collection("Address");

                // Query for the document ID of the address to be updated
                addressCollectionRef.whereEqualTo("completeAddress", completeOldAddress)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        // Get the document ID
                                        String docId = document.getId();
                                        // Update the address document
                                        updateAddress(docId, nameNew, addressNew, cityNew, postCodeNew, phoneNew);
                                        notifyAddressUpdated();
                                    }
                                } else {
                                    Log.d("Edit Address", "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the complete old address, used to get the correct documentID
                String completeOldAddress = selectedAddress.getCompleteAddress();

                // Get the collection reference
                CollectionReference addressCollectionRef = db.collection("User").document(auth.getCurrentUser().getUid()).collection("Address");

                // Query for the document ID of the address to be deleted
                addressCollectionRef.whereEqualTo("completeAddress", completeOldAddress)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        // Get the document ID
                                        String docId = document.getId();
                                        // Delete the address document
                                        deleteAddress(docId);

                                        notifyAddressUpdated();
                                    }
                                } else {
                                    Log.d("Edit Address", "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    private void notifyAddressUpdated() {
        if (mListener != null) {
            mListener.onAddressUpdated();
        }
    }

    // Method to update an address document in Firestore
    private void updateAddress(String docId, String nameNew, String addressNew, String cityNew, String postCodeNew, String phoneNew) {
        //Map the new data
        Map<String, Object> updateData = new HashMap<>();
        String completeNewAddress = nameNew + ", " + addressNew + ", " + cityNew + ", " + postCodeNew + ", " + phoneNew;
        updateData.put("completeAddress", completeNewAddress);
        updateData.put("name", nameNew);
        updateData.put("street", addressNew);
        updateData.put("city", cityNew);
        updateData.put("postcode", postCodeNew);
        updateData.put("phone", phoneNew);
        DocumentReference docRef = db.collection("User").document(auth.getCurrentUser().getUid())
                .collection("Address").document(docId);

        docRef.update(updateData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Update successful
                        Toast.makeText(getContext(), "Address updated successfully", Toast.LENGTH_SHORT).show();
                        //tell parent fragment there is newly updated data
                        getParentFragmentManager().setFragmentResult("addressUpdated", new Bundle());
                        //close out this fragment and navigate back to parent
                        getParentFragmentManager().beginTransaction()
                                .remove(EditAddressFragment.this)
                                .commit();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                        Toast.makeText(getContext(), "Failed to update address", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // delete specific record
    private void deleteAddress(String docId) {
        DocumentReference docRef = db.collection("User").document(auth.getCurrentUser().getUid())
                .collection("Address").document(docId);

        docRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Deletion successful
                        Toast.makeText(getContext(), "Address deleted successfully", Toast.LENGTH_SHORT).show();
                        // Tell parent fragment there is newly updated data
                        getParentFragmentManager().setFragmentResult("addressUpdated", new Bundle());
                        // Close out this fragment and navigate back to parent
                        getParentFragmentManager().beginTransaction()
                                .remove(EditAddressFragment.this)
                                .commit();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                        Toast.makeText(getContext(), "Failed to delete address", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}