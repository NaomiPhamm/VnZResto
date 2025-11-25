package com.example.vnzresto.Resto_Admin_Activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vnzresto.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminProfile extends AppCompatActivity {


    private ImageButton backButton;
    private TextView editButton;
    private EditText nameEditText;
    private EditText addressEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private Button saveInfoButton;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference adminReference;

    private boolean isEditable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        backButton = findViewById(R.id.backButton);
        editButton = findViewById(R.id.editButton);
        nameEditText = findViewById(R.id.name);
        addressEditText = findViewById(R.id.address);
        emailEditText = findViewById(R.id.email);
        phoneEditText = findViewById(R.id.phone);
        saveInfoButton = findViewById(R.id.saveInfoButton);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        adminReference = database.getReference("user");

        setFieldsEnabled(false);

        backButton.setOnClickListener(v -> finish());

        editButton.setOnClickListener(v -> {
            isEditable = !isEditable;
            setFieldsEnabled(isEditable);

            if (isEditable) {
                nameEditText.requestFocus();
            }
        });

        saveInfoButton.setOnClickListener(v -> updateUserData());

        retrieveUserData();
    }

    private void setFieldsEnabled(boolean enabled) {
        nameEditText.setEnabled(enabled);
        addressEditText.setEnabled(enabled);
        phoneEditText.setEnabled(enabled);

        emailEditText.setEnabled(false);

        saveInfoButton.setEnabled(enabled);
    }

    private void retrieveUserData() {
        String currentUserUid = auth.getCurrentUser() != null
                ? auth.getCurrentUser().getUid()
                : null;

        if (currentUserUid == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference userReference = adminReference.child(currentUserUid);

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Object ownerName = snapshot.child("name").getValue();
                    Object email = snapshot.child("email").getValue();
                    Object address = snapshot.child("address").getValue();
                    Object phone = snapshot.child("phone").getValue();

                    setDataToViews(ownerName, email, address, phone);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminProfile.this,
                        "Failed to load profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setDataToViews(Object ownerName,
                                Object email,
                                Object address,
                                Object phone) {

        nameEditText.setText(ownerName != null ? ownerName.toString() : "");
        emailEditText.setText(email != null ? email.toString() : "");
        phoneEditText.setText(phone != null ? phone.toString() : "");
        addressEditText.setText(address != null ? address.toString() : "");
    }

    private void updateUserData() {
        String updateName = nameEditText.getText().toString().trim();
        String updatePhone = phoneEditText.getText().toString().trim();
        String updateAddress = addressEditText.getText().toString().trim();

        String currentUserUid = auth.getCurrentUser() != null
                ? auth.getCurrentUser().getUid()
                : null;

        if (currentUserUid == null) {
            Toast.makeText(this, "Profile Update Failed ", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference userReference = adminReference.child(currentUserUid);

        userReference.child("name").setValue(updateName);
        userReference.child("phone").setValue(updatePhone);
        userReference.child("address").setValue(updateAddress);

        Toast.makeText(this, "Profile Updated Successfully ", Toast.LENGTH_SHORT).show();

        isEditable = false;
        setFieldsEnabled(false);
    }
}
