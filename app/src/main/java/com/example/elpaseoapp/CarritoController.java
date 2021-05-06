package com.example.elpaseoapp;

import java.util.List;

public class CarritoController {

    private String observation;
    private Usuario user;
    private List<CartProduct> cartProducts;
    private NodoDate nodoDate;
    private General general;
    private Double total;

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public List<CartProduct> getCartProducts() {
        return cartProducts;
    }

    public void setCartProducts(List<CartProduct> cartProducts) {
        this.cartProducts = cartProducts;
    }

    public NodoDate getNodoDate() {
        return nodoDate;
    }

    public void setNodoDate(NodoDate nodoDate) {
        this.nodoDate = nodoDate;
    }

    public General getGeneral() {
        return general;
    }

    public void setGeneral(General general) {
        this.general = general;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
