package com.example.elpaseoapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.text.method.PasswordTransformationMethod;
import android.widget.Toast;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import static java.util.Calendar.*;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
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
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        DatePicker datePicker = (DatePicker) view.findViewById(R.id.registro_birthday);
        datePicker.setMaxDate(new Date().getTime());

        Button registerButton = (Button) view.findViewById(R.id.registro_btn);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText n = (EditText) view.findViewById(R.id.registro_name);
                String nombre = n.getText().toString();
                EditText a = (EditText) view.findViewById(R.id.registro_last_name);
                String apellido = a.getText().toString();
                DatePicker f = (DatePicker) view.findViewById(R.id.registro_birthday);
                GregorianCalendar fechaNacimiento = new GregorianCalendar(f.getYear(), f.getMonth(), f.getDayOfMonth());
                int edad = getEdad(fechaNacimiento);
                EditText e = (EditText) view.findViewById(R.id.registro_email);
                String email = e.getText().toString();
                EditText c = (EditText) view.findViewById(R.id.registro_password);
                String contraseña = c.getText().toString();
                EditText cC = (EditText) view.findViewById(R.id.registro_confirm_password);
                String confirmarContraseña = cC.getText().toString();
                EditText t = (EditText) view.findViewById(R.id.registro_phone);
                String telefono = t.getText().toString();
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
                if (nombre.length() == 0) {
                    error = true;
                    msg += String.format("Debe completar su nombre.%n");
                }
                if (apellido.length() == 0) {
                    error = true;
                    msg += String.format("Debe completar su apellido.%n");
                }
                if (edad == 0) {
                    error = true;
                    msg += String.format("Debe seleccionar una fecha de nacimiento válida.%n");
                }
                if (email.length() == 0) {
                    error = true;
                    msg += String.format("Debe completar su correo electrónico.%n");
                }
                if (telefono.length() == 0) {
                    error = true;
                    msg += String.format("Debe completar su teléfono.%n");
                }
                if (calle.length() == 0) {
                    error = true;
                    msg += String.format("Debe completar la calle de su dirección.%n");
                }
                if (altura.length() == 0) {
                    error = true;
                    msg += String.format("Debe completar la altura de su dirección.%n");
                }
                if (contraseña.length() == 0) {
                    error = true;
                    msg += String.format("Debe completar su contraseña.%n");
                } else if (contraseña.length() < 8) {
                    error = true;
                    msg += String.format("La contraseña debe ser de al menos 8 caracteres.%n");
                }
                if (!contraseña.toString().equals(confirmarContraseña.toString())) {
                    error = true;
                    msg += String.format("Las contraseñas no coinciden.");
                }

                if (error) {
                    mostrarDialogError(msg);
                } else {
                    Direccion direccion = new Direccion(calle, altura, descripcion);
                    UsuarioSignUp user = new UsuarioSignUp(nombre, apellido, email, telefono, direccion, contraseña, confirmarContraseña, edad);
                    APIService service;
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(APIService.url)
                            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                            .build();

                    service = retrofit.create(APIService.class);
                    service.signUp(user).enqueue(new Callback<Usuario>() {
                        @Override
                        public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                            if (response.isSuccessful()) {
                                LoginUser tokenGenerator = new LoginUser(email, contraseña);
                                SessionPreferences.setPrefUsername(getContext(), response.body().getFirstName() + " " + response.body().getLastName());
                                SessionPreferences.guardarDireccionRegisto(getContext(), user.getAddress());
                                generateToken(tokenGenerator);
                            } else if(response.code() >= 500) {
                                mostrarDialogError("Ya existe un usuario registrado con ese correo.");
                            }
                        }

                        @Override
                        public void onFailure(Call<Usuario> call, Throwable t) {
                            CharSequence text = "Error de conexión";
                            int duration = Toast.LENGTH_LONG;
                            Toast toast = Toast.makeText(getContext(), text, duration);
                            toast.show();
                        }
                    });
                }
            }
        });

        Button verPassword = (Button) view.findViewById(R.id.registro_ver_password);
        verPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText contraseña = (EditText) view.findViewById(R.id.registro_password);
                if (contraseña.getTransformationMethod() != null) {
                    contraseña.setTransformationMethod(null);
                } else {
                    contraseña.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });

        Button verConfirmPassword = (Button) view.findViewById(R.id.registro_ver_confirm_password);
        verConfirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText contraseña = (EditText) view.findViewById(R.id.registro_confirm_password);
                if (contraseña.getTransformationMethod() != null) {
                    contraseña.setTransformationMethod(null);
                } else {
                    contraseña.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });

        return  view;
    }

    private void mostrarDialogError(String msg) {
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

    public static int getEdad(GregorianCalendar fechaNacimiento) {
        GregorianCalendar hoy = new GregorianCalendar();
        int diff = hoy.get(YEAR) - fechaNacimiento.get(YEAR);
        // Controlar si ya cumplio años este año
        if (fechaNacimiento.get(MONTH) > hoy.get(MONTH) ||
                (fechaNacimiento.get(MONTH) == hoy.get(MONTH) && fechaNacimiento.get(DATE) > fechaNacimiento.get(DATE))) {
            diff--;
        }
        return diff;
    }

    private void generateToken(LoginUser user) {
        APIService service;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.url)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();

        service = retrofit.create(APIService.class);
        service.getToken(user).enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                String token = response.body().getValue();
                SessionPreferences.setPrefToken(getContext(), token);
                SessionPreferences.setUser(getContext(), response.body().getUser());
                // Una vez seteado el token podemos cambiar el fragment
                // Si no esperamos a que se setee el token creo que cambia el contexto
                CharSequence text = "Registrado correctamente";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(getContext(), text, duration);
                toast.show();
                // Vuelvo a cargar el menu lateral
                ((MainActivity) getActivity()).verificarLogin();
                FragmentManagement.reemplazarFragment(getFragmentManager(), new HomeFragment());
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                CharSequence text = "Error de conexión.";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(getContext(), text, duration);
                toast.show();
            }
        });
    }
}