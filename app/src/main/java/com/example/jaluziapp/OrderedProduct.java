package com.example.jaluziapp;

import java.util.Date;

public class OrderedProduct{
    int productID;
    int width;
    int height;
    Date date;
    int price;
    String image;
    String name;
    Double type_price_multiplier;
    public OrderedProduct(int productID, int width, int height, Date date, int price, String image, String name, Double type_price_multiplier){
        this.productID = productID;
        this.width = width;
        this.height = height;
        this.date = date;
        this.price = price;
        this.image = image;
        this.name = name;
        this.type_price_multiplier = type_price_multiplier;
    }
    public OrderedProduct(){
        this.productID = -1;
        this.width = -1;
        this.height = -1;
        this.date = null;
        this.price = 0;
        this.image = null;
        this.name = null;
    }
}