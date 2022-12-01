package fr.esgi.cleancode.service.validation;

import fr.esgi.cleancode.exception.InvalidDriverSocialSecurityNumberException;
import fr.esgi.cleancode.model.DrivingLicence;
import io.vavr.control.Validation;

import java.util.Objects;

import static io.vavr.API.Invalid;
import static io.vavr.API.Valid;

public class SocialSecurityNumberValidator {

    public Validation<InvalidDriverSocialSecurityNumberException, String> validate(String socialSecurityNumber) throws InvalidDriverSocialSecurityNumberException {
        if(Objects.isNull(socialSecurityNumber)){
            return Invalid(new InvalidDriverSocialSecurityNumberException("Driver social security number should not be null"));
        }

        if(!socialSecurityNumber.matches("\\d+")) {
            return Invalid(new InvalidDriverSocialSecurityNumberException("Driver social security number should contain only digits"));
        }

        if(socialSecurityNumber.length() != 15) {
            return Invalid(new InvalidDriverSocialSecurityNumberException("Driver social security number should contain 15 digits"));
        }

        return Valid(socialSecurityNumber);
    }
}
