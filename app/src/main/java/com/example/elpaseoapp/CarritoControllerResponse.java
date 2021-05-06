package com.example.elpaseoapp;

import java.util.List;

public class CarritoControllerResponse {

    private boolean canceled;
    private List<CartProduct> cartProducts;

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public List<CartProduct> getCartProducts() {
        return cartProducts;
    }

    public void setCartProducts(List<CartProduct> cartProducts) {
        this.cartProducts = cartProducts;
    }
}
