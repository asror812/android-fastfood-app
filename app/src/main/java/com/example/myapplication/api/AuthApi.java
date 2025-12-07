package com.example.myapplication.api;

import com.example.myapplication.api.dto.request.SignInDto;
import com.example.myapplication.api.dto.request.SignUpDto;
import com.example.myapplication.api.dto.request.ValidatePhoneNumberDto;
import com.example.myapplication.api.dto.response.ApiMessageResponse;
import com.example.myapplication.api.dto.response.TokenResponseDto;
import com.example.myapplication.api.dto.response.UserResponseDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {
    String BASE_PATH = "/auth";

    @POST(BASE_PATH + "/validate")
    Call<ApiMessageResponse> validatePhoneNumber(@Body ValidatePhoneNumberDto dto);

    @POST(BASE_PATH + "/sign-up")
    Call<UserResponseDto> signUp(@Body SignUpDto signUpDto);

    @POST(BASE_PATH + "/sign-in")
    Call<TokenResponseDto> signIn(@Body SignInDto signInDto);
}
