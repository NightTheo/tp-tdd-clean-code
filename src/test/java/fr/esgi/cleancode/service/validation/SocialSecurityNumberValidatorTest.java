package fr.esgi.cleancode.service.validation;

import fr.esgi.cleancode.exception.InvalidDriverSocialSecurityNumberException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.vavr.api.VavrAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SocialSecurityNumberValidatorTest {

    @Test
    void should_not_be_null() {
        final var validator = new SocialSecurityNumberValidator();

        final var invalid = validator.validate(null);

        assertThat(invalid).containsInvalidInstanceOf(InvalidDriverSocialSecurityNumberException.class);
        assertEquals("Driver social security number should not be null", invalid.getError().getMessage());
    }

    @Test
    void should_contain_only_digits() {
        final var validator = new SocialSecurityNumberValidator();

        final var invalid = validator.validate("12345678901234A");

        assertThat(invalid).containsInvalidInstanceOf(InvalidDriverSocialSecurityNumberException.class);
        assertEquals("Driver social security number should contain only digits", invalid.getError().getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"12345678901234", "1234567890123456"})
    void should_have_a_length_of_15(String invalidLengthSocialSecurityNumber) {
        final var validator = new SocialSecurityNumberValidator();

        final var invalid = validator.validate(invalidLengthSocialSecurityNumber);

        assertThat(invalid).containsInvalidInstanceOf(InvalidDriverSocialSecurityNumberException.class);
        assertEquals("Driver social security number should contain 15 digits", invalid.getError().getMessage());
    }

    @Test
    void should_validate() {
        final var validator = new SocialSecurityNumberValidator();

        assertThat(validator.validate("123456789012345")).containsValid("123456789012345");
    }
}