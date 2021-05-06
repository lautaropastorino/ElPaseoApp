package com.example.elpaseoapp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.text.DecimalFormat;

public class Carrito {

    private static final Carrito instance = new Carrito();
    private Map<Producto, Integer> map;
    private double precioTotal;

    private Carrito() {
        this.map = new HashMap<Producto, Integer>();
        this.precioTotal = 0.0;
    }

    public void agregarCantidadAProducto(Integer cant, Producto prod){
        // Si está vacio, creamos la entrada, sino sumamos.
        if (map.keySet().contains(prod)){
            Integer cantAnterior = this.map.get(prod);
            this.map.put(prod, cantAnterior + cant);
        } else {
            this.map.put(prod, cant);
        }
    }

    public void resetearCantidadProducto(Producto prod){
        if (map.keySet().contains(prod)) {
            this.map.remove(prod);
        }
    }

    public void resetearTodoElPedido(){
        this.map = new HashMap<Producto, Integer>();
        this.precioTotal = 0.0;
    }

    public boolean estaPresente(Producto prod){
        return this.map.containsKey(prod);
    }

    public Set<Producto> getProductos() {
        return map.keySet();
    }

    public Integer getCantidadProducto(Producto prod){
        if (map.keySet().contains(prod)) {
            return this.map.get(prod);
        } else {
            return 0;
        }
    }

    public void agregarPrecioACarro(double precio){
        this.precioTotal = this.precioTotal + precio;
    }

    public double getPrecioTotal(){
        return this.precioTotal;
    }

    public String getPrecioTotalToString() {
        DecimalFormat formatDouble = new DecimalFormat("#####.##");
        return ("Total: $" + String.valueOf(formatDouble.format(this.precioTotal)));
    }

    public Map<Producto, Integer> getMapProductos(){
        return map;
    }

    public static Carrito getInstance(){
        return instance;
    }

}
