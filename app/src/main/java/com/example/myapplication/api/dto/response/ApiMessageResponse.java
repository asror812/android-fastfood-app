package com.example.myapplication.api.dto.response;


import java.util.Objects;

public class ApiMessageResponse {

    private String message;

    public ApiMessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ApiMessageResponse) obj;
        return Objects.equals(this.message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message);
    }

    @Override
    public String toString() {
        return "ApiMessageResponse[" + "message=" + message + ']';
    }


}

