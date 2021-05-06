package com.example.elpaseoapp;

import java.util.ArrayList;
import java.util.Objects;

public class Producto {

    private int id;
    private String brand;
    private String description;
    private float price;
    private ArrayList<Categoria> categories;
    private String title;
    private Unidad unit;
    private int stock;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public ArrayList<Categoria> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Categoria> categorias) {
        this.categories = categorias;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Unidad getUnit() {
        return unit;
    }

    public void setUnit(Unidad unit) {
        this.unit = unit;
    }

    public int getStock(){ return this.stock;}

    public void setStock(int stock){
        this.stock=stock;
    }

    @Override
    public boolean equals(Object o) {
        // equals por ID
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return id == producto.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
