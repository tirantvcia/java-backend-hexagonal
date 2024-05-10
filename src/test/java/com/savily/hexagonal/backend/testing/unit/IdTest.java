package com.savily.hexagonal.backend.testing.unit;

import com.savily.hexagonal.backend.testing.core.Email;
import com.savily.hexagonal.backend.testing.core.Id;
import com.savily.hexagonal.backend.testing.core.ValidationError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class IdTest {
    @Test
    @DisplayName("generate a valid identifier")
    public void generateUniqueIdentifier() {
        Id id = Id.generateUniqueIdentifier();
        String regex = "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$";
        assertTrue(Pattern.matches(regex, id.toString()));
    }
    @Test
    @DisplayName("create an ID from a given valid identifier")
    public void createIdFromValidIdentifier() {
        Id idSource = Id.generateUniqueIdentifier();
        Id anotherId = Id.createFrom(idSource.toString());
        String regex = "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$";
        assertTrue(Pattern.matches(regex, anotherId.toString()));
        assertEquals(idSource, anotherId);
    }
    @Test
    @DisplayName("fails the creation ID from a given invalid identifier")
    public void doesNotCreateIdFromInvalidIdentifier() {
        Exception exception = assertThrows(ValidationError.class, ()-> {
            Id.createFrom("id-not-valid");
        }) ;
        String actualExemptionMessage = exception.getMessage();
        String expectedExemptionMessage = "Invalid format Id";
        assertEquals(expectedExemptionMessage, actualExemptionMessage);
    }
    @Test
    @DisplayName("does not match two different identifiers")
    public void doesNotMatchTwoDifferentIdentifiers() {
        Id idSource = Id.generateUniqueIdentifier();
        Id anotherId = Id.generateUniqueIdentifier();
        assertNotEquals(idSource, anotherId);
    }

}
