package com.example.elpaseoapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static com.example.elpaseoapp.SessionPreferences.gson;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfirmarCompraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfirmarCompraFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Direccion direccionCompra;
    private Tarjeta tarjetaCompra;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ConfirmarCompraFragment() {
        // Required empty public constructor
    }

    public ConfirmarCompraFragment(Direccion d, Tarjeta t){
        this.direccionCompra = d;
        this.tarjetaCompra = t;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConfirmarCompraFragment newInstance(String param1, String param2) {
        ConfirmarCompraFragment fragment = new ConfirmarCompraFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_confirmar_compra, container, false);

        LinearLayout linearDireccion = (LinearLayout) view.findViewById(R.id.linear_direccion);
        LinearLayout linearMetodoPago = (LinearLayout) view.findViewById(R.id.linear_metodo_pago);
        LinearLayout linearMontoTotal = (LinearLayout) view.findViewById(R.id.linear_monto);

        TextView tvDireccion = new TextView(getContext());
        TextView tvMetodoPago = new TextView(getContext());
        String txtDir;
        String txtMetodoPago;

        if (direccionCompra != null) {
            txtDir = "Enviar a calle: " + direccionCompra.getStreet() + ", número: " + direccionCompra.getNumber();
        } else {
            txtDir = "Retiro en nodo";
        }
        tvDireccion.setText(txtDir);
        tvDireccion.setTextSize(18);
        tvDireccion.setTypeface(null, Typeface.BOLD_ITALIC);

        if (tarjetaCompra != null){
            txtMetodoPago = tarjetaCompra.getMarca() + " terminada en " + tarjetaCompra.getNumeroTarjeta().substring(tarjetaCompra.getNumeroTarjeta().length() - 4);
        } else{
            txtMetodoPago = "Pago en efectivo";
        }
        tvMetodoPago.setText(txtMetodoPago);
        tvMetodoPago.setTextSize(18);
        tvMetodoPago.setTypeface(null, Typeface.BOLD_ITALIC);

        TextView tvMontoTotal = new TextView(getContext());
        tvMontoTotal.setText(Carrito.getInstance().getPrecioTotalToString());
        tvMontoTotal.setTextSize(18);
        tvMontoTotal.setTypeface(null, Typeface.BOLD_ITALIC);

        linearMontoTotal.addView(tvMontoTotal);
        linearDireccion.addView(tvDireccion);
        linearMetodoPago.addView(tvMetodoPago);

        Button botonAtras = (Button) view.findViewById(R.id.boton_atras);
        botonAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManagement.reemplazarFragmentDesdeDetalle(getActivity().getSupportFragmentManager(), new DireccionEnvioFragment());
            }
        });

        Button botonConfirmar = (Button) view.findViewById(R.id.boton_confirmar_compra);
        botonConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmar compra");
                builder.setMessage("¿Desea confirmar la compra? Por favor, revise los datos antes de confirmar la compra.");
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        actualizarCarritoAPI(view);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return view;
    }

    private void actualizarCarritoAPI(View view){
        view.findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.url)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();

        APIService service = retrofit.create(APIService.class);
        service.getGeneralActive().enqueue(new Callback<GeneralActiveResponse>() {
            @Override
            public void onResponse(Call<GeneralActiveResponse> call, Response<GeneralActiveResponse> response) {
                CarritoController carrito = new CarritoController();

                General g = new General();
                g.setId(response.body().getId());

                Nodo n = new Nodo();
                n.setId(response.body().getActiveNodes().get(0).getNode().getId());
                NodoDate nodoDate = new NodoDate();
                nodoDate.setNode(n);
                nodoDate.setId(response.body().getActiveNodes().get(0).getId());

                carrito.setGeneral(g);
                carrito.setNodoDate(nodoDate);

                Map<Producto, Integer> mapProds = Carrito.getInstance().getMapProductos();
                ArrayList<CartProduct> cartProds = new ArrayList<CartProduct>();
                for (Producto prod: mapProds.keySet()){
                    CartProduct cartProd = new CartProduct();
                    // Creamos una instancia vacía de Producto, solo seteamos el id.
                    Producto prodAux = new Producto();
                    prodAux.setId(prod.getId());
                    cartProd.setProduct(prodAux);
                    cartProd.setPrice(prod.getPrice());
                    cartProd.setQuantity(mapProds.get(prod));
                    cartProds.add(cartProd);
                }

                carrito.setCartProducts(cartProds);

                // Creamos una instancia vacía de Usuario, solo seteamos el id.
                Usuario user = SessionPreferences.getUser(getContext());
                Usuario userAux = new Usuario();
                userAux.setId(user.getId());
                carrito.setUser(userAux);

                carrito.setObservation("Realizando una compra.");

                carrito.setTotal(Carrito.getInstance().getPrecioTotal());

                String carritoJson = gson.toJson(carrito);
                System.out.println(carritoJson);

                service.updateCarrito("Bearer " + SessionPreferences.getPrefToken(getContext()), carrito).enqueue(new Callback<CarritoControllerResponse>() {
                    @Override
                    public void onResponse(Call<CarritoControllerResponse> call, Response<CarritoControllerResponse> response) {
                        view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Compra confirmada");
                        builder.setMessage("Su compra fue realizada exitosamente.");
                        builder.setIcon(R.drawable.ic_baseline_check_24);
                        builder.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                volverAlHome();
                            }
                        });
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                volverAlHome();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }

                    @Override
                    public void onFailure(Call<CarritoControllerResponse> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onFailure(Call<GeneralActiveResponse> call, Throwable t) {

            }
        });
    }

    private void volverAlHome(){
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }

}