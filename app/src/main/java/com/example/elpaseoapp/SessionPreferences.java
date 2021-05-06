package com.example.elpaseoapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SessionPreferences {
    static final String PREF_USERNAME = "username";
    static final String PREF_TOKEN = "token";
    static final Gson gson = new Gson();

    static SharedPreferences getSharedPreferences(Context context) {
        // Obtener el manejador de SharedPreferences
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setUser(Context context, Usuario user){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        String userJson = gson.toJson(user);
        editor.putString("USER", userJson);
        editor.commit();
    }

    public static Usuario getUser(Context context){
        return gson.fromJson(getSharedPreferences(context).getString("USER", null), Usuario.class);
    }

    public static void setPrefUsername(Context context, String username) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_USERNAME, username);
        editor.commit();
    }

    public static String getPrefUsername(Context context)
    {
        return getSharedPreferences(context).getString(PREF_USERNAME, "");
    }

    public static void clearSessionPreferences(Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().clear().commit();
    }

    public static void guardarTarjeta(Context context, String tarjetaJson){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("tarjetaGuardada", tarjetaJson);
        editor.commit();
    }

    public static String getTarjetaGuardada(Context context){
        return getSharedPreferences(context).getString("tarjetaGuardada", null);
    }

    public static void eliminarTarjetaGuardada(Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("tarjetaGuardada", null);
        editor.commit();
    }

    public static void guardarDireccion(Context context, String listaDireccionesJson){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("direccionesGuardadas", listaDireccionesJson);
        editor.commit();
    }

    public static ArrayList<Direccion> getDireccionesGuardadas(Context context){
        String listaDireccionesJson = getSharedPreferences(context).getString("direccionesGuardadas", null);
        Type direccionType = new TypeToken<ArrayList<Direccion>>(){}.getType();
        ArrayList<Direccion> listaDirecciones = gson.fromJson(listaDireccionesJson, direccionType);
        return listaDirecciones;
    }

    public static void setPrefToken(Context context, String token) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_TOKEN, token);
        editor.commit();
    }

    public static String getPrefToken(Context context) {
        return getSharedPreferences(context).getString(PREF_TOKEN, "");
    }

    public static void guardarDireccionRegisto(Context context, Direccion direccion){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        String direccionJson = gson.toJson(direccion);
        editor.putString("direccionRegistro", direccionJson);
        editor.commit();
    }

    public static Direccion getDireccionRegistro(Context context){
        String direccionString = getSharedPreferences(context).getString("direccionRegistro", null);
        Direccion dir = gson.fromJson(direccionString, Direccion.class);
        return dir;
    }

    public static void eliminarDireccionRegistro(Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("direccionRegistro", null);
        editor.commit();
    }

}
