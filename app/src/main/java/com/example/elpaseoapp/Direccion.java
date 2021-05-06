package com.example.elpaseoapp;

import java.util.Objects;

public class Direccion {
    private String street;
    private String number;
    private String description;
    private String floor;
    private String apartment;

    public Direccion() {}

    public Direccion(String street, String number, String description) {
        this.street = street;
        this.number = number;
        this.description = description;
    }

    public Direccion(String street, String number, String description, String floor, String apartment) {
        this.street = street;
        this.number = number;
        this.description = description;
        this.floor = floor;
        this.apartment = apartment;
    }

    public String getStreet() {
        return street;
    }

    public String getNumber() {
        return number;
    }

    public String getDescription() {
        return description;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Direccion direccion = (Direccion) o;
        return Objects.equals(street, direccion.street) &&
                Objects.equals(number, direccion.number) &&
                Objects.equals(description, direccion.description) &&
                Objects.equals(floor, direccion.floor) &&
                Objects.equals(apartment, direccion.apartment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, number, description, floor, apartment);
    }
}
