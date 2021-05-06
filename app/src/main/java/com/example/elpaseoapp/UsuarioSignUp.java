package com.example.elpaseoapp;


public class UsuarioSignUp {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Direccion address;
    private String encryptedPassword;
    private String passwordConfirmation;
    private int age;

    public UsuarioSignUp() {

    }

    public UsuarioSignUp(String firstName, String lastName, String email, String phone, Direccion address, String encryptedPassword, String passwordConfirmation, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.encryptedPassword = encryptedPassword;
        this.passwordConfirmation = passwordConfirmation;
        this.age = age;
    }


    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Direccion getAddress() {
        return address;
    }

    public void setAddress(Direccion address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}
