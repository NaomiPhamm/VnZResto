package com.example.vnzresto.Adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.vnzresto.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder> {
    private Context context;
    private List<String> customerNames;
    private List<Boolean> moneyStatus;

    public DeliveryAdapter(Context context, List<String> customerNames, List<Boolean> moneyStatus) {
        this.context = context;
        this.customerNames = customerNames;
        this.moneyStatus = moneyStatus;
    }

    @NonNull
    @Override
    public DeliveryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.delivery_item, parent, false);
        return new DeliveryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryViewHolder holder, int position) {
        holder.bind(position);
    }
    @Override
    public int getItemCount() {
        return customerNames.size();
    }
    class DeliveryViewHolder extends RecyclerView.ViewHolder {
        TextView customerName, statusMoney;
        View statusColor;
        DeliveryViewHolder(@NonNull View itemView) {
            super(itemView);

            customerName = itemView.findViewById(R.id.customerName);
            statusMoney = itemView.findViewById(R.id.statusMoney);
            statusColor = itemView.findViewById(R.id.statusColor);
        }

        void bind(int position) {
            customerName.setText(customerNames.get(position));

            boolean isReceived = moneyStatus.get(position);

            if (isReceived) {
                statusMoney.setText("Received");
                statusMoney.setTextColor(Color.GREEN);
                statusColor.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            } else {
                statusMoney.setText("Not Received");
                statusMoney.setTextColor(Color.RED);
                statusColor.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            }
        }
    }
}

