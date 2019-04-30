package com.example.paises.Util;

import com.example.paises.Model.Countries;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class HttpRetro {
    private static final String BASE_URL = "https://restcountries.eu/";

    //Inicializa Retrofit
    public static CountryInterface getCountryClient() {
        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return restAdapter.create(CountryInterface.class);
    }

    // Interface com métodos de requisicao
    public interface CountryInterface {
        @GET("rest/v1/all")
        Call<List<Countries>> getUbs();
    }
}