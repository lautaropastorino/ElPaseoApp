package com.example.elpaseoapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static com.example.elpaseoapp.SessionPreferences.gson;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PreferenciasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreferenciasFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PreferenciasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PreferenciasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PreferenciasFragment newInstance(String param1, String param2) {
        PreferenciasFragment fragment = new PreferenciasFragment();
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
        View view =  inflater.inflate(R.layout.fragment_preferencias, container, false);
        TextView textTitulo = (TextView) view.findViewById(R.id.titulo_preferencias);
        textTitulo.setText(String.format("Hola, %s", SessionPreferences.getPrefUsername(view.getContext())));

        Button botonAgregar = (Button) view.findViewById(R.id.boton_agregar_tarjeta);
        String tarjetaJson = SessionPreferences.getTarjetaGuardada(view.getContext());
        if (tarjetaJson != null) {
            // Si hay una tarjeta agregada
            botonAgregar.setEnabled(false);
            Gson gson = new Gson();
            Tarjeta tarjeta = gson.fromJson(tarjetaJson, Tarjeta.class);
            String nombreCompleto = tarjeta.getNombre().toUpperCase() + " " + tarjeta.getApellido().toUpperCase();
            String marcaNumero = tarjeta.getMarca() + " terminada en " + tarjeta.getNumeroTarjeta().substring(tarjeta.getNumeroTarjeta().length() - 4);
            TextView txtNombreCompleto = (TextView) view.findViewById(R.id.nombre_completo_tarjeta);
            txtNombreCompleto.setText(nombreCompleto);
            TextView txtMarcaNumero = (TextView) view.findViewById(R.id.marca_numero_tarjeta);
            txtMarcaNumero.setText(marcaNumero);
            botonAgregar.setTextSize(15);
            botonAgregar.setText("Eliminar");
            botonAgregar.setEnabled(true);
            // Si el usuario selecciona eliminar
            botonAgregar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Eliminar tarjeta");
                    builder.setMessage("¿Desea eliminar la tarjeta? Luego podrá guardar otra.");
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Elimino la tarjeta de sharedPrefs.
                            SessionPreferences.eliminarTarjetaGuardada(view.getContext());
                            // Recargo la vista actual para reflejar los cambios.
                            FragmentManagement.reemplazarFragment(getActivity().getSupportFragmentManager(), new PreferenciasFragment());
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        } else {
            // Si no hay tarjeta agregada
            LinearLayout linearTarjeta = view.findViewById(R.id.linear_tarjeta);
            TextView textNoHayTarjeta = new TextView(view.getContext());
            textNoHayTarjeta.setText("No hay ninguna tarjeta guardada.");
            textNoHayTarjeta.setTextSize(14);
            textNoHayTarjeta.setGravity(Gravity.CENTER);
            textNoHayTarjeta.setTypeface(null, Typeface.BOLD_ITALIC);
            linearTarjeta.addView(textNoHayTarjeta);
            botonAgregar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManagement.reemplazarFragment(getActivity().getSupportFragmentManager(), new AgregarTarjetaFragment());
                }
            });
        }

        Button botonAgregarDireccion = (Button) view.findViewById(R.id.boton_agregar_direccion);
        // Recupero las direcciones guardadas
        ArrayList<Direccion> listaDirecciones = SessionPreferences.getDireccionesGuardadas(view.getContext());
        if (listaDirecciones != null) {
            if (listaDirecciones.size() != 0) {
                int i = 0;
                LinearLayout linearDirecciones = (LinearLayout) view.findViewById(R.id.linear_direcciones);
                for (Direccion direccion : listaDirecciones) {
                    LinearLayout direccionRow = new LinearLayout(view.getContext());
                    View v = inflater.inflate(R.layout.direccion_list_element, null);

                    // Agrego el texto para mostrar
                    TextView datosDireccion = (TextView) v.findViewById(R.id.datos_direccion);
                    String texto = "Calle " + direccion.getStreet() + ", n° " + direccion.getNumber();
                    datosDireccion.setText(texto);

                    Button botonEditar = v.findViewById(R.id.boton_editar_direccion);
                    botonEditar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putString("calle", direccion.getStreet());
                            bundle.putString("altura", direccion.getNumber());
                            bundle.putString("piso", direccion.getFloor());
                            bundle.putString("dpto", direccion.getApartment());
                            bundle.putString("descripcion", direccion.getDescription());
                            FragmentManagement.reemplazarFragment(getFragmentManager(), new EditarDireccionFragment(), bundle);
                        }
                    });

                    Button botonEliminar = v.findViewById(R.id.boton_eliminar_direccion);
                    botonEliminar.setTag(i);
                    botonEliminar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ArrayList<Direccion> auxListaDirecciones = listaDirecciones;
                            int index = (int) botonEliminar.getTag();
                            auxListaDirecciones.remove(index);
                            String listaDireccionesJson = gson.toJson(auxListaDirecciones);
                            SessionPreferences.guardarDireccion(getContext(), listaDireccionesJson);
                            FragmentManagement.reemplazarFragment(getActivity().getSupportFragmentManager(), new PreferenciasFragment());
                        }
                    });

                    direccionRow.addView(v);
                    linearDirecciones.addView(direccionRow);
                    i++;
                }

                if (listaDirecciones.size() >= 3) {
                    // Se deshabilita el botón para que no se puedan seguir guardando direcciones
                    botonAgregarDireccion.setEnabled(false);
                }
            } else {
                mostrarTextoNoHayDirecciones(view);
            }
        } else {
            mostrarTextoNoHayDirecciones(view);
        }
        botonAgregarDireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManagement.reemplazarFragment(getActivity().getSupportFragmentManager(), new AgregarDireccionFragment());
            }
        });
        return view;
    }

    private void mostrarTextoNoHayDirecciones(View view){
        LinearLayout linearDirecciones = view.findViewById(R.id.linear_direcciones);
        TextView textNoHayDireccion = new TextView(view.getContext());
        textNoHayDireccion.setText("No hay ninguna dirección guardada.");
        textNoHayDireccion.setTextSize(14);
        textNoHayDireccion.setGravity(Gravity.CENTER);
        textNoHayDireccion.setTypeface(null, Typeface.BOLD_ITALIC);
        linearDirecciones.addView(textNoHayDireccion);
    }

}