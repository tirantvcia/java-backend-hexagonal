package com.savily.hexagonal.backend.testing.core.entities;

import com.savily.hexagonal.backend.testing.core.valueObjects.Email;
import com.savily.hexagonal.backend.testing.core.valueObjects.Id;
import com.savily.hexagonal.backend.testing.core.valueObjects.Password;
import com.savily.hexagonal.backend.testing.core.common.ValidationError;

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
}
