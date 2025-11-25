package com.example.vnzresto.Resto_User_Activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vnzresto.Model.CartItems;
import com.example.vnzresto.Model.MenuItem;
import com.example.vnzresto.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetailsActivity extends AppCompatActivity {

    private TextView detailFoodName, detailDescription, detailIngredients;
    private ImageView detailFoodImage;
    private ImageButton imageButton, favoriteButton;
    private Button addItemButton;

    private String foodName;
    private String foodImage;
    private String foodDescriptions;
    private String foodIngredients;
    private String foodPrice;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        auth = FirebaseAuth.getInstance();

        detailFoodName = findViewById(R.id.detailFoodName);
        detailDescription = findViewById(R.id.detailDescription);
        detailIngredients = findViewById(R.id.detailIngredients);
        detailFoodImage = findViewById(R.id.detailFoodImage);
        imageButton = findViewById(R.id.imageButton);
        addItemButton = findViewById(R.id.addItemButton);

        favoriteButton = findViewById(R.id.favoriteButton);

        foodName = getIntent().getStringExtra("MenuItemName");
        foodDescriptions = getIntent().getStringExtra("MenuItemDescription");
        foodIngredients = getIntent().getStringExtra("MenuItemIngredients");
        foodPrice = getIntent().getStringExtra("MenuItemPrice");
        foodImage = getIntent().getStringExtra("MenuItemImage");

        detailFoodName.setText(foodName != null ? foodName : "");
        detailDescription.setText(foodDescriptions != null ? foodDescriptions : "");
        detailIngredients.setText(foodIngredients != null ? foodIngredients : "");

        if (foodImage != null && !foodImage.isEmpty()) {
            try {
                if (foodImage.startsWith("http")) {

                    new ImageLoadTask(foodImage, detailFoodImage).execute();
                } else {
                    Uri uri = Uri.parse(foodImage);
                    detailFoodImage.setImageURI(uri);
                }
            } catch (Exception e) {
                e.printStackTrace();
                detailFoodImage.setImageResource(R.drawable.ic_launcher_background);
            }
        } else {
            detailFoodImage.setImageResource(R.drawable.ic_launcher_background);
        }

        imageButton.setOnClickListener(v -> finish());

        addItemButton.setOnClickListener(v -> addItemToCart());

        if (favoriteButton != null) {
            favoriteButton.setOnClickListener(v -> addItemToFavorite());
        }

    }
    private void addItemToFavorite() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = database.getReference();
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : "";

        if (userId.isEmpty()) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }
        MenuItem favItem = new MenuItem(
                null,
                foodName != null ? foodName : "",
                foodPrice != null ? foodPrice : "",
                foodDescriptions != null ? foodDescriptions : "",
                foodImage != null ? foodImage : "",
                foodIngredients != null ? foodIngredients : ""
        );


        rootRef.child("user")
                .child(userId)
                .child("Favorites")
                .push()
                .setValue(favItem)
                .addOnSuccessListener(unused ->
                        Toast.makeText(this, "Added to Favorites ", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to add ", Toast.LENGTH_SHORT).show());
    }


    private void addItemToCart() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = database.getReference();
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : "";

        if (userId.isEmpty()) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        CartItems cartItem = new CartItems(
                foodName != null ? foodName : "",
                foodPrice != null ? foodPrice : "",
                foodDescriptions != null ? foodDescriptions : "",
                foodImage != null ? foodImage : "",
                foodIngredients != null ? foodIngredients : "",
                1
        );


        rootRef.child("user")
                .child(userId)
                .child("CartItems")
                .push()
                .setValue(cartItem)
                .addOnSuccessListener(unused ->
                        Toast.makeText(DetailsActivity.this,
                                "Item added to cart successfully ",
                                Toast.LENGTH_SHORT).show()
                )
                .addOnFailureListener(e ->
                        Toast.makeText(DetailsActivity.this,
                                "Item not added ",
                                Toast.LENGTH_SHORT).show()
                );
    }


    private static class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private final String url;
        private final ImageView imageView;

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
