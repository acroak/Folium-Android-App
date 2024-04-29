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


    public class FeaturedAdapter extends RecyclerView.Adapter<FeaturedAdapter.ViewHolder>{
        private Context context;
        private List<Items> featuredList;

        public FeaturedAdapter(Context context, List<Items> featuredList) {
            this.context = context;
            this.featuredList = featuredList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.single_featured_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            //check to see if category has an image, if not use the placeholder
            String checkImgUrl = featuredList.get(position).getImg_url();
            if (checkImgUrl.isEmpty()){
                Glide.with(context).load(R.drawable.placeholder)
                        .centerCrop()
                        .into(holder.featImg);
            } else {
                Glide.with(context).load(featuredList.get(position).getImg_url())
                        .centerCrop()
                        .into(holder.featImg);
            }

            holder.price.setText((String.format("$%.2f", featuredList.get(position).getPrice())));
            holder.name.setText(featuredList.get(position).getName());

            holder.featContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Items featured = featuredList.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("detail", featured);
                    DetailFragment detailFragment = new DetailFragment();
                    detailFragment.setArguments(bundle);

                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.home_page, detailFragment)
                            .addToBackStack(null)
                            .commit();

                }
            });


        }

        @Override
        public int getItemCount() {
            return featuredList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView featImg;
            private TextView name, price;
            private ConstraintLayout featContainer;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                featImg = itemView.findViewById(R.id.featImg);
                name = itemView.findViewById(R.id.featTitle);
                price = itemView.findViewById(R.id.featPrice);
                featContainer = itemView.findViewById(R.id.featuredContainer);
            }
        }
    }

