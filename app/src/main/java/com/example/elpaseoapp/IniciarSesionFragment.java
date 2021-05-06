package com.example.elpaseoapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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
 * Use the {@link IniciarSesionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IniciarSesionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public IniciarSesionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IniciarSesionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IniciarSesionFragment newInstance(String param1, String param2) {
        IniciarSesionFragment fragment = new IniciarSesionFragment();
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
        View view = inflater.inflate(R.layout.fragment_iniciar_sesion, container, false);

        Button verPassword = (Button) view.findViewById(R.id.login_ver_password);
        verPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText contraseña = (EditText) view.findViewById(R.id.login_password);
                if (contraseña.getTransformationMethod() != null) {
                    contraseña.setTransformationMethod(null);
                } else {
                    contraseña.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });

        TextView loginRegistrate = (TextView) view.findViewById(R.id.login_registrate);
        loginRegistrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManagement.reemplazarFragment(getFragmentManager(), new SignUpFragment());
            }
        });

        Button loginButton = (Button) view.findViewById(R.id.login_btn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText e = (EditText) view.findViewById(R.id.login_mail);
                String email = e.getText().toString();
                EditText c = (EditText) view.findViewById(R.id.login_password);
                String contraseña = c.getText().toString();
                boolean error = false;
                String msg = new String();
                if (email.length() == 0) {
                    error = true;
                    msg += String.format("Debe ingresar un mail.%n");
                }
                if (contraseña.length() == 0) {
                    error = true;
                    msg += String.format("Debe ingresar una contraseña.%n");
                }

                if (error) {
                    mostrarDialogError(msg);
                } else {
                    APIService service;
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(APIService.url)
                            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                            .build();

                    service = retrofit.create(APIService.class);

                    LoginUser user = new LoginUser(email, contraseña);
                    service.getToken(user).enqueue(new Callback<TokenResponse>() {
                        @Override
                        public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                            if (response.isSuccessful()) {
                                String token = response.body().getValue();
                                SessionPreferences.setPrefToken(getContext(), token);
                                String username = response.body().getUser().getFirstName() + " " + response.body().getUser().getLastName();
                                SessionPreferences.setPrefUsername(getContext(), username);
                                SessionPreferences.guardarDireccionRegisto(getContext(), response.body().getUser().getAddress());
                                SessionPreferences.setUser(getContext(), response.body().getUser());
                                CharSequence text = "Sesión Iniciada";
                                int duration = Toast.LENGTH_LONG;
                                Toast toast = Toast.makeText(getContext(), text, duration);
                                toast.show();
                                // Vuelvo a cargar el menu lateral
                                ((MainActivity) getActivity()).verificarLogin();
                                FragmentManagement.reemplazarFragment(getFragmentManager(), new HomeFragment());
                            } else {
                                mostrarDialogError("Email o contraseña incorrectos.");
                            }
                        }

                        @Override
                        public void onFailure(Call<TokenResponse> call, Throwable t) {
                            CharSequence text = "Error de conexión";
                            int duration = Toast.LENGTH_LONG;
                            Toast toast = Toast.makeText(getContext(), text, duration);
                            toast.show();
                        }
                    });
                }
            }
        });

        return view;
    }

    public void mostrarDialogError(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Error en los datos");
        builder.setMessage(msg);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}