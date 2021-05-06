package com.example.elpaseoapp;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MetodoPagoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MetodoPagoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Tarjeta tarjetaSeleccionada;

    public MetodoPagoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MetodoPagoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MetodoPagoFragment newInstance(String param1, String param2) {
        MetodoPagoFragment fragment = new MetodoPagoFragment();
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

    @SuppressLint("NonConstantResourceId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_metodo_pago, container, false);
        this.deshabilitarBotonContinuar(view);
        this.deshabilitarBotonAgregarTarjeta(view);

        TextView monto = (TextView) view.findViewById(R.id.metodo_pago_monto);
        monto.setText(Carrito.getInstance().getPrecioTotalToString());

        // Si no hay ninguna tarjeta guardada, deshabilito el checkbox correspondiente
        Gson gson = new Gson();
        String tarjetaJson = SessionPreferences.getTarjetaGuardada(getContext());
        RadioButton cbTarjetaGuardada = (RadioButton) view.findViewById(R.id.radiobutton_metodo_tarjeta_guardada);
        if (tarjetaJson == null){
            cbTarjetaGuardada.setEnabled(false);
        } else {
            Tarjeta tarjeta = gson.fromJson(tarjetaJson, Tarjeta.class);
            String textCheckbox = "Pago con " + tarjeta.getMarca() + " terminada en " + tarjeta.getNumeroTarjeta().substring(tarjeta.getNumeroTarjeta().length() - 4);
            cbTarjetaGuardada.setText(textCheckbox);
        }

        // Listeners para cada boton
        Button botonContinuar = (Button) view.findViewById(R.id.boton_continuar);
        Button botonAgregarTarjeta = (Button) view.findViewById(R.id.boton_agregar_tarjeta);
        Button botonAtras = (Button) view.findViewById(R.id.boton_atras);

        botonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManagement.reemplazarFragmentDesdeDetalle(getActivity().getSupportFragmentManager(), new DireccionEnvioFragment(tarjetaSeleccionada));
            }
        });
        botonAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManagement.reemplazarFragmentDesdeDetalle(getActivity().getSupportFragmentManager(), new DetalleCompraFragment());
            }
        });
        botonAgregarTarjeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManagement.reemplazarFragmentDesdeDetalle(getActivity().getSupportFragmentManager(), new AgregarTarjetaFragment(true));
            }
        });

        // Lógica para la seleccion de botones
        RadioGroup rg = (RadioGroup) view.findViewById(R.id.radio_button_group);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radiobutton_metodo_efectivo:
                        deshabilitarBotonAgregarTarjeta(view);
                        habilitarBotonContinuar(view);
                        setTarjetaSeleccionada(null);
                        break;
                    case R.id.radiobutton_metodo_tarjeta_guardada:
                        deshabilitarBotonAgregarTarjeta(view);
                        habilitarBotonContinuar(view);
                        setTarjetaSeleccionada(gson.fromJson(tarjetaJson, Tarjeta.class));
                        break;
                    case R.id.radiobutton_metodo_tarjeta_nueva:
                        deshabilitarBotonContinuar(view);
                        habilitarBotonAgregarTarjeta(view);
                        mostrarToast();
                        break;
                }
            }
        });

        return view;
    }

    private void deshabilitarBotonContinuar(View view){
        Button botonContinuar = (Button) view.findViewById(R.id.boton_continuar);
        botonContinuar.setEnabled(false);
    }

    private void habilitarBotonContinuar(View view){
        Button botonContinuar = (Button) view.findViewById(R.id.boton_continuar);
        botonContinuar.setEnabled(true);
    }

    private void habilitarBotonAgregarTarjeta(View view){
        Button botonAgregarTarjeta = (Button) view.findViewById(R.id.boton_agregar_tarjeta);
        botonAgregarTarjeta.setVisibility(View.VISIBLE);
        botonAgregarTarjeta.setEnabled(true);
    }

    private void deshabilitarBotonAgregarTarjeta(View view){
        Button botonAgregarTarjeta = (Button) view.findViewById(R.id.boton_agregar_tarjeta);
        botonAgregarTarjeta.setVisibility(View.INVISIBLE);
        botonAgregarTarjeta.setEnabled(false);
    }

    private void mostrarToast(){
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(getContext(), "Seleccione 'Agregar tarjeta' para continuar con la compra.", duration);
        toast.show();
    }

    private void setTarjetaSeleccionada(Tarjeta tarjeta){
        this.tarjetaSeleccionada = tarjeta;
    }


}