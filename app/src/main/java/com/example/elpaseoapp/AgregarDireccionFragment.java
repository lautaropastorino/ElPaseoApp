package com.example.elpaseoapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AgregarDireccionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AgregarDireccionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AgregarDireccionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AgregarDireccionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AgregarDireccionFragment newInstance(String param1, String param2) {
        AgregarDireccionFragment fragment = new AgregarDireccionFragment();
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
        View view = inflater.inflate(R.layout.fragment_agregar_direccion, container, false);
        Button botonGuardar = (Button) view.findViewById(R.id.boton_guardar_direccion);
        Button botonCancelar = (Button) view.findViewById(R.id.boton_cancelar);

        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManagement.reemplazarFragment(getActivity().getSupportFragmentManager(), new PreferenciasFragment());
            }
        });

        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Validar campos de formulario
                EditText ca = (EditText) view.findViewById(R.id.registro_street);
                String calle = ca.getText().toString();
                EditText al = (EditText) view.findViewById(R.id.registro_street_number);
                String altura = al.getText().toString();
                EditText p = (EditText) view.findViewById(R.id.registro_floor);
                String piso = p.getText().toString();
                EditText d = (EditText) view.findViewById(R.id.registro_apartment);
                String dpto = d.getText().toString();
                EditText de = (EditText) view.findViewById(R.id.registro_address_description);
                String descripcion = de.getText().toString();

                boolean error = false;
                String msg = new String();
                if (calle.length() == 0) {
                    error = true;
                    msg += String.format("Debe completar la calle de su dirección.%n");
                }
                if (altura.length() == 0) {
                    error = true;
                    msg += "Debe completar la altura de su dirección.";
                }

                if (error) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Error en los campos:");
                    builder.setMessage(msg);
                    builder.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    Gson gson = new Gson();
                    Direccion direccion = new Direccion(calle, altura, descripcion, piso, dpto);
                    // Recupero la lista de direcciones guardadas
                    ArrayList<Direccion> listaDireccionesRecuperada = SessionPreferences.getDireccionesGuardadas(view.getContext());
                    String jsonString;

                    // Si la lista es null, todavía no se agregó ninguna dirección.
                    if (listaDireccionesRecuperada == null){
                        ArrayList<Direccion> listaDirecciones= new ArrayList<Direccion>();
                        listaDirecciones.add(direccion);
                        jsonString = gson.toJson(listaDirecciones);
                    } else {
                        // Si no es null, genero el Json a partir de la lista recuperada.
                        listaDireccionesRecuperada.add(direccion);
                        jsonString = gson.toJson(listaDireccionesRecuperada);
                    }
                    SessionPreferences.guardarDireccion(view.getContext(), jsonString);

                    FragmentManagement.reemplazarFragment(getActivity().getSupportFragmentManager(), new PreferenciasFragment());
                }
            }
        });

        return view;
    }
}