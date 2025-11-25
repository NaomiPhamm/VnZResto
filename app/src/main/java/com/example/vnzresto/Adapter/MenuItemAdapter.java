package com.example.vnzresto.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vnzresto.Model.AllMenu;
import com.example.vnzresto.R;
import com.google.firebase.database.DatabaseReference;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.AddItemViewHolder> {

    private Context context;
    private ArrayList<AllMenu> menuList;
    private OnDeleteClickListener onDeleteClickListener;
    private int[] itemQuantities;
    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public MenuItemAdapter(Context context, ArrayList<AllMenu> menuList, DatabaseReference databaseReference,OnDeleteClickListener onDeleteClickListener) {
        this.context = context;
        this.menuList = menuList;
        this.onDeleteClickListener = onDeleteClickListener;
        this.itemQuantities = new int[menuList.size()];

        for (int i = 0; i < itemQuantities.length; i++) {
            itemQuantities[i] = 1;
        }
    }

    @NonNull
    @Override
    public AddItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_item, parent, false);
        return new AddItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddItemViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    class AddItemViewHolder extends RecyclerView.ViewHolder {

        ImageView foodImageView;
        TextView foodNameTextView, priceTextView, quantityTextVIew;
        ImageButton minusButton, pluseButton, deleteButton;

        public AddItemViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImageView = itemView.findViewById(R.id.foodImageView);
            foodNameTextView = itemView.findViewById(R.id.foodNameTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            quantityTextVIew = itemView.findViewById(R.id.quantityTextVIew);
            minusButton = itemView.findViewById(R.id.minusButton);
            pluseButton = itemView.findViewById(R.id.pluseButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }

        public void bind(int position) {
            AllMenu menuItem = menuList.get(position);
            foodNameTextView.setText(menuItem.getFoodName());
            priceTextView.setText(menuItem.getFoodPrice());
            String imageUrl = menuItem.getFoodImage();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                new ImageLoadTask(imageUrl, foodImageView).execute();
            } else {
                foodImageView.setImageResource(R.drawable.ic_launcher_background);
            }

            int quantity = itemQuantities[position];
            quantityTextVIew.setText(String.valueOf(quantity));

            minusButton.setOnClickListener(v -> decreaseQuantity(position));
            pluseButton.setOnClickListener(v -> increaseQuantity(position));
            deleteButton.setOnClickListener(v -> {
                if (onDeleteClickListener != null) {
                    onDeleteClickListener.onDeleteClick(getAdapterPosition());
                }
            });
        }

        private void increaseQuantity(int position) {
            if (position >= 0 && position < itemQuantities.length) {
                if (itemQuantities[position] < 10) {
                    itemQuantities[position]++;
                    quantityTextVIew.setText(String.valueOf(itemQuantities[position]));
                }
            }
        }

        private void decreaseQuantity(int position) {
            if (position >= 0 && position < itemQuantities.length) {
                if (itemQuantities[position] > 1) {
                    itemQuantities[position]--;
                    quantityTextVIew.setText(String.valueOf(itemQuantities[position]));
                }
            }
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


