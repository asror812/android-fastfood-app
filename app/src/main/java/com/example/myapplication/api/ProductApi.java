package com.example.myapplication.api;

import com.example.myapplication.api.dto.response.ProductResponseDto;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProductApi {
    String BASE_PATH = "/products";

    @GET(BASE_PATH)
    Call<List<ProductResponseDto>> getAll();

    @GET(BASE_PATH + "/popular")
    Call<List<ProductResponseDto>> getPopularProducts();

    @GET("/campaign")
    Call<List<ProductResponseDto>> getCampaignProducts();

    @GET("/{id}")
    Call<ProductResponseDto> getById(@Path("id") UUID id);
}
