package com.savily.hexagonal.backend.testing.domain.entities;

import com.savily.hexagonal.backend.testing.domain.valueObjects.Email;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Id;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Password;
import com.savily.hexagonal.backend.testing.domain.common.ValidationError;


public class User {
    private final Id id;
    private final Email email;
    private Password password;

    public User(Id id, Email email, Password password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public void changePassword(Password newPassword) {
        ensureIsDifferentPassword(newPassword);
        this.password = newPassword;
    }

    private void ensureIsDifferentPassword(Password newPassword) {
        if(this.isMatchingPassword(newPassword)) {
            throw new ValidationError("New Password must be different as current password");
        }
    }

    public boolean isMatchingPassword(Password newPassword) {
        return this.password.equals(newPassword);
    }

    public Id getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public Password getPassword() {
        return password;
    }

    public boolean isMatchingId(Id id) {
       return this.id.equals(id);
    }

    public boolean isMatchingEmail(Email email) {
        return this.email.equals(email);
    }
}
