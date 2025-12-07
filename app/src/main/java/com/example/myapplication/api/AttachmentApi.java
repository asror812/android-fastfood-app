package com.example.myapplication.api;

import java.util.UUID;

import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AttachmentApi {

    String BASE_PATH = "/attachments";

    @GET(BASE_PATH + "/download/{id}")
    void load(@Path("id") UUID id);
}
