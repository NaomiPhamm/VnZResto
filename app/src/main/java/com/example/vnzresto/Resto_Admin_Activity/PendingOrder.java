package com.example.vnzresto.Resto_Admin_Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vnzresto.Model.OrderDetailsModel;
import com.example.vnzresto.R;
import com.example.vnzresto.Adapter.PendingOrderAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PendingOrder extends AppCompatActivity implements PendingOrderAdapter.OnItemClicked {

    private ImageButton backButton;
    private RecyclerView pendingOrderRecyclerView;

    private List<String> listOfName = new ArrayList<>();
    private List<String> listOfTotalPrice = new ArrayList<>();
    private List<String> listOfImageFirstFoodOrder = new ArrayList<>();
    private ArrayList<OrderDetailsModel> listOfOrderItem = new ArrayList<>();

    private FirebaseDatabase database;
    private DatabaseReference databaseOrderDetails;

    private PendingOrderAdapter pendingOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_order);

        backButton = findViewById(R.id.backButton);
        pendingOrderRecyclerView = findViewById(R.id.pendingOrderRecyclerView);

        database = FirebaseDatabase.getInstance();
        databaseOrderDetails = database.getReference("OrderDetails");

        getOrdersDetails();

        backButton.setOnClickListener(v -> finish());
    }

    private void getOrdersDetails() {
        databaseOrderDetails.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listOfOrderItem.clear();
                listOfName.clear();
                listOfTotalPrice.clear();
                listOfImageFirstFoodOrder.clear();

                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    OrderDetailsModel orderDetails = orderSnapshot.getValue(OrderDetailsModel.class);
                    if (orderDetails != null) {
                        listOfOrderItem.add(orderDetails);
                    }
                }
                addDataToListForRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addDataToListForRecyclerView() {
        for (OrderDetailsModel orderItem : listOfOrderItem) {

            // Tên user
            if (orderItem.getUserName() != null) {
                listOfName.add(orderItem.getUserName());
            } else {
                listOfName.add("");
            }

            if (orderItem.getTotalPrice() != null) {
                listOfTotalPrice.add(orderItem.getTotalPrice());
            } else {
                listOfTotalPrice.add("0");
            }
            String firstImage = "";
            if (orderItem.getFoodImages() != null) {
                for (String img : orderItem.getFoodImages()) {
                    if (img != null && !img.isEmpty()) {
                        firstImage = img;
                        break;
                    }
                }
            }
            listOfImageFirstFoodOrder.add(firstImage);
        }

        setAdapter();
    }

    private void setAdapter() {
        pendingOrderRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );

        pendingOrderAdapter = new PendingOrderAdapter(
                this,
                listOfName,
                listOfTotalPrice,
                listOfImageFirstFoodOrder,
                this
        );
        pendingOrderRecyclerView.setAdapter(pendingOrderAdapter);
    }

    @Override
    public void onItemClickListener(int position) {
        if (position < 0 || position >= listOfOrderItem.size()) return;

        OrderDetailsModel userOrderDetails = listOfOrderItem.get(position);

        Intent intent = new Intent(PendingOrder.this, OrderDetails.class);
        intent.putExtra("UserOrderDetails", userOrderDetails);
        startActivity(intent);
    }

    @Override
    public void onItemAcceptClickListener(int position) {
        if (position < 0 || position >= listOfOrderItem.size()) return;

        String childItemPushKey = listOfOrderItem.get(position).getItemPushKey();
        if (childItemPushKey == null) return;

        DatabaseReference clickItemOrderReference =
                database.getReference("OrderDetails").child(childItemPushKey);

        clickItemOrderReference.child("orderAccepted").setValue(true);

        updateOrderAcceptStatus(position);
    }

    @Override
    public void onItemDispatchClickListener(int position) {
        if (position < 0 || position >= listOfOrderItem.size()) return;

        String dispatchItemPushKey = listOfOrderItem.get(position).getItemPushKey();
        if (dispatchItemPushKey == null) return;

        DatabaseReference dispatchItemOrderReference =
                database.getReference("CompletedOrder").child(dispatchItemPushKey);

        dispatchItemOrderReference.setValue(listOfOrderItem.get(position))
                .addOnSuccessListener(unused -> deleteThisItemFromOrderDetails(dispatchItemPushKey));
        OrderDetailsModel order = listOfOrderItem.get(position);
        String userId = order.getUserUid();

        if (userId != null) {
            DatabaseReference notiRef = database.getReference("user")
                    .child(userId)
                    .child("Notifications")
                    .push();

            notiRef.child("text").setValue("Your order is on the way!");
            notiRef.child("image").setValue("delivery");
            notiRef.child("time").setValue(System.currentTimeMillis());
        }
    }

    private void deleteThisItemFromOrderDetails(String dispatchItemPushKey) {
        DatabaseReference orderDetailsItemsReference =
                database.getReference("OrderDetails").child(dispatchItemPushKey);

        orderDetailsItemsReference.removeValue()
                .addOnSuccessListener(unused ->
                        Toast.makeText(PendingOrder.this,
                                "Order is dispatched", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(PendingOrder.this,
                                "Order is not dispatched", Toast.LENGTH_SHORT).show());
    }

    private void updateOrderAcceptStatus(int position) {
        OrderDetailsModel order = listOfOrderItem.get(position);
        String userIdOfClickedItem = order.getUserUid();
        String pushKeyOfClickedItem = order.getItemPushKey();

        if (userIdOfClickedItem == null || pushKeyOfClickedItem == null) return;

        DatabaseReference buyHistoryReference = database.getReference("user")
                .child(userIdOfClickedItem)
                .child("BuyHistory")
                .child(pushKeyOfClickedItem);

        buyHistoryReference.child("orderAccepted").setValue(true);

        databaseOrderDetails.child(pushKeyOfClickedItem)
                .child("orderAccepted")
                .setValue(true);
        DatabaseReference notiRef = database.getReference("user")
                .child(userIdOfClickedItem)
                .child("Notifications")
                .push();

        notiRef.child("text").setValue("Your order is being prepared.");
        notiRef.child("image").setValue("prepare");
        notiRef.child("time").setValue(System.currentTimeMillis());
    }
}