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

import com.example.vnzresto.Model.CartItems;
import com.example.vnzresto.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CartAdapter extends  RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private Context context;
    private ArrayList<CartItems> cartList;
    private FirebaseAuth auth;
    private DatabaseReference cartItemsReference;
    public CartAdapter(Context context,ArrayList<CartItems>cartList){
        this.context =context;
        this.cartList=cartList;
        auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : "";
        cartItemsReference= database.getReference("user")
                .child(userId)
                .child("CartItems");

    }

    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item,parent,false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        holder.bind(cartList.get(position),position);
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        TextView cartFoodName, cartItemPrice, catItemQuantity;
        ImageView cartImage;
        ImageButton minusbutton, plusebutton, deleteButton;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView );
            cartFoodName = itemView.findViewById(R.id.cartFoodName);
            cartItemPrice = itemView.findViewById(R.id.cartItemPrice);
            catItemQuantity = itemView.findViewById(R.id.catItemQuantity);
            cartImage = itemView.findViewById(R.id.cartImage);
            minusbutton = itemView.findViewById(R.id.minusbutton);
            plusebutton = itemView.findViewById(R.id.plusebutton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }

        public void bind(CartItems item, int position){
            cartFoodName.setText(item.getFood_name());
            cartItemPrice.setText(item.getFood_price());
            catItemQuantity.setText(String.valueOf(item.getFoodQuantity())); String img = item.getFood_img();
            if (img != null && !img.isEmpty()) {
                new ImageLoadTask(img, cartImage).execute();
            } else {
                cartImage.setImageResource(R.drawable.ic_launcher_background);
            }
            plusebutton.setOnClickListener(v -> {
                int qty = item.getFoodQuantity();
                if (qty < 10) {
                    item.setFoodQuantity(qty + 1);
                    notifyItemChanged(position);
                }
            });

            minusbutton.setOnClickListener(v -> {
                int qty = item.getFoodQuantity();
                if (qty > 1) {
                    item.setFoodQuantity(qty - 1);
                    notifyItemChanged(position);
                }
            });

            deleteButton.setOnClickListener(v -> delete(position));
        }

    }
    private void delete(int  p){
        CartItems deletedItem =cartList.get(p);

        cartItemsReference.orderByChild("food_name").equalTo(deletedItem.getFood_name()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            snap.getRef().removeValue();
                        }
                        cartList.remove(p);
                        notifyItemRemoved(p);
                        notifyItemRangeChanged(p, cartList.size());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
    private  static  class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {
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
