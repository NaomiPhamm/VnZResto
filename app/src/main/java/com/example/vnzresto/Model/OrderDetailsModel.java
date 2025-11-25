package com.example.vnzresto.Model;

import java.io.Serializable;
import java.util.List;

public class OrderDetailsModel implements Serializable {
    private String userUid,userName;
    private List<String> foodNames;
    private List<String> foodImages;
    private List<String> foodPrices;
    private List<Integer> foodQuantities;
    private String address;
    private String totalPrice;
    private String phoneNumber;
    private boolean orderAccepted;
    private boolean paymentReceived;
    private String itemPushKey;
    private long currentTime;
    private boolean orderReceived;

    public OrderDetailsModel() {
    }

    public OrderDetailsModel(String userUid, String userName, List<String> foodNames, List<String> foodImages, List<String> foodPrices, List<Integer> foodQuantities, String address, String totalPrice, String phoneNumber, boolean orderAccepted, boolean paymentReceived, String itemPushKey, long currentTime, boolean orderReceived) {
        this.userUid = userUid;
        this.userName = userName;
        this.foodNames = foodNames;
        this.foodImages = foodImages;
        this.foodPrices = foodPrices;
        this.foodQuantities = foodQuantities;
        this.address = address;
        this.totalPrice = totalPrice;
        this.phoneNumber = phoneNumber;
        this.orderAccepted = orderAccepted;
        this.paymentReceived = paymentReceived;
        this.itemPushKey = itemPushKey;
        this.currentTime = currentTime;
        this.orderReceived = orderReceived;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<String> getFoodNames() {
        return foodNames;
    }

    public void setFoodNames(List<String> foodNames) {
        this.foodNames = foodNames;
    }

    public List<String> getFoodImages() {
        return foodImages;
    }

    public void setFoodImages(List<String> foodImages) {
        this.foodImages = foodImages;
    }

    public List<String> getFoodPrices() {
        return foodPrices;
    }

    public void setFoodPrices(List<String> foodPrices) {
        this.foodPrices = foodPrices;
    }

    public List<Integer> getFoodQuantities() {
        return foodQuantities;
    }

    public void setFoodQuantities(List<Integer> foodQuantities) {
        this.foodQuantities = foodQuantities;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isOrderAccepted() {
        return orderAccepted;
    }

    public void setOrderAccepted(boolean orderAccepted) {
        this.orderAccepted = orderAccepted;
    }

    public boolean isPaymentReceived() {
        return paymentReceived;
    }

    public void setPaymentReceived(boolean paymentReceived) {
        this.paymentReceived = paymentReceived;
    }

    public String getItemPushKey() {
        return itemPushKey;
    }

    public void setItemPushKey(String itemPushKey) {
        this.itemPushKey = itemPushKey;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public boolean isOrderReceived() {
        return orderReceived;
    }

    public void setOrderReceived(boolean orderReceived) {
        this.orderReceived = orderReceived;
    }
}
