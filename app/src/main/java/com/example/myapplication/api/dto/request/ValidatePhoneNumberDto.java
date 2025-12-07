package com.example.myapplication.api.dto.request;

public class ValidatePhoneNumberDto {

    private String phoneNumber;

    private Integer otp;

    public ValidatePhoneNumberDto(){}

    public ValidatePhoneNumberDto(String phoneNumber, Integer otp) {
        this.phoneNumber = phoneNumber;
        this.otp = otp;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getOtp() {
        return otp;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }
}
