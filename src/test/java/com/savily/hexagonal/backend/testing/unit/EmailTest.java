package com.savily.hexagonal.backend.testing.unit;

import static org.junit.jupiter.api.Assertions.*;


import com.savily.hexagonal.backend.testing.core.Email;
import com.savily.hexagonal.backend.testing.core.ValidationError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;






public class EmailTest {
    @Test
    @DisplayName("creates an email for a given address in a correct format")
    public void createEmailSuccessfully() {
        String address = "example@example.com";
        Email email = Email.create(address);
        assertEquals(address, email.toString());
    }
    @Test
    @DisplayName("does not allow creating an email for a given incorrectly formatted address")
    public void creatingErrorWrongFormatAddress() {
        String wrongFormatAddress = "example_example.com";
        Exception exception = assertThrows(ValidationError.class, ()-> {
            Email.create(wrongFormatAddress);
        }) ;
        String actualExemptionMessage = exception.getMessage();
        String expectedExemptionMessage = "Invalid format email";
        assertEquals(expectedExemptionMessage, actualExemptionMessage);
    }
    @Test
    @DisplayName("considers two emails with the same address as equal")
    public void considersTwoEmailsWithSameAddressAsEquals() {
        String address = "example@example.com";
        Email anEmail = Email.create(address);
        Email otherEmail = Email.create(address);
        assertTrue(anEmail.equals(otherEmail));
    }
}
