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
 * Use the {@link EditarDireccionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditarDireccionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditarDireccionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditarDireccionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditarDireccionFragment newInstance(String param1, String param2) {
        EditarDireccionFragment fragment = new EditarDireccionFragment();
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
        View view = inflater.inflate(R.layout.fragment_editar_direccion, container, false);
        Bundle data = getArguments();
        EditText calle = (EditText) view.findViewById(R.id.editar_street);
        calle.setText(data.getString("calle"));
        EditText altura = (EditText) view.findViewById(R.id.editar_street_number);
        altura.setText(data.getString("altura"));
        EditText piso = (EditText) view.findViewById(R.id.editar_floor);
        piso.setText(data.getString("piso"));
        EditText dpto = (EditText) view.findViewById(R.id.editar_apartment);
        dpto.setText(data.getString("dpto"));
        EditText descripcion = (EditText) view.findViewById(R.id.editar_address_description);
        descripcion.setText(data.getString("descripcion"));

        Button cancelarButton = (Button) view.findViewById(R.id.boton_cancelar);
        cancelarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManagement.reemplazarFragment(getFragmentManager(), new PreferenciasFragment());
            }
        });

        Button guardarButton = (Button) view.findViewById(R.id.boton_guardar_direccion);
        guardarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText ca = (EditText) view.findViewById(R.id.editar_street);
                String calle = ca.getText().toString();
                EditText al = (EditText) view.findViewById(R.id.editar_street_number);
                String altura = al.getText().toString();
                EditText p = (EditText) view.findViewById(R.id.editar_floor);
                String piso = p.getText().toString();
                EditText d = (EditText) view.findViewById(R.id.editar_apartment);
                String dpto = d.getText().toString();
                EditText de = (EditText) view.findViewById(R.id.editar_address_description);
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
                    Direccion direccionActualizada = new Direccion(calle, altura, descripcion, piso, dpto);
                    Direccion direccionVieja = new Direccion(
                            data.getString("calle"),
                            data.getString("altura"),
                            data.getString("descripcion"),
                            data.getString("piso"),
                            data.getString("dpto"));
                    // Recupero la lista de direcciones guardadas
                    ArrayList<Direccion> listaDireccionesRecuperada = SessionPreferences.getDireccionesGuardadas(view.getContext());
                    String jsonString;

                    for (Direccion dir : listaDireccionesRecuperada) {
                        if (dir.equals(direccionVieja)) {
                            listaDireccionesRecuperada.set(listaDireccionesRecuperada.indexOf(dir), direccionActualizada);
                            break;
                        }
                    }

                    jsonString = gson.toJson(listaDireccionesRecuperada);
                    SessionPreferences.guardarDireccion(view.getContext(), jsonString);
                    FragmentManagement.reemplazarFragment(getActivity().getSupportFragmentManager(), new PreferenciasFragment());
                }
            }
        });

        return view;
    }
}