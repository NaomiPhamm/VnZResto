package com.example.vnzresto.Resto_User_Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.vnzresto.Model.OrderDetailsModel;
import com.example.vnzresto.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PayOutActivity extends AppCompatActivity {

    // UI
    private ImageButton backeButton;
    private EditText nameEditText;
    private EditText addressEditText;
    private EditText phoneEditText;
    private EditText totalAmountEditText;
    private Button placeMyOrderButton;

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    private String name;
    private String address;
    private String phone;
    private String totalAmount;
    private String userId;

    private ArrayList<String> foodItemName;
    private ArrayList<String> foodItemPrice;
    private ArrayList<String> foodItemImage;
    private ArrayList<String> foodItemDescription;
    private ArrayList<String> foodItemIngredient;
    private ArrayList<Integer> foodItemQuantities;

    private String PublishableKey = "";
    private String SecretKey = "";

    private String CustomersURL = "https://api.stripe.com/v1/customers";
    private String EphemeralKeyURL = "https://api.stripe.com/v1/ephemeral_keys";
    private String ClientSecretURL = "https://api.stripe.com/v1/payment_intents";

    private String CustomerId = null;
    private String EphemeralKey;
    private String ClientSecret;
    private PaymentSheet paymentSheet;

    private String AmountCents = "";
    private String Currency = "usd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_out);
        backeButton = findViewById(R.id.backeButton);
        nameEditText = findViewById(R.id.name);
        addressEditText = findViewById(R.id.address);
        phoneEditText = findViewById(R.id.phone);
        totalAmountEditText = findViewById(R.id.totalAmount);
        placeMyOrderButton = findViewById(R.id.PlaceMyOrder);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        setUserData();

        Intent intent = getIntent();

        foodItemName = intent.getStringArrayListExtra("FoodItemName");
        foodItemPrice = intent.getStringArrayListExtra("FoodItemPrice");
        foodItemImage = intent.getStringArrayListExtra("FoodItemImage");
        foodItemDescription = intent.getStringArrayListExtra("FoodItemDescription");
        foodItemIngredient = intent.getStringArrayListExtra("FoodItemIngredient");
        foodItemQuantities = intent.getIntegerArrayListExtra("FoodItemQuantities");

        if (foodItemName == null) foodItemName = new ArrayList<>();
        if (foodItemPrice == null) foodItemPrice = new ArrayList<>();
        if (foodItemImage == null) foodItemImage = new ArrayList<>();
        if (foodItemDescription == null) foodItemDescription = new ArrayList<>();
        if (foodItemIngredient == null) foodItemIngredient = new ArrayList<>();
        if (foodItemQuantities == null) foodItemQuantities = new ArrayList<>();
        int totalInt = calculateTotalAmount();
        totalAmount = totalInt + "$";
        totalAmountEditText.setText(totalAmount);
        totalAmountEditText.setEnabled(false);

        int amountInCents = totalInt * 100;
        AmountCents = String.valueOf(amountInCents);

        PaymentConfiguration.init(this, PublishableKey);
        paymentSheet = new PaymentSheet(this, this::onPaymentResult);
        createCustomer();
        backeButton.setOnClickListener(v -> finish());
        placeMyOrderButton.setOnClickListener(v -> {
            name = nameEditText.getText().toString().trim();
            address = addressEditText.getText().toString().trim();
            phone = phoneEditText.getText().toString().trim();

            if (TextUtils.isEmpty(name) ||
                    TextUtils.isEmpty(address) ||
                    TextUtils.isEmpty(phone)) {

                Toast.makeText(PayOutActivity.this,
                        "Please enter all the details ",
                        Toast.LENGTH_SHORT).show();
            } else {

                if (CustomerId != null && ClientSecret != null && !ClientSecret.isEmpty()) {
                    paymentFlow();
                } else {
                    Toast.makeText(PayOutActivity.this,
                            "Payment is not ready yet, please wait a moment.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void createCustomer() {
        StringRequest request = new StringRequest(Request.Method.POST, CustomersURL,
                response -> {
                    try {
                        JSONObject object = new JSONObject(response);
                        CustomerId = object.getString("id");
                        if (CustomerId != null && !CustomerId.isEmpty()) {
                            getEphemeralKey();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(PayOutActivity.this,
                                "Error creating customer: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(PayOutActivity.this,
                        "Error creating customer: " + error.getLocalizedMessage(),
                        Toast.LENGTH_SHORT).show()
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + SecretKey);
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void getEphemeralKey() {
        StringRequest request = new StringRequest(Request.Method.POST, EphemeralKeyURL,
                response -> {
                    try {
                        JSONObject object = new JSONObject(response);
                        EphemeralKey = object.getString("id");

                        if (EphemeralKey != null && !EphemeralKey.isEmpty()) {
                            getClientSecret(CustomerId, EphemeralKey);
                        } else {
                            Toast.makeText(PayOutActivity.this,
                                    "Failed to fetch ephemeral key",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(PayOutActivity.this,
                                "Error fetching ephemeral key: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(PayOutActivity.this,
                        "Error fetching ephemeral key: " + error.getLocalizedMessage(),
                        Toast.LENGTH_SHORT).show()
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + SecretKey);
                headers.put("Stripe-Version", "2022-11-15");
                return headers;
            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", CustomerId);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void getClientSecret(String customerId, String ephemeralKey) {
        StringRequest request = new StringRequest(Request.Method.POST, ClientSecretURL,
                response -> {
                    try {
                        JSONObject object = new JSONObject(response);
                        ClientSecret = object.getString("client_secret");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(PayOutActivity.this,
                                "Error fetching client secret: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(PayOutActivity.this,
                        "Error fetching client secret: " + error.getLocalizedMessage(),
                        Toast.LENGTH_SHORT).show()
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + SecretKey);
                return headers;
            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", customerId);
                params.put("amount", AmountCents);
                params.put("currency", Currency);
                params.put("automatic_payment_methods[enabled]", "true");
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void paymentFlow() {
        if (ClientSecret != null && !ClientSecret.isEmpty()) {
            paymentSheet.presentWithPaymentIntent(
                    ClientSecret,
                    new PaymentSheet.Configuration(
                            "Stripe Demo",
                            new PaymentSheet.CustomerConfiguration(
                                    CustomerId,
                                    EphemeralKey
                            )
                    )
            );
        } else {
            Toast.makeText(PayOutActivity.this,
                    "Client Secret not available",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show();
            // Payment thành công → đặt hàng vào Firebase
            placeOrder();
        } else {
            Toast.makeText(this, "Payment Failed or Canceled", Toast.LENGTH_SHORT).show();
        }
    }


    private void placeOrder() {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            return;
        }

        userId = user.getUid();
        long time = System.currentTimeMillis();

        String itemPushKey = databaseReference.child("OrderDetails").push().getKey();
        if (itemPushKey == null) {
            Toast.makeText(this, "Error creating order key", Toast.LENGTH_SHORT).show();
            return;
        }

        OrderDetailsModel orderDetails = new OrderDetailsModel();
        orderDetails.setUserUid(userId);
        orderDetails.setUserName(name);
        orderDetails.setFoodNames(foodItemName);
        orderDetails.setFoodPrices(foodItemPrice);
        orderDetails.setFoodImages(foodItemImage);
        orderDetails.setFoodQuantities(foodItemQuantities);
        orderDetails.setAddress(address);
        orderDetails.setTotalPrice(totalAmount);
        orderDetails.setPhoneNumber(phone);
        orderDetails.setItemPushKey(itemPushKey);
        orderDetails.setOrderAccepted(false);
        orderDetails.setPaymentReceived(true);

        DatabaseReference orderReference =
                databaseReference.child("OrderDetails").child(itemPushKey);

        orderReference.setValue(orderDetails)
                .addOnSuccessListener(unused -> {
                    Intent intent = new Intent(PayOutActivity.this, CongratsActivity.class);
                    startActivity(intent);
                    finish();

                    removeItemFromCart();
                    addOrderToHistory(orderDetails);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(PayOutActivity.this,
                                "Failed to save order ",
                                Toast.LENGTH_SHORT).show()
                );
    }

    private void addOrderToHistory(OrderDetailsModel orderDetails) {
        if (orderDetails.getItemPushKey() == null) return;

        databaseReference.child("user")
                .child(userId)
                .child("BuyHistory")
                .child(orderDetails.getItemPushKey())
                .setValue(orderDetails)
                .addOnSuccessListener(unused -> {
                });
    }

    private void removeItemFromCart() {
        databaseReference.child("user")
                .child(userId)
                .child("CartItems")
                .removeValue();
    }

    private int calculateTotalAmount() {
        int total = 0;

        for (int i = 0; i < foodItemPrice.size() && i < foodItemQuantities.size(); i++) {
            String price = foodItemPrice.get(i);
            if (price == null) continue;
            price = price.trim();
            if (price.isEmpty()) continue;

            char lastChar = price.charAt(price.length() - 1);
            int priceIntValue;

            try {
                if (lastChar == '$') {
                    priceIntValue = Integer.parseInt(price.substring(0, price.length() - 1));
                } else {
                    priceIntValue = Integer.parseInt(price);
                }
            } catch (NumberFormatException e) {
                priceIntValue = 0;
            }

            int quantity = foodItemQuantities.get(i) != null ? foodItemQuantities.get(i) : 0;
            total += priceIntValue * quantity;
        }

        return total;
    }

    private void setUserData() {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) return;

        String userId = user.getUid();
        DatabaseReference userReference = databaseReference.child("user").child(userId);

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    String names = snapshot.child("name").getValue(String.class);
                    String addresses = snapshot.child("address").getValue(String.class);
                    String phones = snapshot.child("phone").getValue(String.class);

                    if (names == null) names = "";
                    if (addresses == null) addresses = "";
                    if (phones == null) phones = "";

                    nameEditText.setText(names);
                    addressEditText.setText(addresses);
                    phoneEditText.setText(phones);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
