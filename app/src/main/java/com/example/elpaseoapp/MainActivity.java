package com.example.elpaseoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_close, R.string.navigation_drawer_open);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        verificarLogin();

        verificarFragment();
    }

    @Override
    public void onBackPressed() {
        // cerrar el drawer si esta abierto cuando se aprieta back
        if (this.drawer.isDrawerOpen(GravityCompat.START)){
            this.drawer.closeDrawer(GravityCompat.START);
        } else {
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if(f instanceof ProductorFragment) {
                FragmentManagement.reemplazarFragment(getSupportFragmentManager(), new ProductoresFragment());
            } else if(f instanceof AgregarDireccionFragment || f instanceof AgregarTarjetaFragment || f instanceof EditarDireccionFragment) {
                FragmentManagement.reemplazarFragment(getSupportFragmentManager(), new PreferenciasFragment());
            } else if (!(f instanceof HomeFragment)){
                FragmentManagement.reemplazarFragment(getSupportFragmentManager(), new HomeFragment());
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                FragmentManagement.reemplazarFragment(getSupportFragmentManager(), new HomeFragment());
                break;
            case R.id.nav_nueva_compra:
                Intent intent = new Intent(MainActivity.this, ListadoProductoActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_quienes_somos:
                FragmentManagement.reemplazarFragment(getSupportFragmentManager(), new QuienesSomosFragment());
                break;
            case R.id.nav_productores:
                FragmentManagement.reemplazarFragment(getSupportFragmentManager(), new ProductoresFragment());
                break;
            case R.id.nav_preferencias:
                //FIXME: Este fragment solo debería ser visible una vez que el usuario inicio sesión en la app. Lo dejo asi para probarlo.
                FragmentManagement.reemplazarFragment(getSupportFragmentManager(), new PreferenciasFragment());
                break;
            case R.id.nav_log_in:
                FragmentManagement.reemplazarFragment(getSupportFragmentManager(), new IniciarSesionFragment());
                break;
            case R.id.nav_sign_up:
                FragmentManagement.reemplazarFragment(getSupportFragmentManager(), new SignUpFragment());
                break;
            case R.id.nav_log_out:
                FragmentManagement.reemplazarFragment(getSupportFragmentManager(), new CerrarSesionFragment());
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void verificarLogin() {
        if (SessionPreferences.getPrefUsername(MainActivity.this).length() == 0) {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.nav_menu_logged_out);
            View headerView = navigationView.getHeaderView(0);
            TextView mail = (TextView) headerView.findViewById(R.id.header_mail);
            mail.setText("");
            ImageView imagen = (ImageView) headerView.findViewById(R.id.header_image);
            imagen.setImageResource(R.mipmap.ic_el_paseo_logo_v2_foreground);
        } else {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.nav_menu);
            View headerView = navigationView.getHeaderView(0);
            TextView mail = (TextView) headerView.findViewById(R.id.header_mail);
            mail.setText(SessionPreferences.getPrefUsername(MainActivity.this));
            ImageView imagen = (ImageView) headerView.findViewById(R.id.header_image);
            imagen.setImageResource(R.mipmap.ic_user_default_foreground);
        }
    }

    public void verificarFragment() {
        // Verificar si vengo desde otra activity o si recien arranca la app
        Intent i = getIntent();
        String fragament = i.getStringExtra("fragment");
        if (fragament != null) { // si es null recien arranca la app
            switch (fragament) {
                case "login":
                    FragmentManagement.reemplazarFragment(getSupportFragmentManager(), new IniciarSesionFragment());
                    break;
                case "signup":
                    FragmentManagement.reemplazarFragment(getSupportFragmentManager(), new SignUpFragment());
                    break;
                case "logout":
                    FragmentManagement.reemplazarFragment(getSupportFragmentManager(), new CerrarSesionFragment());
                    break;
                case "preferencias":
                    FragmentManagement.reemplazarFragment(getSupportFragmentManager(), new PreferenciasFragment());
                    break;
                case "quienes somos":
                    FragmentManagement.reemplazarFragment(getSupportFragmentManager(), new QuienesSomosFragment());
                    break;
                case "productores":
                    FragmentManagement.reemplazarFragment(getSupportFragmentManager(), new ProductoresFragment());
                    break;
            }
        } else {
            // Agregar el primer fragment base, correspondiente al home de la app
            FragmentManagement.reemplazarFragment(getSupportFragmentManager(), new HomeFragment());
        }

    }

}
