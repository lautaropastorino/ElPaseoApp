package com.example.elpaseoapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetalleCompraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetalleCompraFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private APIService service;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DetalleCompraFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetalleCompraFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetalleCompraFragment newInstance(String param1, String param2) {
        DetalleCompraFragment fragment = new DetalleCompraFragment();
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
        View view =  inflater.inflate(R.layout.fragment_detalle_compra, container, false);

        DecimalFormat df = new DecimalFormat("0.00");

        LinearLayout listaDetalle = (LinearLayout) view.findViewById(R.id.detalle_linear);
        for (Producto p: Carrito.getInstance().getProductos()) {
            System.out.println(p.getTitle());
            LinearLayout filaDetalle = new LinearLayout(getContext());
            TextView prod = new TextView(getContext());
            prod.setText("(x" + Carrito.getInstance().getCantidadProducto(p) + ") " + p.getTitle());
            prod.setTextSize(18f);
            filaDetalle.addView(prod);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            TextView precio = new TextView(getContext());
            precio.setLayoutParams(params);
            precio.setText("$" + df.format(p.getPrice() * Carrito.getInstance().getCantidadProducto(p)));
            precio.setTextSize(18f);
            precio.setGravity(Gravity.RIGHT);
            filaDetalle.addView(precio);
            listaDetalle.addView(filaDetalle);
        }
        TextView montoTotal = (TextView) view.findViewById(R.id.monto);
        montoTotal.setText("$" + df.format(Carrito.getInstance().getPrecioTotal()));


        Button realizarCompra = (Button) view.findViewById(R.id.boton_confirmar_compra);
        realizarCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManagement.reemplazarFragmentDesdeDetalle(getActivity().getSupportFragmentManager(), new MetodoPagoFragment());
            }
        });

        Button btnAtras = (Button) view.findViewById(R.id.boton_atras);
        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        return view;
    }
}