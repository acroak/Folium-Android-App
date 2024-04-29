package com.example.folium_nav.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.folium_nav.MainActivity;
import com.example.folium_nav.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment {
    private TextView haveAnAcct;

    private EditText nameRegET, passwordRegET, emailRegET;

    private Button regBtn;

    private FirebaseAuth regAuth;

    private FirebaseFirestore db;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        //Information Input fields
        nameRegET = view.findViewById(R.id.nameRegET);
        passwordRegET = view.findViewById(R.id.passwordRegET);
        emailRegET = view.findViewById(R.id.emailRegET);

        regBtn = view.findViewById(R.id.logBtn);

        regAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Handle the "Already have an Acct? Log In" switch
        haveAnAcct = view.findViewById(R.id.haveAnAcctBtn);

        haveAnAcct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Replace the current fragment with the LoginFragment
                Fragment loginFragment = new LoginFragment();
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.register_page, loginFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameRegET.getText().toString();
                String email = emailRegET.getText().toString();
                String password = passwordRegET.getText().toString();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    regAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Account Created", Toast.LENGTH_SHORT).show();
                                // Move the profile creation logic inside this block
                                Map<String, Object> profile = new HashMap<>();
                                profile.put("name", name);

                                db.collection("User")
                                        .document(regAuth.getCurrentUser().getUid())
                                        .collection("Profile")
                                        .add(profile)
                                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d("Registration Frag: ", "DocumentSnapshot added with ID: " + task.getResult().getId());

                                                    // Redirect to MainActivity after profile creation
                                                    Intent intent = new Intent(getContext(), MainActivity.class);
                                                    startActivity(intent);
                                                } else {
                                                    Log.w("Registration Frag: ", "Error adding document", task.getException());
                                                    Toast.makeText(getContext(), "Error adding to profile collection", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    Toast.makeText(getContext(), "An account with this email already exists", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "" + task.getException(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }
            }
        });




        // Inflate the layout for this fragment
        return view;
    }
}