package com.example.myapplication.api.dto.request;


import java.time.LocalDate;

public class UserDto {
    private String name;

    private String phoneNumber;

    private LocalDate birthDate;

    public UserDto(String name, String phoneNumber, LocalDate birthDate) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
    }

    public UserDto(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
