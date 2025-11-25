package com.example.vnzresto.Model;

public class AllMenu {
    private String key, foodName, foodPrice, foodDescription, foodImage,foodIngredient;

    public AllMenu() {
    }

    public AllMenu(String key, String foodName, String foodPrice, String foodDescription, String foodImage, String foodIngredient) {
        this.key = key;
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodDescription = foodDescription;
        this.foodImage = foodImage;
        this.foodIngredient = foodIngredient;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }

    public String getFoodDescription() {
        return foodDescription;
    }

    public void setFoodDescription(String foodDescription) {
        this.foodDescription = foodDescription;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public String getFoodIngredient() {
        return foodIngredient;
    }

    public void setFoodIngredient(String foodIngredient) {
        this.foodIngredient = foodIngredient;
    }
}
