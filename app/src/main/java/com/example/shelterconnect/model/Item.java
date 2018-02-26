package com.example.shelterconnect.model;

/**
 * Item object for model
 * Created by daniel on 2/12/18.
 */

public class Item {

    private int itemID;
    private String name;
    private double price;

    public Item(int itemID, String name, double price){
        if(itemID < 0 || name == null || price < 0.0){
            throw new IllegalArgumentException("Invalid input");
        }

        this.itemID = itemID;
        this.name = name;
        this.price = price;
    }

    public int getItemID(){
        return itemID;
    }

    public void setItemID(int itemID){
        this.itemID = itemID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
