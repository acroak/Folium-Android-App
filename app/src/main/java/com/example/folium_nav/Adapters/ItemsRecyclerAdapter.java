package com.example.folium_nav.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.folium_nav.Domain.Items;
import com.example.folium_nav.Fragments.DetailFragment;
import com.example.folium_nav.R;

import java.util.List;

public class ItemsRecyclerAdapter extends RecyclerView.Adapter<ItemsRecyclerAdapter.ViewHolder> {
    Context applicationContext;
    List<Items> mItemsList;
    public ItemsRecyclerAdapter(Context applicationContext, List<Items> mItemsList) {
        this.applicationContext=applicationContext;
        this.mItemsList=mItemsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(applicationContext).inflate(R.layout.single_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.mCost.setText(String.format("$%.2f",mItemsList.get(position).getPrice()));
        holder.mName.setText(mItemsList.get(position).getName());

        //check to see if category has an image, if not use the placeholder
        String checkImgUrl = mItemsList.get(position).getImg_url();
        if (checkImgUrl.isEmpty()){
            Glide.with(applicationContext).load(R.drawable.placeholder)
                    .centerCrop()
                    .into(holder.mItemImage);
        } else {
            Glide.with(applicationContext).load(mItemsList.get(position).getImg_url())
                    .centerCrop()
                    .into(holder.mItemImage);
        }

        holder.singleItemCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Items items = mItemsList.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("detail", items);
                DetailFragment detailFragment = new DetailFragment();
                detailFragment.setArguments(bundle);

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.items_recycler_page, detailFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mItemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView mItemImage;
        private TextView mCost;
        private TextView mName;
        private ConstraintLayout singleItemCont;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mItemImage=itemView.findViewById(R.id.item_image);
            mCost=itemView.findViewById(R.id.item_cost);
            mName=itemView.findViewById(R.id.item_nam);
            singleItemCont = itemView.findViewById(R.id.singleItemCont);
        }
    }
}
