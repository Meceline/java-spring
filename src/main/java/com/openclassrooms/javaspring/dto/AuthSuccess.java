package com.openclassrooms.javaspring.dto;

public class AuthSuccess {
    public String token;

    public AuthSuccess(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
