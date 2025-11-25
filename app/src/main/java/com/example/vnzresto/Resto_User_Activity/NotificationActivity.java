package com.example.vnzresto.Resto_User_Activity;

import android.os.Bundle;
import android.widget.ImageButton;
import com.google.firebase.auth.FirebaseAuth;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vnzresto.Adapter.NotificationAdapter;
import com.example.vnzresto.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    private ImageButton buttonBack;
    private RecyclerView notificationRecyclerView;
    private ArrayList<String> notifications = new ArrayList<>();
    private NotificationAdapter adapter;
    private ArrayList<Integer> notificationImages = new ArrayList<>();
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        buttonBack = findViewById(R.id.buttonBack);
        notificationRecyclerView = findViewById(R.id.notificationRecyclerView);

        buttonBack.setOnClickListener(v -> finish());
        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        adapter = new NotificationAdapter(notifications, notificationImages);

        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notificationRecyclerView.setAdapter(adapter);

        loadNotifi();
    }

    private void loadNotifi() {

        DatabaseReference notiRef = FirebaseDatabase.getInstance()
                .getReference("user")
                .child(userid)
                .child("Notifications");

        notiRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                notifications.clear();
                notificationImages.clear();

                for (DataSnapshot s : snapshot.getChildren()) {
                    String text = s.child("text").getValue(String.class);
                    String img = s.child("image").getValue(String.class);

                    notifications.add(text);

                    if ("prepare".equals(img)) {
                        notificationImages.add(R.drawable.congrats);
                    } else if ("delivery".equals(img)) {
                        notificationImages.add(R.drawable.truck);
                    } else {
                        notificationImages.add(R.drawable.congrats);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }
}

