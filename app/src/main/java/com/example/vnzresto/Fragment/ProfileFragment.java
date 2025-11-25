package com.example.vnzresto.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vnzresto.Login;
import com.example.vnzresto.Model.UserModel;
import com.example.vnzresto.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private EditText nameEditText, emailEditText, addressEditText, phoneEditText;
    private Button editButton, saveInfoButton,logoutButton ;

    private FirebaseAuth auth;
    private FirebaseDatabase database;

    public ProfileFragment() {

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        editButton = view.findViewById(R.id.editButton);
        saveInfoButton = view.findViewById(R.id.saveInfoButton);

        nameEditText = view.findViewById(R.id.name);
        emailEditText = view.findViewById(R.id.email);
        addressEditText = view.findViewById(R.id.address);
        phoneEditText = view.findViewById(R.id.phone);
        logoutButton = view.findViewById(R.id.Log_outButton);

        setEnabled(false);
        setUserData();
        editButton.setOnClickListener(v -> {
            boolean enable = !nameEditText.isEnabled();
            setEnabled(enable);
        });

        saveInfoButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String address = addressEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();

            updateData(name, email, address, phone);
        });
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(requireActivity(), Login.class);
            startActivity(intent);
            requireActivity().finish();
        });
        return view;

    }
    private void setEnabled(boolean enabled) {
        nameEditText.setEnabled(enabled);
        emailEditText.setEnabled(false);
        addressEditText.setEnabled(enabled);
        phoneEditText.setEnabled(enabled);
    }

    private void updateData(String name, String email, String address, String phone) {
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        if (userId == null) {
            Toast.makeText(requireContext(),
                    "User not logged in",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference userReference = database.getReference("user").child(userId);

        Map<String, Object> userData = new HashMap<>();
        userData.put("name", name);
        userData.put("address", address);
        userData.put("email", email);
        userData.put("phone", phone);

        userReference.setValue(userData)
                .addOnSuccessListener(unused -> Toast.makeText(
                        requireContext(),
                        "Profile updated successfully ",
                        Toast.LENGTH_SHORT
                ).show())
                .addOnFailureListener(e -> Toast.makeText(
                        requireContext(),
                        "Profile update failed ",
                        Toast.LENGTH_SHORT
                ).show());
    }

    private void setUserData() {
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        if (userId == null) {
            return;
        }

        DatabaseReference userReference = database.getReference("user").child(userId);

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    UserModel userProfile = snapshot.getValue(UserModel.class);
                    if (userProfile != null) {
                        if (!TextUtils.isEmpty(userProfile.getName())) {
                            nameEditText.setText(userProfile.getName());
                        }
                        if (!TextUtils.isEmpty(userProfile.getAddress())) {
                            addressEditText.setText(userProfile.getAddress());
                        }
                        if (!TextUtils.isEmpty(userProfile.getEmail())) {
                            emailEditText.setText(userProfile.getEmail());
                        }
                        if (!TextUtils.isEmpty(userProfile.getPhone())) {
                            phoneEditText.setText(userProfile.getPhone());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
