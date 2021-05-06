package com.example.elpaseoapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
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


import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class ListadoProductoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private APIService service;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private static DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_productos);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_close, R.string.navigation_drawer_open);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        verificarLogin();

        if (SessionPreferences.getPrefUsername(getApplicationContext()).length() == 0) {
            RelativeLayout footer = (RelativeLayout) findViewById(R.id.footer);
            footer.setVisibility(View.GONE);
        }

        //Creo el servicio para la API
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.url)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();

        service = retrofit.create(APIService.class);

        actualizarPrecioView("Total: $" + df.format(Carrito.getInstance().getPrecioTotal()));

        service.getProductos().enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                HashSet<Categoria> categorias = new HashSet<Categoria>();
                for (Producto p : response.body()) {
                    // el mapper crea un objeto por cada categoria aunque sea la misma categoria
                    for (Categoria c : p.getCategories()) {
                        boolean esta = false;
                        for (Categoria cat : categorias) {
                            if (cat.getId() == c.getId()) {
                                esta = true;
                                break;
                            }
                        }
                        if (!esta) {
                            // Guardo la categoria solo si no la habia guardado antes
                            categorias.add(c);
                        }
                    }
                }

                //Diccionario con categorias como clave y productos como valor
                HashMap<Categoria, ArrayList<Producto>> productos = new HashMap<Categoria, ArrayList<Producto>>();
                for (Categoria c : categorias) {
                    productos.put(c, new ArrayList<Producto>());
                }
                for (Producto p : response.body()) {
                    for (Categoria c : p.getCategories()) {
                        for (Categoria cat : productos.keySet()) {
                            if (cat.getId() == c.getId()) {
                                productos.get(cat).add(p);
                            }
                        }
                    }
                }

                LinearLayout listaCategoriasLinear = (LinearLayout) findViewById(R.id.linear_categorias);
                Categoria catOferta = null;
                ArrayList<Categoria> listaCategorias = new ArrayList<Categoria>();
                for (Categoria cat : productos.keySet()) {
                    if (cat.getName().equals("Ofertas")){
                        catOferta = cat;
                    }
                    else {
                        listaCategorias.add(cat);
                    }
                }
                // Primero generamos la vista correspondiente para las ofertas y sus productos.
                generarCategoriaListaProductosView(listaCategoriasLinear, productos, catOferta);
                for (Categoria c : listaCategorias) {
                    // Luego generamos las demás categorias.
                    generarCategoriaListaProductosView(listaCategoriasLinear, productos, c);
                }
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {
                CharSequence text = "Error de conexión";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(ListadoProductoActivity.this, text, duration);
                toast.show();
            }
        });

        Button resetCarro = (Button) findViewById(R.id.boton_borrar_seleccion);
        resetCarro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Carrito.getInstance().resetearTodoElPedido();
                actualizarPrecioView("Total: $0,00");
                CharSequence text = "Selección borrada";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(ListadoProductoActivity.this, text, duration);
                toast.show();
            }
        });

        Button confirmarCompra = (Button) findViewById(R.id.boton_realizar_compra);
        confirmarCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Carrito.getInstance().getPrecioTotal() > 0.0f) {
                    Intent intent = new Intent(ListadoProductoActivity.this, DetalleCompraActivity.class);
                    startActivity(intent);
                } else {
                    CharSequence text = "Seleccione al menos 1 producto";
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(ListadoProductoActivity.this, text, duration);
                    toast.show();
                }
            }
        });
    }

    private void generarCategoriaListaProductosView(LinearLayout listaCategorias, HashMap<Categoria, ArrayList<Producto>> productos, Categoria c){
        //Nombre de la categoria
        TextView nombreCategoria = new TextView(ListadoProductoActivity.this);
        nombreCategoria.setText(c.getName().substring(0, 1).toUpperCase() + c.getName().substring(1));
        nombreCategoria.setTextSize(30f);
        listaCategorias.addView(nombreCategoria);
        //Lista de productos en la categoria
        ListView listaProductos = new ListView(ListadoProductoActivity.this);
        listaProductos.setDividerHeight(0);
        listaCategorias.addView(listaProductos);
        AdaptadorProducto adapter = new AdaptadorProducto(ListadoProductoActivity.this, R.layout.producto_list_element, productos.get(c));
        listaProductos.setAdapter(adapter);
        AdapterHelper.getListViewSize(listaProductos);
        //Agrego margenes al nombre y a la lista de productos
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        params.setMargins(0, 15, 0, 10);
        nombreCategoria.setLayoutParams(params);
        //listaProductos.setLayoutParams(params);
    }

    private void actualizarPrecioView(String precioStr){
        TextView precioText = (TextView) findViewById(R.id.precioTotalCarro);
        precioText.setText(precioStr);
    }

    public void mostrarDialogCantidad(int id_prod){
        service.getProducto(id_prod).enqueue(new Callback<Producto>() {
            @Override
            public void onResponse(Call<Producto> call, Response<Producto> response) {
                final Dialog d = new Dialog(ListadoProductoActivity.this);
                d.setContentView(R.layout.seleccionar_cantidad_dialog);
                TextView titulo = (TextView) d.findViewById(R.id.titulo_seleccionar_cantidad);
                Button b1_ok = (Button) d.findViewById(R.id.boton_accept);
                Button b2_cancel = (Button) d.findViewById(R.id.boton_cancel);
                Button b3_restart = (Button) d.findViewById(R.id.boton_restart);
                final NumberPicker np = (NumberPicker) d.findViewById(R.id.number_picker);

                String t = "Seleccionar la cantidad del producto: " + response.body().getTitle();
                // Obtengo el stock correspondiente al ID del producto.
                np.setMaxValue(response.body().getStock());
                np.setMinValue(1);
                if (response.body().getStock() == 0) {
                    t += String.format("%n(No hay stock)");
                    b1_ok.setEnabled(false);
                    b3_restart.setEnabled(false);
                }
                titulo.setText(t);

                // Valor default cuando se abre el dialog
                np.setTag(1);
                np.setWrapSelectorWheel(false);
                if (Carrito.getInstance().estaPresente(response.body())){
                    // Si ya se seleccionó el prod. no se puede volver a elegir.
                    np.setEnabled(false);
                    b1_ok.setEnabled(false);
                    // Seteo al number picker con el valor que se eligio previamente
                    np.setValue(Carrito.getInstance().getCantidadProducto(response.body()));
                }

                // Lógica de los botones
                b1_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Carrito.getInstance().agregarCantidadAProducto(np.getValue(), response.body());
                        // Hay que actualizar el precio total
                        Carrito.getInstance().agregarPrecioACarro(np.getValue() * response.body().getPrice());
                        actualizarPrecioView(Carrito.getInstance().getPrecioTotalToString());
                        d.dismiss();
                        CharSequence text = "Producto agregado al carro";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(ListadoProductoActivity.this, text, duration);
                        toast.show();
                    }
                });
                b2_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });
                b3_restart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer cantSeleccionada = Carrito.getInstance().getCantidadProducto(response.body());
                        // Resto al total la cantidad que se habia seleccionado anteriormente.
                        Carrito.getInstance().agregarPrecioACarro(- (cantSeleccionada * response.body().getPrice()));
                        Carrito.getInstance().resetearCantidadProducto(response.body());
                        actualizarPrecioView(Carrito.getInstance().getPrecioTotalToString());
                        np.setEnabled(true);
                        b1_ok.setEnabled(true);
                    }
                });
                d.show();
            }

            @Override
            public void onFailure(Call<Producto> call, Throwable t) {
                CharSequence text = "Error de Conexión";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(ListadoProductoActivity.this, text, duration);
                toast.show();
            }
        });
    }

    public void mostrarDescripcion(int id) {

        service.getProducto(id).enqueue(new Callback<Producto>() {
            @Override
            public void onResponse(Call<Producto> call, Response<Producto> response) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ListadoProductoActivity.this);
                builder.setTitle(response.body().getTitle());

                String unidadStr = new String();
                unidadStr = response.body().getUnit().getDescription().substring(0, 1).toLowerCase();
                unidadStr += response.body().getUnit().getDescription().substring(1, response.body().getUnit().getDescription().length()-1);
                if (response.body().getUnit().getDescription().charAt(unidadStr.length()) != 's') {
                    unidadStr += response.body().getUnit().getDescription().charAt(unidadStr.length());
                }

                String descString = String.format("%s%n%nPrecio por %s: $%s%n%nMarca: %s",
                        response.body().getDescription() == null || response.body().getDescription() == "" ? "Descripción no disponible." : response.body().getDescription(),
                        unidadStr,
                        response.body().getPrice(),
                        response.body().getBrand() == null || response.body().getBrand() == "" ? "Marca no disponible." : response.body().getBrand()
                );
                builder.setMessage(descString);


                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }

            @Override
            public void onFailure(Call<Producto> call, Throwable t) {
                CharSequence text = "Error de conexión";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(ListadoProductoActivity.this, text, duration);
                toast.show();
            }
        });

    }

    public void mostrarDialogInicioSesion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ListadoProductoActivity.this);
        builder.setTitle("Debe iniciar sesión para continuar");
        builder.setMessage("Por favor inicie sesión para poder realizar un pedido.");
        builder.setPositiveButton("Ir", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("fragment", "login");
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void verificarLogin() {
        if (SessionPreferences.getPrefUsername(ListadoProductoActivity.this).length() == 0) {
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
            mail.setText(SessionPreferences.getPrefUsername(ListadoProductoActivity.this));
            ImageView imagen = (ImageView) headerView.findViewById(R.id.header_image);
            imagen.setImageResource(R.mipmap.ic_user_default_foreground);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                startActivity(intent);
                break;
            case R.id.nav_nueva_compra:
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

}
