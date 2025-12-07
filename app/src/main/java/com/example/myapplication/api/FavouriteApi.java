package com.example.myapplication.api;

import com.example.myapplication.api.dto.response.ApiMessageResponse;
import com.example.myapplication.api.dto.response.ProductResponseDto;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FavouriteApi {

    String BASE_PATH = "/favorites";
    @GET(BASE_PATH)
    Call<List<ProductResponseDto>> getFavoriteProducts();

    @POST(BASE_PATH + "/{productId}")
    Call<ApiMessageResponse> addFavorite(@Path("productId") UUID productId);
}
