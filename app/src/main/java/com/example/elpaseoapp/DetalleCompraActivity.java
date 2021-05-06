package com.example.elpaseoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.navigation.NavigationView;


import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class DetalleCompraActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalle_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_close, R.string.navigation_drawer_open);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        verificarLogin();

        FragmentManagement.agregarFragmentInicialDesdeDetalle(getSupportFragmentManager(), new DetalleCompraFragment());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                startActivity(intent);
                break;
            case R.id.nav_nueva_compra:
                Intent intentNuevaCompra = new Intent(getApplicationContext(), ListadoProductoActivity.class);
                startActivity(intentNuevaCompra);
                break;
            case R.id.nav_quienes_somos:
                intent.putExtra("fragment", "quienes somos");
                startActivity(intent);
                break;
            case R.id.nav_productores:
                intent.putExtra("fragment", "productores");
                startActivity(intent);
                break;
            case R.id.nav_preferencias:
                //FIXME: Este fragment solo debería ser visible una vez que el usuario inicio sesión en la app. Lo dejo asi para probarlo.
                intent.putExtra("fragment", "preferencias");
                startActivity(intent);
                break;
            case R.id.nav_log_in:
                intent.putExtra("fragment", "login");
                startActivity(intent);
                break;
            case R.id.nav_sign_up:
                intent.putExtra("fragment", "signup");
                startActivity(intent);
                break;
            case R.id.nav_log_out:
                intent.putExtra("fragment", "logout");
                startActivity(intent);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void verificarLogin() {
        if (SessionPreferences.getPrefUsername(DetalleCompraActivity.this).length() == 0) {
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
            mail.setText(SessionPreferences.getPrefUsername(DetalleCompraActivity.this));
            ImageView imagen = (ImageView) headerView.findViewById(R.id.header_image);
            imagen.setImageResource(R.mipmap.ic_user_default_foreground);
        }
    }
}
