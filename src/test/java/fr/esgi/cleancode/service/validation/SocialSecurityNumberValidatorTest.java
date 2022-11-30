package fr.esgi.cleancode.service.validation;

import fr.esgi.cleancode.exception.InvalidDriverSocialSecurityNumberException;
import fr.esgi.cleancode.model.DrivingLicence;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SocialSecurityNumberValidatorTest {

    @Test
    void should_not_be_null() {
        final var validator = new SocialSecurityNumberValidator();

        var e = assertThrows(
                InvalidDriverSocialSecurityNumberException.class,
                () -> validator.validate(null)
        );

        assertEquals("Driver social security number should not be null", e.getMessage());
    }

    @Test
    void should_contain_only_digits() {
        final var validator = new SocialSecurityNumberValidator();

        var e = assertThrows(
                InvalidDriverSocialSecurityNumberException.class,
                () -> validator.validate("12345678901234A")
        );

        assertEquals("Driver social security number should contain only digits", e.getMessage());
    }

    @Test
    void should_have_a_length_of_15() {
        final var validator = new SocialSecurityNumberValidator();

        var eAbove15 = assertThrows(
                InvalidDriverSocialSecurityNumberException.class,
                () -> validator.validate("1234567890123456")
        );

        assertEquals("Driver social security number should contain 15 digits", eAbove15.getMessage());

        var eUnder15 = assertThrows(
                InvalidDriverSocialSecurityNumberException.class,
                () -> validator.validate("12345678901234")
        );

        assertEquals("Driver social security number should contain 15 digits", eUnder15.getMessage());
    }

    @Test
    void should_validate() {
        final var validator = new SocialSecurityNumberValidator();

        assertDoesNotThrow(() -> validator.validate("123456789012345"));
    }
}