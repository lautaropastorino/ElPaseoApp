package com.example.elpaseoapp;

public class Tarjeta {

    private String nombre;
    private String apellido;
    private String numeroTarjeta;
    private String marca;
    private String fechaVencimiento;
    private String codSeguridad;

    public Tarjeta(String nombre, String apellido, String numeroTarjeta, String marca, String fechaVencimiento, String codSeguridad) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.numeroTarjeta = numeroTarjeta;
        this.marca = marca;
        this.fechaVencimiento = fechaVencimiento;
        this.codSeguridad = codSeguridad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getCodSeguridad() {
        return codSeguridad;
    }

    public void setCodSeguridad(String codSeguridad) {
        this.codSeguridad = codSeguridad;
    }

}
