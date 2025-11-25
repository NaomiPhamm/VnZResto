package com.example.vnzresto.Resto_Admin_Activity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vnzresto.Adapter.EarningAdapter;
import com.example.vnzresto.Model.OrderDetailsModel;
import com.example.vnzresto.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EarningDetails extends AppCompatActivity {

    private TextView totalEarningTextView;
    private RecyclerView earningRecyclerView;
    private EarningAdapter earningAdapter;
    private ImageButton backButton;

    private DatabaseReference completedOrderReference;
    private ArrayList<OrderDetailsModel> ordersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earning_details);
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        totalEarningTextView = findViewById(R.id.totalEarningTextView);
        earningRecyclerView = findViewById(R.id.earningRecyclerView);

        earningRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );

        earningAdapter = new EarningAdapter(this, ordersList);
        earningRecyclerView.setAdapter(earningAdapter);


        completedOrderReference = FirebaseDatabase.getInstance()
                .getReference("CompletedOrder");

        loadCompletedOrders();
    }

    private void loadCompletedOrders() {
        completedOrderReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ordersList.clear();
                int total = 0;

                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    OrderDetailsModel order = orderSnapshot.getValue(OrderDetailsModel.class);
                    if (order != null && order.getTotalPrice() != null) {
                        ordersList.add(order);

                        String priceStr = order.getTotalPrice().replace("$", "").trim();
                        try {
                            int value = Integer.parseInt(priceStr);
                            total += value;
                        } catch (NumberFormatException e) {
                        }
                    }
                }

                totalEarningTextView.setText(total + "$");
                earningAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
}