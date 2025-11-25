package com.example.vnzresto.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vnzresto.R;

import java.util.List;

public class BuyAgainAdapter extends RecyclerView.Adapter<BuyAgainAdapter.BuyAgainViewHolder> {

    private Context context;
    private List<String> buyAgainFoodName;
    private List<String> buyAgainFoodPrice;
    private List<String> buyAgainFoodImage;
    public interface OnBuyAgainClick {
        void onBuyAgain(int position);
    }

    private OnBuyAgainClick listener;
    public void setOnBuyAgainClickListener(OnBuyAgainClick listener) {
        this.listener = listener;
    }
    public BuyAgainAdapter(Context context, List<String> buyAgainFoodName, List<String> buyAgainFoodPrice, List<String> buyAgainFoodImage) {
        this.context = context;
        this.buyAgainFoodName = buyAgainFoodName;
        this.buyAgainFoodPrice = buyAgainFoodPrice;
        this.buyAgainFoodImage = buyAgainFoodImage;
    }
    @NonNull
    @Override
    public BuyAgainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.buy_again_item, parent, false);
        return new BuyAgainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BuyAgainViewHolder holder, int position) {
        String name = buyAgainFoodName.get(position);
        String price = buyAgainFoodPrice.get(position);
        String image = buyAgainFoodImage.get(position);
        holder.buyAgainFoodName.setText(name);
        holder.buyAgainFoodPrice.setText(price);

        if (image != null && !image.isEmpty()) {
            try {
                Uri uri = Uri.parse(image);
                holder.buyAgainFoodImage.setImageURI(uri);
            } catch (Exception e) {
                holder.buyAgainFoodImage.setImageResource(R.drawable.menu2);
            }
        } else {
            holder.buyAgainFoodImage.setImageResource(R.drawable.menu2);
        }
    }

    @Override
    public int getItemCount() {
        return buyAgainFoodName != null ? buyAgainFoodName.size() : 0;
    }

    public class BuyAgainViewHolder extends RecyclerView.ViewHolder {

        TextView buyAgainFoodName, buyAgainFoodPrice;
        ImageView buyAgainFoodImage;
        Button buyaGainFoodButton;

        public BuyAgainViewHolder(@NonNull View itemView) {
            super(itemView);
            buyAgainFoodName = itemView.findViewById(R.id.buyAgainFoodName);
            buyAgainFoodPrice = itemView.findViewById(R.id.buyAgainFoodPrice);
            buyAgainFoodImage = itemView.findViewById(R.id.buyAgainFoodImage);
            buyaGainFoodButton = itemView.findViewById(R.id.buyaGainFoodButton);
            buyaGainFoodButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onBuyAgain(getAdapterPosition());
                }
            });
        }
    }
}