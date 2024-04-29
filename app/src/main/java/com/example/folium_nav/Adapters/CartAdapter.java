package com.example.folium_nav.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.folium_nav.Domain.Cart;
import com.example.folium_nav.Interfaces.CartAdapterListener;
import com.example.folium_nav.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private final FirebaseFirestore db;
    private final FirebaseAuth auth;
    private final Context context;
    private final List<Cart> cartList;
    private final RecyclerView recyclerView;
    private final CartAdapterListener listener;

    public CartAdapter(Context context, List<Cart> cartList, RecyclerView recyclerView, CartAdapterListener listener) {
        this.context = context;
        this.cartList = cartList;
        this.recyclerView = recyclerView;
        this.db = FirebaseFirestore.getInstance();
        this.auth = FirebaseAuth.getInstance();
        this.listener = listener;
        setupItemTouchHelper();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_cart_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cart cartItem = cartList.get(position);

        holder.titleTxt.setText(cartItem.getName());

        double price = Double.parseDouble(cartItem.getPrice());
        holder.feeEachItem.setText(String.format("$%.2f", price));

        holder.itemQuantity.setText(String.valueOf(cartItem.getQuantity()));

        double totalPrice = Double.parseDouble(cartItem.getTotalPrice());
        holder.totalEachItem.setText(String.format("$%.2f",totalPrice));

        String imgUrl = cartItem.getImg_url();
        Glide.with(context).load(imgUrl.isEmpty() ? R.drawable.placeholder : imgUrl)
                .centerCrop()
                .into(holder.imgView);

        holder.plusCartBtn.setOnClickListener(view -> updateQuantity(position, true));
        holder.minusCartBtn.setOnClickListener(view -> updateQuantity(position, false));
    }

    private void setupItemTouchHelper() {
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int swipedPosition = viewHolder.getAdapterPosition();
                Cart swipedItem = cartList.get(swipedPosition);
                cartList.remove(swipedPosition);
                notifyItemRemoved(swipedPosition);
                getDelItem(swipedItem);
            }
        };

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
    }

    private void updateQuantity(int position, boolean isIncrement) {
        Cart cartItem = cartList.get(position);
        int quantity = Integer.parseInt(cartItem.getQuantity());
        if (isIncrement) {
            quantity++;
        } else {
            if (quantity > 0) {
                quantity--;
            }
        }

        if (quantity == 0) {
            // Remove item from cartList
            cartList.remove(position);
            // Update totals in CartFragment
            listener.updateTotals();
            notifyItemRemoved(position);
            getDelItem(cartItem);
        } else {
            // Update quantity of the item
            cartItem.setQuantity(String.valueOf(quantity));
            double price = Double.parseDouble(cartItem.getPrice());
            double totalPrice = price * quantity;
            cartItem.setTotalPrice(String.valueOf(totalPrice));
            getUpdateItem(cartItem);
            notifyItemChanged(position);
            // Update totals in CartFragment
            listener.updateTotals();
        }
    }


    private void getUpdateItem(Cart swipedItem){
        String plantID = swipedItem.getP_id();
        CollectionReference addressCollectionRef = db.collection("User").document(Objects.requireNonNull(auth.getCurrentUser()).getUid()).collection("Cart");

       addressCollectionRef.whereEqualTo("p_id", plantID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String docId = document.getId();
                            getCartUpdate(docId, swipedItem);
                        }
                    } else {
                        Log.d("Edit Address", "Error getting documents: ", task.getException());
                    }
                });
    }

    private void getCartUpdate(String docId, Cart cartItem) {
        db.collection("User").document(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .collection("Cart").document(docId)
                .update("quantity", cartItem.getQuantity(), "totalPrice", cartItem.getTotalPrice())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.v("Cart update", "Cart update complete");
                    } else {
                        Log.v("Cart update", "Cart Update Error");
                    }
                });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgView;
        TextView titleTxt, feeEachItem, totalEachItem, itemQuantity, plusCartBtn, minusCartBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgView = itemView.findViewById(R.id.cartPic);
            titleTxt = itemView.findViewById(R.id.checkoutCartTitle);
            feeEachItem = itemView.findViewById(R.id.feeEachItem);
            totalEachItem = itemView.findViewById(R.id.totalEachItem);
            itemQuantity = itemView.findViewById(R.id.itemQuantity);
            plusCartBtn = itemView.findViewById(R.id.plusCartBtn);
            minusCartBtn = itemView.findViewById(R.id.minusCartBtn);
        }
    }

    private void getDelItem(Cart swipedItem){
        String plantID = swipedItem.getP_id();
        CollectionReference addressCollectionRef = db.collection("User").document(Objects.requireNonNull(auth.getCurrentUser()).getUid()).collection("Cart");

        Task<QuerySnapshot> taskId = addressCollectionRef.whereEqualTo("p_id", plantID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String docId = document.getId();
                            delCartItem(docId, swipedItem);
                        }
                    } else {
                        Log.d("Edit Address", "Error getting documents: ", task.getException());
                    }
                });
    }

    private void delCartItem(String docId, Cart delItem){
        db.collection("User").document(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .collection("Cart").document(docId)
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Update totals in CartFragment
                        listener.updateTotals();
                        Log.d("Cart deletion", "DocumentSnapshot successfully deleted!");
                    } else {
                        Log.w("Cart deletion", "Error deleting document", task.getException());
                    }
                });
    }
}
