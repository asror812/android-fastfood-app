package com.example.myapplication.api.dto.response;


import java.util.UUID;

public class AddressResponseDto {
    private UUID id;
    private double longitude;
    private double latitude;

    public AddressResponseDto(){}

    public AddressResponseDto(UUID id, double longitude, double latitude) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}