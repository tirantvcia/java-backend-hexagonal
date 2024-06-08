package com.savily.hexagonal.backend.testing.application;

public class UserPasswordChangeRequest {
    private final  String email;
    private final String oldPassword;
    private final String newPassword;

    public UserPasswordChangeRequest(String email, String oldPassword, String newPassword) {
        this.email = email;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getEmail() {
        return email;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
