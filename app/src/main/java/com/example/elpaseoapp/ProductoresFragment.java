package com.example.elpaseoapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductoresFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductoresFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProductoresFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductoresFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductoresFragment newInstance(String param1, String param2) {
        ProductoresFragment fragment = new ProductoresFragment();
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
        View view = inflater.inflate(R.layout.fragment_productores, container, false);

        APIService service;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.url)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();

        service = retrofit.create(APIService.class);

        service.getProductores().enqueue(new Callback<List<Productor>>() {
            @Override
            public void onResponse(Call<List<Productor>> call, Response<List<Productor>> response) {
                LinearLayout linearProductores = (LinearLayout) view.findViewById(R.id.linear_productores);
                for (Productor p : response.body()) {
                    LinearLayout productor = new LinearLayout(view.getContext());
                    LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View v = inflater.inflate(R.layout.productor_list_element, null);

                    TextView nombreProductor = (TextView) v.findViewById(R.id.productor_nombre);
                    String nombre = p.getName();
                    if (p.getLastName() != null) {
                        nombre += " " + p.getLastName();
                    }
                    nombreProductor.setText(nombre);

                    Button ver_mas = (Button) v.findViewById(R.id.productor_ver_mas);
                    ver_mas.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Abrir la página del productor
                            cambiarAProductor(p.getId(), view.getContext());
                        }
                    });
                    productor.addView(v);
                    linearProductores.addView(productor);
                    view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<Productor>> call, Throwable t) {
                CharSequence text = "Error de conexión";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(view.getContext(), text, duration);
                toast.show();
            }
        });
        return view;
    }

    private void cambiarAProductor(int id, Context actualContext) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        FragmentManagement.reemplazarFragment(getActivity().getSupportFragmentManager(), new ProductorFragment(), bundle);

    }
}