package com.example.jaluziapp;

import java.util.Date;

public class RegistrationInfo {
    String name;
    String city;
    String street;
    int house;
    int address_index;
    int passport_id;
    int passport_serie;
    String date_of_issue;
    String date_of_birth;
    String login;
    String password;
    public RegistrationInfo(String name,
            String city,
            String street,
            int house,
            int address_index,
            int passport_id,
            int passport_serie,
            String date_of_issue,
            String date_of_birth,
            String login,
            String password){
        this.name = name;
        this.city = city;
        this.street = street;
        this.house = house;
        this.address_index = address_index;
        this.passport_id = passport_id;
        this.passport_serie = passport_serie;
        this.date_of_birth = date_of_birth;
        this.date_of_issue = date_of_issue;
        this.login = login;
        this.password = GlobalClass.getHash(password);
    }
}
