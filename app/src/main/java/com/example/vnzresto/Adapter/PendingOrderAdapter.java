package com.example.vnzresto.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vnzresto.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class PendingOrderAdapter extends RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder> {

    private Context context;
    private List<String> customerNames;
    private List<String> quantity;
    private List<String> foodImage;
    private OnItemClicked itemClicked;

    public interface OnItemClicked {
        void onItemClickListener(int position);
        void onItemAcceptClickListener(int position);
        void onItemDispatchClickListener(int position);
    }
    public PendingOrderAdapter(Context context, List<String> customerNames, List<String> quantity, List<String> foodImage, OnItemClicked itemClicked) {
        this.context = context;
        this.customerNames = customerNames;
        this.quantity = quantity;
        this.foodImage = foodImage;
        this.itemClicked = itemClicked;
    }

    @NonNull
    @Override
    public PendingOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.pending_order_item, parent, false);
        return new PendingOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingOrderViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return customerNames.size();
    }
    class PendingOrderViewHolder extends RecyclerView.ViewHolder {

        TextView customerNameTextView;
        TextView pendingOrderQuantityTextView;
        ImageView orderedFoodImageView;
        Button orderedAcceptButton;

        private boolean isAccepted = false;

        public PendingOrderViewHolder(@NonNull View itemView) {
            super(itemView);

            customerNameTextView = itemView.findViewById(R.id.customerName);
            pendingOrderQuantityTextView = itemView.findViewById(R.id.pendingOredarQuantity);
            orderedFoodImageView = itemView.findViewById(R.id.orderdFoodImage);
            orderedAcceptButton = itemView.findViewById(R.id.orderedAcceptButton);
        }

        public void bind(int position) {
            customerNameTextView.setText(customerNames.get(position));
            String uriString = foodImage.get(position);
            if (uriString != null && !uriString.isEmpty()) {
                Uri uri = Uri.parse(uriString);
                new ImageLoadTask(uri.toString(), orderedFoodImageView).execute();
            } else {
                orderedFoodImageView.setImageResource(R.drawable.ic_launcher_background);
            }

            if (!isAccepted) {
                orderedAcceptButton.setText("Accept");
            } else {
                orderedAcceptButton.setText("Dispatch");
            }

            orderedAcceptButton.setOnClickListener(v -> {
                int adapterPos = getAdapterPosition();
                if (adapterPos == RecyclerView.NO_POSITION) return;

                if (!isAccepted) {
                    orderedAcceptButton.setText("Dispatch");
                    isAccepted = true;
                    showToast("Order is accepted");
                    if (itemClicked != null) {
                        itemClicked.onItemAcceptClickListener(adapterPos);
                    }
                } else {
                    if (adapterPos >= 0 && adapterPos < customerNames.size()) {
                        customerNames.remove(adapterPos);
                        quantity.remove(adapterPos);
                        foodImage.remove(adapterPos);
                        notifyItemRemoved(adapterPos);
                        notifyItemRangeChanged(adapterPos, customerNames.size());
                    }
                    showToast("Order is dispatched");
                    if (itemClicked != null) {
                        itemClicked.onItemDispatchClickListener(adapterPos);
                    }
                }
            });

            itemView.setOnClickListener(v -> {
                int adapterPos = getAdapterPosition();
                if (adapterPos != RecyclerView.NO_POSITION && itemClicked != null) {
                    itemClicked.onItemClickListener(adapterPos);
                }
            });
        }
        private void showToast(String message) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    private static class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {
        private String url;
        private ImageView imageView;
        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }
        @Override
        protected Bitmap doInBackground(Void... voids) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                return BitmapFactory.decodeStream(input);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                imageView.setImageBitmap(result);
            } else {
                imageView.setImageResource(R.drawable.ic_launcher_background);
            }
        }
    }
}
