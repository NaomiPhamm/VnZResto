package com.example.vnzresto.Resto_Admin_Activity;


import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vnzresto.R;
import com.example.vnzresto.Model.AllMenu;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddItem extends AppCompatActivity {
    private ImageButton backButton;
    private EditText foodName, foodPrice, description, ingredint;
    private TextView selectImage;
    private ImageView selectedImage;
    private Button AddItemButton;
    private DatabaseReference menuRef;
    private StorageReference storageRef;
    private Uri imageUri;

    private final ActivityResultLauncher<String> selectImageLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    imageUri = uri;
                    selectedImage.setImageURI(uri);
                }
            });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        menuRef = FirebaseDatabase.getInstance().getReference("menu");
        storageRef = FirebaseStorage.getInstance().getReference("menu_images");

        backButton = findViewById(R.id.backButton);
        foodName = findViewById(R.id.foodName);
        foodPrice = findViewById(R.id.foodPrice);
        description = findViewById(R.id.description);
        ingredint = findViewById(R.id.ingredint);
        selectImage = findViewById(R.id.selectImage);
        selectedImage = findViewById(R.id.selectedImage);
        AddItemButton = findViewById(R.id.AddItemButton);
        backButton.setOnClickListener(v -> finish());

        selectImage.setOnClickListener(v -> selectImageLauncher.launch("image/*"));

        AddItemButton.setOnClickListener(v -> saveItem());
    }
    private void saveItem(){
        String name = foodName.getText().toString().trim();
        String price = foodPrice.getText().toString().trim();
        String desc = description.getText().toString().trim();
        String ingred = ingredint.getText().toString().trim();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(price) || TextUtils.isEmpty(desc) || TextUtils.isEmpty(ingred) || imageUri == null) {

            Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }
        String id = menuRef.push().getKey();
        if (id == null) {
            Toast.makeText(this, "Error generating item ID", Toast.LENGTH_SHORT).show();
            return;
        }
        StorageReference imageRef = storageRef.child(id + ".jpg");
        UploadTask uploadTask = imageRef.putFile(imageUri);

        uploadTask.addOnSuccessListener(taskSnapshot ->
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            AllMenu menuItem = new AllMenu(id, name, price, desc, imageUrl, ingred);
                            menuRef.child(id).setValue(menuItem).addOnSuccessListener(aVoid -> {
                                        Toast.makeText(this, "Item added successfully!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(this, "Database error: " + e.getMessage(), Toast.LENGTH_LONG).show()
                                    );
                        })
                )
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
    }
}
