package com.example.vnzresto.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vnzresto.Adapter.BuyAgainAdapter;
import com.example.vnzresto.Model.OrderDetailsModel;
import com.example.vnzresto.R;
import com.example.vnzresto.Resto_User_Activity.RecentOrderItems;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private ImageView buyAgainFoodImage;
    private TextView buyAgainFoodName;
    private TextView buyAgainFoodPrice;
    private CardView orderdStutus;
    private Button receivedButton;
    private RecyclerView buyAgainRecyclerView;
    private View recentBuyItemLayout;

    private BuyAgainAdapter buyAgainAdapter;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private String userId;
    private ArrayList<OrderDetailsModel> listOfOrderItem = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        buyAgainFoodImage = view.findViewById(R.id.buyAgainFoodImage);
        buyAgainFoodName = view.findViewById(R.id.buyAgainFoodName);
        buyAgainFoodPrice = view.findViewById(R.id.buyAgainFoodPrice);
        orderdStutus = view.findViewById(R.id.orderdStutus);
        receivedButton = view.findViewById(R.id.receivedButton);
        buyAgainRecyclerView = view.findViewById(R.id.BuyAgainRecyclerView);
        recentBuyItemLayout = view.findViewById(R.id.recentbuyitem);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        retrieveBuyHistory();

        recentBuyItemLayout.setOnClickListener(v -> ItemsRecentBuy());
        receivedButton.setOnClickListener(v -> updateStatus());

        return view;
    }

    private void updateStatus() {
        if (listOfOrderItem.isEmpty()) return;

        OrderDetailsModel recentOrder = listOfOrderItem.get(0);
        String itemPushKey = recentOrder.getItemPushKey();
        if (itemPushKey == null) return;

        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;
        if (userId == null) return;

        DatabaseReference root = database.getReference();

        root.child("CompletedOrder").child(itemPushKey).child("orderReceived").setValue(true);
        root.child("user").child(userId).child("BuyHistory").child(itemPushKey).child("orderReceived").setValue(true);

        Toast.makeText(getContext(), "Thank you for your order ", Toast.LENGTH_SHORT).show();
        retrieveBuyHistory();
    }

    private void ItemsRecentBuy() {
        if (listOfOrderItem.isEmpty()) return;

        Intent intent = new Intent(getActivity(), RecentOrderItems.class);
        intent.putExtra("RecentBuyOrderItem", listOfOrderItem);
        startActivity(intent);
    }
    private void retrieveBuyHistory() {
        if (auth.getCurrentUser() == null) return;
        userId = auth.getCurrentUser().getUid();
        DatabaseReference buyItemReference = database.getReference("user").child(userId).child("BuyHistory");
        buyItemReference.orderByChild("currentTime").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listOfOrderItem.clear();

                        for (DataSnapshot buySnapshot : snapshot.getChildren()) {
                            OrderDetailsModel buyHistoryItem = buySnapshot.getValue(OrderDetailsModel.class);
                            if (buyHistoryItem != null) listOfOrderItem.add(buyHistoryItem);
                        }
                        java.util.Collections.reverse(listOfOrderItem);

                        if (!listOfOrderItem.isEmpty()) {
                            RecentAndOlditems();
                        } else {
                            recentBuyItemLayout.setVisibility(View.GONE);
                            buyAgainRecyclerView.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
    }

    private void RecentAndOlditems() {
        OrderDetailsModel currentOrder = null;

        for (OrderDetailsModel order : listOfOrderItem) {
            if (!order.isOrderReceived()) {
                currentOrder = order;
                break;
            }
        }

        if (currentOrder == null) {
            recentBuyItemLayout.setVisibility(View.GONE);
            previousBuyRecyclerView(listOfOrderItem);
        } else {
            recentBuyItemLayout.setVisibility(View.VISIBLE);
            RecentBuyItem(currentOrder);

            ArrayList<OrderDetailsModel> oldOrders = new ArrayList<>(listOfOrderItem);
            oldOrders.remove(currentOrder);
            previousBuyRecyclerView(oldOrders);
        }
    }

    private void RecentBuyItem(OrderDetailsModel recentOrderItem) {
        String firstName = "";
        String firstPrice = "";
        String firstImage = "";

        if (recentOrderItem.getFoodNames() != null && !recentOrderItem.getFoodNames().isEmpty())
            firstName = recentOrderItem.getFoodNames().get(0);

        if (recentOrderItem.getFoodPrices() != null && !recentOrderItem.getFoodPrices().isEmpty())
            firstPrice = recentOrderItem.getFoodPrices().get(0);

        if (recentOrderItem.getFoodImages() != null && !recentOrderItem.getFoodImages().isEmpty())
            firstImage = recentOrderItem.getFoodImages().get(0);

        buyAgainFoodName.setText(firstName);
        buyAgainFoodPrice.setText(firstPrice);

        if (firstImage != null && !firstImage.isEmpty()) {
            try {
                Uri uri = Uri.parse(firstImage);
                buyAgainFoodImage.setImageURI(uri);
            } catch (Exception e) {
                buyAgainFoodImage.setImageResource(R.drawable.menu2);
            }
        } else {
            buyAgainFoodImage.setImageResource(R.drawable.menu2);
        }

        boolean isOrderAccepted = recentOrderItem.isOrderAccepted();
        boolean isOrderReceived = recentOrderItem.isOrderReceived();

        if (isOrderAccepted && !isOrderReceived) {
            orderdStutus.getBackground().setTint(Color.GREEN);
            receivedButton.setVisibility(View.VISIBLE);
        } else {
            receivedButton.setVisibility(View.INVISIBLE);
        }
    }

    private void previousBuyRecyclerView(List<OrderDetailsModel> orders) {
        List<String> names = new ArrayList<>();
        List<String> prices = new ArrayList<>();
        List<String> images = new ArrayList<>();

        for (OrderDetailsModel order : orders) {
            if (order.getFoodNames() != null && !order.getFoodNames().isEmpty())
                names.add(order.getFoodNames().get(0));
            if (order.getFoodPrices() != null && !order.getFoodPrices().isEmpty())
                prices.add(order.getFoodPrices().get(0));
            if (order.getFoodImages() != null && !order.getFoodImages().isEmpty())
                images.add(order.getFoodImages().get(0));
        }

        buyAgainRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false)
        );

        buyAgainAdapter = new BuyAgainAdapter(getContext(), names, prices, images);
        buyAgainRecyclerView.setAdapter(buyAgainAdapter);
    }
}
