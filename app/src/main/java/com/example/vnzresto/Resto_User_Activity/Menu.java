package com.example.vnzresto.Resto_User_Activity;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vnzresto.Adapter.MenuAdapter;
import com.example.vnzresto.Model.MenuItem;
import com.example.vnzresto.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Menu extends AppCompatActivity {

    private RecyclerView menuRecyclerView;
    private ArrayList<MenuItem> menuItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        menuRecyclerView = findViewById(R.id.menuRecyclerView);
        menuRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ImageButton buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> finish());

        loadMenuItems();
    }

    private void loadMenuItems() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("menu");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot itemSnap : snapshot.getChildren()) {
                    MenuItem item = itemSnap.getValue(MenuItem.class);
                    if (item != null) {
                        menuItems.add(item);
                    }
                }

                MenuAdapter adapter = new MenuAdapter(Menu.this, menuItems);
                menuRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }
}
