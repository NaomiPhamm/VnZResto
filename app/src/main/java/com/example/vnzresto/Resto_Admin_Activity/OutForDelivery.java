package com.example.vnzresto.Resto_Admin_Activity;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vnzresto.Adapter.DeliveryAdapter;
import com.example.vnzresto.Model.OrderDetailsModel;
import com.example.vnzresto.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OutForDelivery extends AppCompatActivity {

    private ImageButton backButton;
    private RecyclerView deliveryRecyclerView;

    private FirebaseDatabase database;
    private ArrayList<OrderDetailsModel> listOfCompleteOrderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_for_delivery);

        backButton = findViewById(R.id.backButton);
        deliveryRecyclerView = findViewById(R.id.deliveryRecyclerView);

        database = FirebaseDatabase.getInstance();

        backButton.setOnClickListener(v -> finish());

        completeOrderDetail();
    }

    private void completeOrderDetail() {

        Query completeOrderReference = database.getReference("CompletedOrder")
                .orderByChild("currentTime");

        completeOrderReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listOfCompleteOrderList.clear();

                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    OrderDetailsModel completeOrder = orderSnapshot.getValue(OrderDetailsModel.class);
                    if (completeOrder != null) {
                        listOfCompleteOrderList.add(completeOrder);
                    }
                }

                Collections.reverse(listOfCompleteOrderList);

                setDataIntoRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void setDataIntoRecyclerView() {

        List<String> customerName = new ArrayList<>();
        List<Boolean> moneyStatus = new ArrayList<>();

        for (OrderDetailsModel order : listOfCompleteOrderList) {
            if (order.getUserName() != null) {
                customerName.add(order.getUserName());
            } else {
                customerName.add("");
            }

            moneyStatus.add(order.isPaymentReceived());
        }

        DeliveryAdapter adapter = new DeliveryAdapter(this, customerName, moneyStatus);
        deliveryRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );
        deliveryRecyclerView.setAdapter(adapter);
    }
}