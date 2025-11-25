package com.example.vnzresto.Resto_Admin_Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.vnzresto.Login;
import com.example.vnzresto.Model.OrderDetailsModel;
import com.example.vnzresto.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminDashBoard extends AppCompatActivity {

    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private DatabaseReference completedOrderReference;
    private LinearLayout addMenu;
    private LinearLayout allItemMenu;
    private CardView outForDeliveryButton;
    private LinearLayout profile;
    private LinearLayout logoutButton;
    private TextView pendingOrderTextView;
    private TextView pendingOrders;
    private TextView completeOrders;
    private TextView wholeTimeEarning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        addMenu = findViewById(R.id.addMenu);
        allItemMenu = findViewById(R.id.allItemMenu);
        outForDeliveryButton = findViewById(R.id.outForDeliveryButton);
        profile = findViewById(R.id.profile);

        logoutButton = findViewById(R.id.logoutButton);
        pendingOrderTextView = findViewById(R.id.pendingOrderTextView);
        pendingOrders = findViewById(R.id.pendingOrders);
        completeOrders = findViewById(R.id.completeOrders);
        wholeTimeEarning = findViewById(R.id.wholeTimeEarning);


        addMenu.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashBoard.this, AddItem.class);
            startActivity(intent);
        });

        allItemMenu.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashBoard.this, AllItem.class);
            startActivity(intent);
        });


        outForDeliveryButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashBoard.this, OutForDelivery.class);
            startActivity(intent);
        });


        profile.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashBoard.this, AdminProfile.class);
            startActivity(intent);
        });
        wholeTimeEarning.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashBoard.this, EarningDetails.class);
            startActivity(intent);
        });


        pendingOrderTextView.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashBoard.this, PendingOrder.class);
            startActivity(intent);
        });


        logoutButton.setOnClickListener(v -> {
            auth.signOut();
            Intent intent = new Intent(AdminDashBoard.this, Login.class);
            startActivity(intent);
            finish();
        });

        pendingOrders();
        completedOrders();
        wholeTimeEarning();
    }

    private void pendingOrders() {
        DatabaseReference pendingOrderReference = database.getReference("OrderDetails");
        pendingOrderReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long count = snapshot.getChildrenCount();
                pendingOrders.setText(String.valueOf(count));
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void completedOrders() {
        DatabaseReference completeOrderReference = database.getReference("CompletedOrder");
        completeOrderReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long count = snapshot.getChildrenCount();
                completeOrders.setText(String.valueOf(count));
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }
    private void wholeTimeEarning() {
        completedOrderReference = FirebaseDatabase.getInstance().getReference("CompletedOrder");
        completedOrderReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int total = 0;

                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    OrderDetailsModel order = orderSnapshot.getValue(OrderDetailsModel.class);
                    if (order != null && order.getTotalPrice() != null) {
                        String priceStr = order.getTotalPrice().replace("$", "").trim();
                        try {
                            int value = Integer.parseInt(priceStr);
                            total += value;
                        } catch (NumberFormatException e) {

                        }
                    }
                }

                wholeTimeEarning.setText(total + "$");
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }
}