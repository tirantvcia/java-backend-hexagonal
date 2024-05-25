package com.savily.hexagonal.backend.testing.application;

public class UserRegistrationRequest {
     private final  String email;
     private final String password;
    public UserRegistrationRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
