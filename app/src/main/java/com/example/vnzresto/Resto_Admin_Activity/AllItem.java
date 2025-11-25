package com.example.vnzresto.Resto_Admin_Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vnzresto.Adapter.MenuItemAdapter;
import com.example.vnzresto.Model.AllMenu;
import com.example.vnzresto.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllItem extends AppCompatActivity {

    private ImageButton backButton;
    private RecyclerView menuRecyclerView;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private ArrayList<AllMenu> menuItems = new ArrayList<>();
    private MenuItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_item);
        backButton = findViewById(R.id.backButton);
        menuRecyclerView = findViewById(R.id.MenuRecyclerView);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        retrieveMenuItem();
        backButton.setOnClickListener(v -> finish());
    }
    private void retrieveMenuItem() {
        DatabaseReference foodRef = database.getReference("menu");

        foodRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                menuItems.clear();

                for (DataSnapshot foodSnapshot : snapshot.getChildren()) {
                    AllMenu menuItem = foodSnapshot.getValue(AllMenu.class);
                    if (menuItem != null) {
                        menuItems.add(menuItem);
                    }
                }

                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("DatabaseError", "Error: " + error.getMessage());
            }
        });
    }

    private void setAdapter() {
        if (adapter == null) {
            adapter = new MenuItemAdapter(
                    this,
                    menuItems,
                    databaseReference,
                    position -> deleteMenuItems(position)
            );
            menuRecyclerView.setLayoutManager(
                    new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            );
            menuRecyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void deleteMenuItems(int position) {
        if (position < 0 || position >= menuItems.size()) return;

        AllMenu menuItemToDelete = menuItems.get(position);
        String menuItemKey = menuItemToDelete.getKey();

        if (menuItemKey == null || menuItemKey.isEmpty()) {
            Toast.makeText(this, "Item key not found", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference foodMenuReference = database.getReference("menu").child(menuItemKey);

        foodMenuReference.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                menuItems.remove(position);
                adapter.notifyItemRemoved(position);
            } else {
                Toast.makeText(this, "Item not Deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
