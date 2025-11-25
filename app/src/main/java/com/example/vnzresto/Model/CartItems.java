package com.example.vnzresto.Model;

public class CartItems {
    private String food_name,food_price,food_Descrip,food_img,food_ingd;
    private Integer foodQuantity;

    public CartItems() {
    }

    public CartItems(String food_name, String food_price, String food_Descrip, String food_img, String food_ingd, Integer foodQuantity) {
        this.food_name = food_name;
        this.food_price = food_price;
        this.food_Descrip = food_Descrip;
        this.food_img = food_img;
        this.food_ingd = food_ingd;
        this.foodQuantity = foodQuantity;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getFood_price() {
        return food_price;
    }

    public void setFood_price(String food_price) {
        this.food_price = food_price;
    }

    public String getFood_Descrip() {
        return food_Descrip;
    }

    public void setFood_Descrip(String food_Descrip) {
        this.food_Descrip = food_Descrip;
    }

    public String getFood_img() {
        return food_img;
    }

    public void setFood_img(String food_img) {
        this.food_img = food_img;
    }

    public String getFood_ingd() {
        return food_ingd;
    }

    public void setFood_ingd(String food_ingd) {
        this.food_ingd = food_ingd;
    }

    public Integer getFoodQuantity() {
        return foodQuantity;
    }

    public void setFoodQuantity(Integer foodQuantity) {
        this.foodQuantity = foodQuantity;
    }
}
