package com.example.elpaseoapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AgregarTarjetaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AgregarTarjetaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Boolean esTemporal;

    public AgregarTarjetaFragment() {
        this.esTemporal = false;
    }

    public AgregarTarjetaFragment(Boolean esTemporal) {
        this.esTemporal = esTemporal;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AgregarTarjetaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AgregarTarjetaFragment newInstance(String param1, String param2) {
        AgregarTarjetaFragment fragment = new AgregarTarjetaFragment();
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

    public Boolean getEsTemporal() {
        return esTemporal;
    }

    public void setEsTemporal(Boolean esTemporal) {
        this.esTemporal = esTemporal;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_agregar_tarjeta, container, false);

        // Configuracion del spinner
        Spinner spinner = (Spinner) view.findViewById(R.id.agregar_tarjeta_marcas_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(), R.array.marcas, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Formato para el número de tarjeta
        TextView textViewNumeroTarjeta = view.findViewById(R.id.agregar_tarjeta_numero);
        textViewNumeroTarjeta.addTextChangedListener(new TextWatcher() {
            private static final char space = ' ';

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Remove all spacing char
                int pos = 0;
                while (true) {
                    if (pos >= s.length()) break;
                    if (space == s.charAt(pos) && (((pos + 1) % 5) != 0 || pos + 1 == s.length())) {
                        s.delete(pos, pos + 1);
                    } else {
                        pos++;
                    }
                }

                // Insert char where needed.
                pos = 4;
                while (true) {
                    if (pos >= s.length()) break;
                    final char c = s.charAt(pos);
                    // Only if its a digit where there should be a space we insert a space
                    if ("0123456789".indexOf(c) >= 0) {
                        s.insert(pos, "" + space);
                    }
                    pos += 5;
                }
            }
        });

        Button botonGuardar = (Button) view.findViewById(R.id.guardar_tarjeta_button);
        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = ((EditText) view.findViewById(R.id.agregar_tarjeta_nombre)).getText().toString();
                String apellido = ((EditText) view.findViewById(R.id.agregar_tarjeta_apellido)).getText().toString();
                String numero = ((EditText) view.findViewById(R.id.agregar_tarjeta_numero)).getText().toString();
                String marca = ((Spinner) view.findViewById(R.id.agregar_tarjeta_marcas_spinner)).getSelectedItem().toString();
                String fechaVencimiento = ((EditText) view.findViewById(R.id.agregar_tarjeta_fecha_venc)).getText().toString();
                String codigoSeguridad = ((EditText) view.findViewById(R.id.agregar_tarjeta_codigo)).getText().toString();

                boolean errorFormulario = false;
                String msjError = "";
                if (nombre.isEmpty() || nombre.trim().equals("")){
                    errorFormulario = true;
                    msjError += String.format("Debe completar el nombre que aparece en la tarjeta.%n");
                }
                if (apellido.isEmpty() || apellido.trim().equals("")){
                    errorFormulario = true;
                    msjError += String.format("Debe completar el apellido que aparece en la tarjeta.%n");
                }
                if (numero.isEmpty() || numero.trim().length() != 19){
                    errorFormulario = true;
                    msjError += String.format("Debe completar el número de la tarjeta.%n");
                }
                if (fechaVencimiento.isEmpty() || fechaVencimiento.trim().equals("")){
                    errorFormulario = true;
                    msjError += String.format("Debe completar la fecha de vencimiento de la tarjeta.%n");
                } else {
                    if (fechaVencimiento.charAt(2) != '/'){
                        errorFormulario = true;
                        msjError += String.format("El formato de la fecha de vencimiento debe ser MM/AA.%n");
                    }
                }
                if (codigoSeguridad.isEmpty() || fechaVencimiento.trim().equals("")){
                    errorFormulario = true;
                    msjError += "Debe completar el código de seguridad de la tarjeta.";
                }

                if (errorFormulario){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Error en los campos:");
                    builder.setMessage(msjError);
                    builder.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else {
                    Tarjeta tarjeta = new Tarjeta(nombre, apellido, numero, marca, fechaVencimiento, codigoSeguridad);
                    if (getEsTemporal()){
                        /* Si la tarjeta es temporal, significa que fue agregada para realizar el pago único de una compra, por lo tanto, no hay que almacenarla.
                        * Después se pasa al fragment para seleccionar la dirección de envio de la compra. */
                        FragmentManagement.reemplazarFragmentDesdeDetalle(getActivity().getSupportFragmentManager(), new DireccionEnvioFragment(tarjeta));
                    } else {
                        Gson gson = new Gson();
                        // Guardo los datos de la tarjeta en SharedPrefs.
                        SessionPreferences.guardarTarjeta(view.getContext(), gson.toJson(tarjeta));
                        // Vuelvo a la vista de preferencias de usuario.
                        FragmentManagement.reemplazarFragment(getActivity().getSupportFragmentManager(), new PreferenciasFragment());
                    }
                }
            }
        });

        Button botonAtras = (Button) view.findViewById(R.id.guardar_tarjeta_atras);
        botonAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getEsTemporal()) {
                    FragmentManagement.reemplazarFragmentDesdeDetalle(getActivity().getSupportFragmentManager(), new MetodoPagoFragment());
                } else {
                    FragmentManagement.reemplazarFragment(getActivity().getSupportFragmentManager(), new PreferenciasFragment());
                }
            }
        });

        return view;
    }
}