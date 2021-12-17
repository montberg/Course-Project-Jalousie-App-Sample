package com.example.jaluziapp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Date;
@JsonPropertyOrder({ "product", "width", "height","price" })
public class OrderedProduct{
    @JsonProperty
    int product;
    @JsonProperty
    int width;
    @JsonProperty
    int height;
    @JsonProperty
    int price;
    String image;
    String name;
    Double type_price_multiplier;
    public OrderedProduct(int productID, int width, int height, int price, String image, String name, Double type_price_multiplier){
        this.product = productID;
        this.width = width;
        this.height = height;
        this.price = price;
        this.image = image;
        this.name = name;
        this.type_price_multiplier = type_price_multiplier;
    }

}

