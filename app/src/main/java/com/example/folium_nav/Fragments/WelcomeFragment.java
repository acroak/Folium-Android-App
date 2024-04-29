package com.example.folium_nav.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.folium_nav.R;

public class WelcomeFragment extends Fragment {
    private Button goToLogin;
    private TextView goToSignUp;


    public WelcomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_welcome, container, false);

        goToLogin = view.findViewById(R.id.loginBtn);
        goToSignUp = view.findViewById(R.id.signUpBtn);

        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Load the LoginFragment
                LoginFragment loginFragment = new LoginFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.welcome_page, loginFragment);
                // allows user to navigate back to previous fragment
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        goToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Load the LoginFragment
                RegisterFragment registerFragment = new RegisterFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.welcome_page, registerFragment);
                // allows user to navigate back to previous fragment
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}