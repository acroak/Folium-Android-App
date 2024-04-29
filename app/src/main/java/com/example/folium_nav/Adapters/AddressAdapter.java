package com.example.folium_nav.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.folium_nav.Domain.Address;
import com.example.folium_nav.Fragments.EditAddressFragment;
import com.example.folium_nav.R;

import java.io.Serializable;
import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    private final Context context;
    private final List<Address> addressList;
    private RadioButton selectedRadioBtn;


    public AddressAdapter(Context context, List<Address> addressList) {
        this.context = context;
        this.addressList = addressList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_address_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.addressTV.setText(addressList.get(position).getCompleteAddress());
        //ensure only one radio button can be selected at a time
        holder.selectBtn.setOnClickListener(view -> {
            for(Address address: addressList){
                address.setSelected(false);
            }
            addressList.get(position).setSelected(true);

            if(selectedRadioBtn != null){
                selectedRadioBtn.setChecked(false);
            }
            selectedRadioBtn = (RadioButton) view;
            selectedRadioBtn.setChecked(true);

        });

        //when the more options button is clicked bring up a dialogue where you can edit or delete your address
        holder.optionsBtn.setOnClickListener(view -> {
            Address selectedAddress = addressList.get(position);
            Bundle bundle = new Bundle();
            bundle.putSerializable("addressItem", (Serializable) selectedAddress);
            EditAddressFragment editFragment = new EditAddressFragment();
            editFragment.setArguments(bundle);

            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.add_list_page, editFragment)
                    .commit();

        });
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView addressTV;
        private final RadioButton selectBtn;

        private final ImageView optionsBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            addressTV = itemView.findViewById(R.id.addressDetail);
            selectBtn = itemView.findViewById(R.id.addressDetailRadioBtn);
            optionsBtn = itemView.findViewById(R.id.optionsButton);
        }
    }
}
