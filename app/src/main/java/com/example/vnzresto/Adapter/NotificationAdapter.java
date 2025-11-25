package com.example.vnzresto.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vnzresto.R;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private ArrayList<String> notificationList;
    private ArrayList<Integer> notificationImages;

    public NotificationAdapter(ArrayList<String> notificationList, ArrayList<Integer> notificationImages) {
        this.notificationList = notificationList;
        this.notificationImages = notificationImages;
    }
    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_item, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView notificationTextView;
        ImageView notificationImageView;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            notificationTextView = itemView.findViewById(R.id.notificationTextView);
            notificationImageView = itemView.findViewById(R.id.notificationImageView);
        }

        public void bind(int position) {
            notificationTextView.setText(notificationList.get(position));
            notificationImageView.setImageResource(notificationImages.get(position));
        }
    }
}
