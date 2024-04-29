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
import com.example.folium_nav.Domain.Category;
import com.example.folium_nav.Fragments.ItemsFragment;
import com.example.folium_nav.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Context context;
    private List<Category> categoryList;
    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    //inflate your category items into single category item (detail view) and place within the recycler view
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_category_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //fetch each imgUrl for each category and place them into a holder of the image of single_category_item layout

        //check to see if category has an image, if not use the placeholder
        String checkImgUrl = categoryList.get(position).getImg_url();

        if (checkImgUrl.isEmpty()){
            Glide.with(context).load(R.drawable.placeholder)
                    .centerCrop()
                    .into(holder.imgView);
        } else {
            Glide.with(context).load(categoryList.get(position).getImg_url())
                    .centerCrop()
                    .into(holder.imgView);
        }
        holder.titleTV.setText(categoryList.get(position).getType());

        //what to do when the category is clicked
        holder.catContainter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = categoryList.get(position).getType();
                Bundle bundle = new Bundle();
                bundle.putString("type", type);
                bundle.putString("title", type);
                ItemsFragment itemsFragment = new ItemsFragment();
                itemsFragment.setArguments(bundle);

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_page, itemsFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgView;

        private TextView titleTV;

        private ConstraintLayout catContainter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.categoryImg);
            titleTV = itemView.findViewById(R.id.catTitle);
            catContainter = itemView.findViewById(R.id.catContainer);
        }

    }
}
