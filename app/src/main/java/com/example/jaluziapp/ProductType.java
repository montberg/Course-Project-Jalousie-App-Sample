package com.example.jaluziapp;

import android.media.Image;

public class ProductType {
    int preview;
    String name;
    String price;
    public ProductType(int preview, String name, String price){
        this.preview = preview;
        this.name = name;
        this.price = price;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getPreview() {
        return preview;
    }
    public String getPrice() {
        return price;
    }
}
