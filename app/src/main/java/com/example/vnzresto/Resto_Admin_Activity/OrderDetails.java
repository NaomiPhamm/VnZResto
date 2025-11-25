package com.example.vnzresto.Resto_Admin_Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vnzresto.Adapter.OrderDetailsAdapter;
import com.example.vnzresto.Model.OrderDetailsModel;
import com.example.vnzresto.R;

import java.util.ArrayList;

public class OrderDetails extends AppCompatActivity {

    private ImageButton backeButton;
    private TextView nameTextView;
    private TextView addressTextView;
    private TextView phoneTextView;
    private TextView totalPayTextView;
    private RecyclerView orderDetailRecyclerVew;

    private String userName;
    private String address;
    private String phoneNumber;
    private String totalPrice;
    private ArrayList<String> foodNames = new ArrayList<>();
    private ArrayList<String> foodImages = new ArrayList<>();
    private ArrayList<Integer> foodQuantity = new ArrayList<>();
    private ArrayList<String> foodPrices = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        initViews();
        backeButton.setOnClickListener(v -> finish());

        getDataFromIntent();
    }

    private void initViews() {
        backeButton = findViewById(R.id.backeButton);
        nameTextView = findViewById(R.id.name);
        addressTextView = findViewById(R.id.address);
        phoneTextView = findViewById(R.id.phone);
        totalPayTextView = findViewById(R.id.totalPay);
        orderDetailRecyclerVew = findViewById(R.id.orderDetailRecyclerVew);
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        OrderDetailsModel receivedOrderDetails =
                (OrderDetailsModel) intent.getSerializableExtra("UserOrderDetails");

        if (receivedOrderDetails != null) {
            userName = receivedOrderDetails.getUserName();
            address = receivedOrderDetails.getAddress();
            phoneNumber = receivedOrderDetails.getPhoneNumber();
            totalPrice = receivedOrderDetails.getTotalPrice();

            foodNames = new ArrayList<>(receivedOrderDetails.getFoodNames());
            foodImages = new ArrayList<>(receivedOrderDetails.getFoodImages());
            foodQuantity = new ArrayList<>(receivedOrderDetails.getFoodQuantities());
            foodPrices = new ArrayList<>(receivedOrderDetails.getFoodPrices());

            setUserDetail();
            setAdapter();
        }
    }

    private void setUserDetail() {
        nameTextView.setText(userName);
        addressTextView.setText(address);
        phoneTextView.setText(phoneNumber);
        totalPayTextView.setText(totalPrice);
    }

    private void setAdapter() {
        orderDetailRecyclerVew.setLayoutManager(new LinearLayoutManager(this));
        OrderDetailsAdapter adapter =
                new OrderDetailsAdapter(this, foodNames, foodImages, foodQuantity, foodPrices);
        orderDetailRecyclerVew.setAdapter(adapter);
    }
}