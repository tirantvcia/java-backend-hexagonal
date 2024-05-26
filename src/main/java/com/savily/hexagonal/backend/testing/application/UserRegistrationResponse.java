package com.savily.hexagonal.backend.testing.application;

public class UserRegistrationResponse {
     private final  String email;
     private final String id;
    public UserRegistrationResponse(String email, String id) {
        this.email = email;
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }
}
