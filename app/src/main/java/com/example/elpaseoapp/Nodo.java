package com.example.elpaseoapp;

public class Nodo {

    private int id;
    private String name;
    private Direccion address;
    private Image image;
    private String description;
    private String phone;
    private Boolean hasFridge;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Direccion getAddress() {
        return address;
    }

    public void setAddress(Direccion address) {
        this.address = address;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getHasFridge() {
        return hasFridge;
    }

    public void setHasFridge(Boolean hasFridge) {
        this.hasFridge = hasFridge;
    }

}
