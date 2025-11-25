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

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailsViewHolder> {

    private Context context;
    private ArrayList<String> foodNames;
    private ArrayList<String> foodImages;
    private ArrayList<Integer> foodQuantities;
    private ArrayList<String> foodPrices;

    public OrderDetailsAdapter(Context context, ArrayList<String> foodNames, ArrayList<String> foodImages,ArrayList<Integer> foodQuantities, ArrayList<String> foodPrices) {
        this.context = context;
        this.foodNames = foodNames;
        this.foodImages = foodImages;
        this.foodQuantities = foodQuantities;
        this.foodPrices = foodPrices;
    }

    @NonNull
    @Override
    public OrderDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_detail_item, parent, false);
        return new OrderDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailsViewHolder holder, int position) {
        String name = foodNames.get(position);
        String imageUri = foodImages.get(position);
        int quantity = foodQuantities.get(position);
        String price = foodPrices.get(position);

        holder.foodName.setText(name);
        holder.foodQuantity.setText(String.valueOf(quantity));
        holder.foodPrice.setText(price);

        if (imageUri != null && !imageUri.isEmpty()) {
            try {
                Uri uri = Uri.parse(imageUri);
                holder.foodImage.setImageURI(uri);
            } catch (Exception e) {
                holder.foodImage.setImageResource(R.drawable.menu2);
            }
        } else {
            holder.foodImage.setImageResource(R.drawable.menu2);
        }
    }

    @Override
    public int getItemCount() {
        return foodNames.size();
    }
    public static class OrderDetailsViewHolder extends RecyclerView.ViewHolder {

        TextView foodName, foodQuantity, foodPrice;
        ImageView foodImage;
        public OrderDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.foodName);
            foodQuantity = itemView.findViewById(R.id.foodQuantity);
            foodPrice = itemView.findViewById(R.id.foodPrice);
            foodImage = itemView.findViewById(R.id.foodImage);
        }
    }
}