package com.example.jaluziapp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {

    @JsonProperty
    int id;
    @JsonProperty
    String name;
    @JsonProperty
    String type_name;
    @JsonProperty
    Integer max_width;
    @JsonProperty
    Integer max_height;
    @JsonProperty
    Double type_price_multiplier;
    @JsonProperty
    String material_price;
    @JsonProperty
    String image;



    public Product(int id, String name, String type_name, String maxWidth,
                   String maxHeight, String priceMultiplier,
                   String materialPrice, String preview){
        this.id = id;
        this.name = name;
        this.type_name = type_name;
        this.max_width = Integer.parseInt(maxWidth);
        this.max_height = Integer.parseInt(maxHeight);
        this.type_price_multiplier = Double.parseDouble(priceMultiplier);
        this.material_price = materialPrice;
        this.image = preview;
    }

    public Product(String name, String type_name, String maxWidth,
                   String maxHeight, String priceMultiplier,
                   String materialPrice, String preview){
        this.name = name;
        this.type_name = type_name;
        this.max_width = Integer.parseInt(maxWidth);
        this.max_height = Integer.parseInt(maxHeight);
        this.type_price_multiplier = Double.parseDouble(priceMultiplier);
        this.material_price = materialPrice;
        this.image = preview;
    }

    public Product(String name,
                   String materialPrice,
                   String preview){
        this.name = name;
        this.material_price = materialPrice;
        this.image = preview;
    }
    public Product(){}

}
