package com.example.elpaseoapp;

import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;

public class FragmentManagement {

    public static final FragmentManagement INSTANCE = new FragmentManagement();
    private static FragmentManager FM;
    private static FragmentTransaction FT;

    private FragmentManagement(){}

    public static void agregarFragmentInicial(FragmentManager fragManager, Fragment fragment){
        FM = fragManager;
        FT = FM.beginTransaction();
        FT.add(R.id.fragment_container, fragment);
        FT.commit();
    }

    public static void agregarFragmentInicialDesdeDetalle(FragmentManager fragManager, Fragment fragment){
        FM = fragManager;
        FT = FM.beginTransaction();
        FT.add(R.id.fragment_container_confirmar, fragment);
        FT.commit();
    }

    public static void reemplazarFragment(FragmentManager fragManager, Fragment fragment){
        FM = fragManager;
        FT = FM.beginTransaction();
        FT.replace(R.id.fragment_container, fragment);
        FT.commit();
    }

    public static void reemplazarFragmentDesdeDetalle(FragmentManager fragManager, Fragment fragment){
        FM = fragManager;
        FT = FM.beginTransaction();
        FT.replace(R.id.fragment_container_confirmar, fragment);
        FT.commit();
    }

    public static void reemplazarFragment(FragmentManager fragManager, Fragment fragment, Bundle bundle) {
        // Metodo sobrecargado para poder enviar parametros al fragment
        FM = fragManager;
        FT = FM.beginTransaction();
        fragment.setArguments(bundle);
        FT.replace(R.id.fragment_container, fragment);
        FT.commit();
    }

    public static final FragmentManagement getInstance(){
        return INSTANCE;
    }

}
