package com.example.elpaseoapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIService {

    String url = "http://ec2-3-227-239-131.compute-1.amazonaws.com";

    @GET("/api/product")
    Call<List<Producto>> getProductos();

    @GET("/api/category")
    Call<List<Categoria>> getCategorias();

    @GET("/api/product/{id}")
    Call<Producto> getProducto(@Path("id") int productId);

    @GET("/api/producer")
    Call<List<Productor>> getProductores();

     @GET("/api/producer/{id}")
    Call<Productor> getProductor(@Path("id") int productorId);

    @Headers({
            "Content-type: application/json"
    })
    @POST("/api/user/signup")
    Call<Usuario> signUp(@Body UsuarioSignUp user);

    @Headers({
            "Content-type: application/json"
    })
    @POST("api/token/generate-token")
    Call<TokenResponse> getToken(@Body LoginUser user);

    @GET("/api/news")
    Call<NoticiaResponse> getNoticias();

    @Headers({
            "Content-type: application/json"
    })
    @POST("/api/cart")
    Call<CarritoControllerResponse> updateCarrito(@Header("Authorization") String token, @Body CarritoController carrito);

    @GET("/api/general/active")
    Call<GeneralActiveResponse> getGeneralActive();

}
