package com.example.vnzresto.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vnzresto.R;

import java.util.ArrayList;

public class RecentBuyAdapter extends RecyclerView.Adapter<RecentBuyAdapter.RecentViewHolder> {

    private Context context;
    private ArrayList<String> foodNameList;
    private ArrayList<String> foodImageList;
    private ArrayList<String> foodPriceList;
    private ArrayList<Integer> foodQuantityList;

    public RecentBuyAdapter(Context context, ArrayList<String> foodNameList, ArrayList<String> foodImageList, ArrayList<String> foodPriceList, ArrayList<Integer> foodQuantityList) {
        this.context = context;
        this.foodNameList = foodNameList;
        this.foodImageList = foodImageList;
        this.foodPriceList = foodPriceList;
        this.foodQuantityList = foodQuantityList;
    }

    @NonNull
    @Override
    public RecentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.recent_buy_item, parent, false);
        return new RecentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentViewHolder holder, int position) {
        String name = foodNameList.get(position);
        String price = foodPriceList.get(position);
        int quantity = foodQuantityList.get(position);
        String imageUriString = foodImageList.get(position);

        holder.foodName.setText(name);
        holder.foodPrice.setText(price);
        holder.foodQuantity.setText(String.valueOf(quantity));
        if (imageUriString != null && !imageUriString.isEmpty()) {
            Uri uri = Uri.parse(imageUriString);
            holder.foodImg.setImageURI(uri);
        } else {
            holder.foodImg.setImageResource(R.drawable.menu2);
        }
    }

    @Override
    public int getItemCount() {
        return foodNameList != null ? foodNameList.size() : 0;
    }

    public static class RecentViewHolder extends RecyclerView.ViewHolder {
        TextView foodName, foodPrice, foodQuantity;
        ImageView foodImg;
        public RecentViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.foodName);
            foodPrice = itemView.findViewById(R.id.foodPrice);
            foodQuantity = itemView.findViewById(R.id.foodQuantity);
            foodImg = itemView.findViewById(R.id.foodImage);
        }
    }
}

