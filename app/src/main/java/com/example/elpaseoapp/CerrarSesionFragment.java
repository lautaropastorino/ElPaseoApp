package com.example.elpaseoapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CerrarSesionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CerrarSesionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CerrarSesionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CerrarSesionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CerrarSesionFragment newInstance(String param1, String param2) {
        CerrarSesionFragment fragment = new CerrarSesionFragment();
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
        View view = inflater.inflate(R.layout.fragment_cerrar_sesion, container, false);

        Button buttonSi = (Button) view.findViewById(R.id.cerrar_sesion_si);
        buttonSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence text = "Sesión finalizada";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(getContext(), text, duration);
                toast.show();
                SessionPreferences.clearSessionPreferences(getContext());
                Carrito.getInstance().resetearTodoElPedido();
                ((MainActivity) getActivity()).verificarLogin();
                FragmentManagement.reemplazarFragment(getFragmentManager(), new HomeFragment());
            }
        });

        Button buttonNo = (Button) view.findViewById(R.id.cerrar_sesion_no);
        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManagement.reemplazarFragment(getFragmentManager(), new HomeFragment());
            }
        });

        return view;
    }
}