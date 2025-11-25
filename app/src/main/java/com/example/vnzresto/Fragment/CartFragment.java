package com.example.vnzresto.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.vnzresto.Adapter.CartAdapter;
import com.example.vnzresto.Model.CartItems;
import com.example.vnzresto.R;
import com.example.vnzresto.Resto_User_Activity.PayOutActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class CartFragment extends Fragment {

    private RecyclerView cartRecyclerView;
    private Button proceedButton;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private String userId;

    private ArrayList<CartItems> cartList = new ArrayList<>();

    private CartAdapter cartAdapter;
    public CartFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        cartRecyclerView = view.findViewById(R.id.cartRecyclerView);
        proceedButton = view.findViewById(R.id.proceedButton);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
       takeCartItems();
        proceedButton.setOnClickListener(v -> getOrderDetail());
        return view;
    }

    private void takeCartItems() {
        if (auth.getCurrentUser() == null) {
            if (getContext() != null) {
                Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        userId = auth.getCurrentUser().getUid();

        DatabaseReference foodReference = database.getReference("user").child(userId).child("CartItems");
        cartList.clear();
        foodReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartList.clear();
                for (DataSnapshot foodSnapshot : snapshot.getChildren()) {
                    CartItems cartItems = foodSnapshot.getValue(CartItems.class);
                    if (cartItems != null) {
                        cartList.add(cartItems);
                    }
                }

                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Data not fetched", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setAdapter() {
        if (getContext() == null) return;

        cartAdapter = new CartAdapter(getContext(), cartList);

        cartRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false)
        );
        cartRecyclerView.setAdapter(cartAdapter);
    }

    private void getOrderDetail() {
        if (cartList.isEmpty()) {
            if (getContext() != null) {
                Toast.makeText(getContext(), "Cart is empty", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        List<String> foodName = new ArrayList<>();
        List<String> foodPrice = new ArrayList<>();
        List<String> foodImage = new ArrayList<>();
        List<String> foodDescription = new ArrayList<>();
        List<String> foodIngredient = new ArrayList<>();
        List<Integer> foodQuantities = new ArrayList<>();

        for (CartItems item : cartList) {
            if (item.getFood_name() != null) {
                foodName.add(item.getFood_name());
            } else {
                foodName.add("");
            }

            if (item.getFood_price() != null) {
                foodPrice.add(item.getFood_price());
            } else {
                foodPrice.add("0");
            }

            if (item.getFood_img() != null) {
                foodImage.add(item.getFood_img());
            } else {
                foodImage.add("");
            }
            if (item.getFood_Descrip() != null) {
                foodDescription.add(item.getFood_Descrip());
            } else {
                foodDescription.add("");
            }

            if (item.getFood_ingd() != null) {
                foodIngredient.add(item.getFood_ingd());
            } else {
                foodIngredient.add("");
            }

            foodQuantities.add(item.getFoodQuantity() != null ? item.getFoodQuantity() : 1);
        }

        orderNow(foodName, foodPrice, foodDescription, foodImage, foodIngredient, foodQuantities);
    }
    private void orderNow(List<String> foodName, List<String> foodPrice, List<String> foodDescription, List<String> foodImage, List<String> foodIngredient, List<Integer> foodQuantities) {

        if (getActivity() == null) return;

        Intent intent = new Intent(getActivity(), PayOutActivity.class);
        intent.putStringArrayListExtra("FoodItemName", new ArrayList<>(foodName));
        intent.putStringArrayListExtra("FoodItemPrice", new ArrayList<>(foodPrice));
        intent.putStringArrayListExtra("FoodItemImage", new ArrayList<>(foodImage));
        intent.putStringArrayListExtra("FoodItemDescription", new ArrayList<>(foodDescription));
        intent.putStringArrayListExtra("FoodItemIngredient", new ArrayList<>(foodIngredient));
        intent.putIntegerArrayListExtra("FoodItemQuantities", new ArrayList<>(foodQuantities));
        startActivity(intent);
    }
}
