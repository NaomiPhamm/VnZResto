package com.example.vnzresto.Resto_User_Activity;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vnzresto.Adapter.RecentBuyAdapter;
import com.example.vnzresto.Model.OrderDetailsModel;
import com.example.vnzresto.R;

import java.util.ArrayList;

public class RecentOrderItems extends AppCompatActivity {

    private ImageButton backButton;
    private RecyclerView recyclerViewRecentBuy;

    private ArrayList<String> allFoodNames = new ArrayList<>();
    private ArrayList<String> allFoodImages = new ArrayList<>();
    private ArrayList<String> allFoodPrices = new ArrayList<>();
    private ArrayList<Integer> allFoodQuantities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_order_items);

        backButton = findViewById(R.id.backButton);
        recyclerViewRecentBuy = findViewById(R.id.recyclerViewRecentBuy);

        backButton.setOnClickListener(v -> finish());

        ArrayList<OrderDetailsModel> recentOrderItems =
                (ArrayList<OrderDetailsModel>) getIntent().getSerializableExtra("RecentBuyOrderItem");

        if (recentOrderItems != null && !recentOrderItems.isEmpty()) {
            OrderDetailsModel recentOrderItem = recentOrderItems.get(0);

            if (recentOrderItem.getFoodNames() != null) {
                allFoodNames.addAll(recentOrderItem.getFoodNames());
            }
            if (recentOrderItem.getFoodImages() != null) {
                allFoodImages.addAll(recentOrderItem.getFoodImages());
            }
            if (recentOrderItem.getFoodPrices() != null) {
                allFoodPrices.addAll(recentOrderItem.getFoodPrices());
            }
            if (recentOrderItem.getFoodQuantities() != null) {
                allFoodQuantities.addAll(recentOrderItem.getFoodQuantities());
            }
        }

        setAdapter();
    }

    private void setAdapter() {
        recyclerViewRecentBuy.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );

        RecentBuyAdapter adapter = new RecentBuyAdapter(
                this,
                allFoodNames,
                allFoodImages,
                allFoodPrices,
                allFoodQuantities
        );
        recyclerViewRecentBuy.setAdapter(adapter);
    }
}
