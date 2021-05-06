package com.example.elpaseoapp;

import java.util.ArrayList;

public class Productor {
    private int id;
    private String name;
    private String lastName;
    private String email;
    private String description;
    private String phone;
    private Producto[] products;
    private Direccion address;
    private ArrayList<Image> images;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getDescription() {
        return description;
    }

    public String getPhone() {
        return phone;
    }

    public Producto[] getProducts() {
        return products;
    }

    public Direccion getAddress() {
        return address;
    }
}
