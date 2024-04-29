package com.example.folium_nav.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.folium_nav.Adapters.CartAdapter;
import com.example.folium_nav.Domain.Cart;
import com.example.folium_nav.Interfaces.CartAdapterListener;
import com.example.folium_nav.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment implements CartAdapterListener {
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private List<Cart> cartList;
    private TextView subtotalTextView, taxTextView, shippingTextView, totalTextView;
    private Button addAddressBtn;
    private ConstraintLayout cartIsEmptyLayout, cartContainerLayout;
    private ScrollView cartContainerSV;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        cartRecyclerView = view.findViewById(R.id.cartView);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        cartList = new ArrayList<>();
        cartAdapter = new CartAdapter(requireContext(), cartList, cartRecyclerView, this);
        cartRecyclerView.setAdapter(cartAdapter);

        subtotalTextView = view.findViewById(R.id.subtotal);
        taxTextView = view.findViewById(R.id.tax);
        shippingTextView = view.findViewById(R.id.shipping);
        totalTextView = view.findViewById(R.id.total);

        addAddressBtn = view.findViewById(R.id.addAddressBtn);

        cartIsEmptyLayout = view.findViewById(R.id.cart_is_empty);
        cartContainerLayout = view.findViewById(R.id.cart_container);
        cartContainerSV = view.findViewById(R.id.cart_container_SV);

        //Set the detail page title
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Cart");

        db.collection("User").document(auth.getCurrentUser().getUid())
                .collection("Cart").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            cartList.clear();
                            double subtotal = 0;
                            for(DocumentSnapshot doc:task.getResult()){
                                Cart cartItem = doc.toObject(Cart.class);
                                cartList.add(cartItem);
                                subtotal += Double.parseDouble(cartItem.getTotalPrice());
                                cartAdapter.notifyDataSetChanged();
                            }

                            double shippingCost = 15.0;
                            double tax = subtotal * 0.07;
                            double total = subtotal + shippingCost + tax;

                            subtotalTextView.setText(String.format("$%.2f", subtotal));
                            taxTextView.setText(String.format("$%.2f", tax));
                            shippingTextView.setText(String.format("$%.2f", shippingCost));
                            totalTextView.setText(String.format("$%.2f", total));

                            isCartEmpty();
                        }
                    }
                });

        addAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment addressFragment = new AddressFragment();

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.cart_page, addressFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    public void updateTotals() {
        double subtotal = 0;
        for (Cart cartItem : cartList) {
            subtotal += Double.parseDouble(cartItem.getTotalPrice());
        }

        double shippingCost = 15.0;
        double tax = subtotal * 0.07;
        double total = subtotal + shippingCost + tax;

        subtotalTextView.setText(String.format("$%.2f", subtotal));
        taxTextView.setText(String.format("$%.2f", tax));
        shippingTextView.setText(String.format("$%.2f", shippingCost));
        totalTextView.setText(String.format("$%.2f", total));

        isCartEmpty();
    }

    private void isCartEmpty() {
        // Check if cart list is empty or null
        if (cartList == null || cartList.isEmpty()) {
            cartIsEmptyLayout.setVisibility(View.VISIBLE);
            cartContainerLayout.setVisibility(View.GONE);
        } else {
            cartIsEmptyLayout.setVisibility(View.GONE);
            cartContainerLayout.setVisibility(View.VISIBLE);
        }
    }

}