package com.example.myapplication.api;

import com.example.myapplication.api.dto.CategoryCreateDto;
import com.example.myapplication.api.dto.response.CategoryResponseDto;
import com.example.myapplication.api.dto.response.ProductResponseDto;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CategoryApi {

    String BASE_PATH = "/categories";

    @POST(BASE_PATH)
    Call<CategoryResponseDto> create(@Body CategoryCreateDto createDTO);

    @GET(BASE_PATH)
    Call<List<CategoryResponseDto>> getAll();

    @GET(BASE_PATH + "/parent")
    Call<List<CategoryResponseDto>> getAllParentCategories();

    @GET(BASE_PATH + "{id}")
    Call<CategoryResponseDto> getById(@Path("id") UUID id);

    @GET(BASE_PATH + "/menu/{id}")
    Call<List<ProductResponseDto>> getProductsByCategoryId(@Path("id") UUID categoryId);
}
