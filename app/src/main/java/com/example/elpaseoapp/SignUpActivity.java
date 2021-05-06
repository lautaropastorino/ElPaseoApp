package com.example.elpaseoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class SignUpActivity extends AppCompatActivity {

    private APIService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        /*Usuario user = new Usuario();
        user.setEmail("ejemplo@mail.com");
        user.setFirstName("Ejemplo");
        user.setLastName("Apellido");
        user.setAge(20);
        user.setPhone("+542944658742");
        user.setDescription("Usuario de prueba");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://ec2-3-235-40-183.compute-1.amazonaws.com/")
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();

        service = retrofit.create(APIService.class);

        service.signUp(user).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                System.out.println(response.body());
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {

            }
        });*/

    }
}