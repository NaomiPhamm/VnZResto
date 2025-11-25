package com.example.vnzresto.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vnzresto.Model.OrderDetailsModel;
import com.example.vnzresto.R;

import java.util.ArrayList;

public class EarningAdapter extends RecyclerView.Adapter<EarningAdapter.EarningViewHolder> {

    private Context context;
    private ArrayList<OrderDetailsModel> orders;

    public EarningAdapter(Context context, ArrayList<OrderDetailsModel> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public EarningViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.earning_item, parent, false);
        return new EarningViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EarningViewHolder holder, int position) {
        OrderDetailsModel order = orders.get(position);

        holder.userNameText.setText(order.getUserName() != null ? order.getUserName() : "");
        holder.totalPriceText.setText(order.getTotalPrice() != null ? order.getTotalPrice() : "0$");
    }
    @Override
    public int getItemCount() {
        return orders != null ? orders.size() : 0;
    }
    static class EarningViewHolder extends RecyclerView.ViewHolder {

        TextView userNameText, totalPriceText;
        public EarningViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameText = itemView.findViewById(R.id.userNameText);
            totalPriceText = itemView.findViewById(R.id.totalPriceText);

        }
    }
}
