package com.example.folium_nav.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.folium_nav.Domain.Items;
import com.example.folium_nav.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class DetailFragment extends Fragment {
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FragmentManager fragmentManager;
    private ImageView itemImg;
    private TextView itemName, itemRatingDesc, itemPrice, itemDescription, sciName;
    TextView lightLvl, droughtTol, petSafe, childSafe, waterFreq, humidity, soilDrain, soilPh,fertType,fertStr;
    LinearLayout waterFreqContainer, soilDrainCont;
    private Button itemRating, addToCartBtn;

    private Object obj;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        itemImg = view.findViewById(R.id.itemImg);
        itemName = view.findViewById(R.id.itemName);
        itemRating = view.findViewById(R.id.itemRating);
        itemRatingDesc = view.findViewById(R.id.itemRatingDesc);
        itemPrice = view.findViewById(R.id.itemPrice);
        itemDescription = view.findViewById(R.id.itemDescription);
        sciName = view.findViewById(R.id.itemSciName);

        lightLvl = view.findViewById(R.id.lightLvl);
        droughtTol = view.findViewById(R.id.droughtTol);
        petSafe = view.findViewById(R.id.petSafe);
        childSafe = view.findViewById(R.id.childSafe);
        waterFreq = view.findViewById(R.id.waterFeq);
        waterFreqContainer = view.findViewById(R.id.waterFreqContainer);
        humidity = view.findViewById(R.id.humidity);
        soilDrainCont = view.findViewById(R.id.soilDrainCont);
        soilDrain = view.findViewById(R.id.soilDrain);
        soilPh = view.findViewById(R.id.soilPh);
        fertType = view.findViewById(R.id.fertType);
        fertStr = view.findViewById(R.id.fertStr);

        addToCartBtn = view.findViewById(R.id.addToCartBtn);

        FrameLayout detailContainer = view.findViewById(R.id.detail_page);

        // Retrieve the data passed from the bundle
        Bundle bundle = getArguments();
        obj = getArguments().getSerializable("detail");
        Items item = (Items) obj;

        String title = item.getName().toLowerCase();

        //Set the detail page title
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(title);

        //check to see if category has an image, if not use the placeholder
        String checkImgUrl = item.getImg_url();

        if (checkImgUrl.isEmpty()){
            Glide.with(this).load(R.drawable.placeholder)
                    .centerCrop()
                    .into(itemImg);
        } else {
            Glide.with(this).load(item.getImg_url())
                    .centerCrop()
                    .into(itemImg);
        }

        itemName.setText(item.getName());
        itemPrice.setText(String.format("$%.2f", item.getPrice()));
        sciName.setText(item.getSci_name());

        getRatings(item);
        itemDescription.setText(item.getDescription());

        lightLvl.setText(item.getLight_level());
        droughtTol.setText(getCheckOrX(item.getDrought_tol()));
        petSafe.setText(getCheckOrX(item.getHarmful_pets()));
        childSafe.setText(getCheckOrX(item.getHarmful_ppl()));
        waterFreq.setText(item.getWater_freq());
        humidity.setText(item.getHumidity()+"%");
        soilDrain.setText(item.getSoil_drain());
        soilPh.setText(item.getSoil_pH());
        fertType.setText(item.getFert_type());
        fertStr.setText(item.getFert_Str());

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if the item is already in the cart
                db.collection("User").document(auth.getCurrentUser().getUid())
                        .collection("Cart").whereEqualTo("p_id", item.getP_id())
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    QuerySnapshot querySnapshot = task.getResult();
                                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                        // Item is already in the cart, update its quantity
                                        DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                                        int currentQuantity = Integer.parseInt(documentSnapshot.getString("quantity"));
                                        int newQuantity = currentQuantity + 1;
                                        double totalPrice = newQuantity * item.getPrice();
                                        documentSnapshot.getReference().update("quantity", String.valueOf(newQuantity), "totalPrice", String.valueOf(totalPrice))
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            // Quantity updated successfully
                                                            getParentFragmentManager().setFragmentResult("cartItemAdded", new Bundle());
                                                            Snackbar.make(requireView(), "Added to Cart", Snackbar.LENGTH_SHORT)
                                                                    .setAnchorView(view)
                                                                    .setAction("View Cart", new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View v) {
                                                                            // Navigate to the cartFragment
                                                                            Fragment cartFragment = new CartFragment();
                                                                            getParentFragmentManager().beginTransaction()
                                                                                    .replace(R.id.detail_page, cartFragment)
                                                                                    .addToBackStack(null)
                                                                                    .commit();
                                                                        }
                                                                    })
                                                                    .show();

                                                        } else {
                                                            Toast.makeText(getContext(), "Error updating cart item quantity", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    } else {
                                        // Item is not in the cart, add it
                                        Map<String, String> map = new HashMap<>();
                                        map.put("img_url", item.getImg_url());
                                        map.put("name", item.getName());
                                        map.put("price", String.valueOf(item.getPrice()));
                                        map.put("quantity", "1");
                                        map.put("totalPrice", String.valueOf(item.getPrice()));
                                        map.put("p_id", item.getP_id());

                                        db.collection("User").document(auth.getCurrentUser().getUid())
                                                .collection("Cart").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                                        if (task.isSuccessful()) {
                                                            getParentFragmentManager().setFragmentResult("cartItemAdded", new Bundle());
                                                            Snackbar.make(requireView(), "Added to Cart", Snackbar.LENGTH_SHORT)
                                                                    .setAnchorView(view)
                                                                    .setAction("View Cart", new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View v) {
                                                                            // Navigate to the cartFragment
                                                                            Fragment cartFragment = new CartFragment();
                                                                            getParentFragmentManager().beginTransaction()
                                                                                    .replace(R.id.detail_page, cartFragment)
                                                                                    .addToBackStack(null)
                                                                                    .commit();
                                                                        }
                                                                    }).show();
                                                        } else {
                                                            Toast.makeText(getContext(), "Error adding cart item", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                } else {
                                    Toast.makeText(getContext(), "Error checking cart for item", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        //handle the weird ability to click thru the detail page and access items from previous fragments like the recycler page
        detailContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Consume the click event
            }
        });

        waterFreqContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Inflate the custom layout
                View customView = getLayoutInflater().inflate(R.layout.watering_table_layout, null);

                // Build the AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(customView)
                        .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        soilDrainCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("About Well Draining Soil:")
                        .setMessage("This type of soil allows water to pass through quickly, preventing root rot and ensuring plants get the right balance of moisture and oxygen.\n" +
                                "\n" +
                                "To mix your own well-draining soil, start with a base of quality potting soil or garden soil. Add in equal parts perlite or coarse sand to improve drainage. For even better results, mix in some compost to boost nutrients and improve soil structure.\n" +
                                "\n" +
                                "Once you have your mix ready, use it to fill your pots or garden beds. Water thoroughly after planting, then monitor the soil moisture to ensure your plants are getting the perfect amount of water. With well-draining soil, your plants will thank you with vibrant growth and healthy roots.")
                        .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });

        // Inflate the layout for this fragment
        return view;
    }



    private void getRatings(Items item) {
        //get the rating int
        itemRating.setText(item.getRating()+"");

        //figure out what the rating number corresponds to for description
        if(item.getRating() >= 4.5){
            itemRatingDesc.setText("Excellent");
        } else if (item.getRating() < 4.5 && item.getRating() >= 4 ){
            itemRatingDesc.setText("Very Good");
        } else if (item.getRating() < 4 && item.getRating() > 3){
            itemRatingDesc.setText("Good");
        } else {
            itemRatingDesc.setText("OK");
        }
    }

    public String getCheckOrX(String source){
        String check = "\u2714"; //✔
        String x = "\u00D7";
        if(source.equalsIgnoreCase("yes")){
            return check;
        } else if (source.equalsIgnoreCase("no")){
            return x;
        } else {
            return source;
        }
    }
}