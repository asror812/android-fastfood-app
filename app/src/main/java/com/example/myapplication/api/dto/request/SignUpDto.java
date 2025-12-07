package com.example.myapplication.api.dto.request;


import java.io.Serializable;

public class SignUpDto extends UserDto implements Serializable {

    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
