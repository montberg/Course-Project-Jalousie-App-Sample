package com.example.jaluziapp;

import java.util.ArrayList;

class CartClass {
    private static ArrayList<OrderedProduct> cart = new ArrayList<>();

    public static void addToCart(OrderedProduct p){
        cart.add(p);
    }
    public static void removeFromCart(int pos){
        cart.remove(pos);
    }
    public static ArrayList<OrderedProduct> getCart(){
        return cart;
    }
    public static void cleanCart(){
        cart.clear();
    }
    public static int getPrice(){
        if (cart.size()!=0){
        int c = 0;
        for(int i = 0; i<cart.size(); i++)
        {
            c+=cart.get(i).price;
        }
        return c;
        }
        else return 0;
    }
    public static void setProduct(int index, OrderedProduct p){
        getCart().set(index, p);
    }
}