package com.example.myapplication.api;

import com.example.myapplication.api.dto.response.product.CategoryResponseDto;
import com.example.myapplication.api.dto.response.product.ProductResponseDto;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CategoryApi {

    String BASE_PATH = "/categories";

    @GET(BASE_PATH + "/parent")
    Call<List<CategoryResponseDto>> getAllParentCategories();

    @GET(BASE_PATH + "/menu/{id}")
    Call<List<ProductResponseDto>> getProductsByCategoryId(@Path("id") UUID categoryId);
}
