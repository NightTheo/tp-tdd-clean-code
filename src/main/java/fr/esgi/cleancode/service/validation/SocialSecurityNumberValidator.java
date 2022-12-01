package fr.esgi.cleancode.service.validation;

import fr.esgi.cleancode.exception.InvalidDriverSocialSecurityNumberException;
import fr.esgi.cleancode.model.DrivingLicence;

import java.util.Objects;

public class SocialSecurityNumberValidator {

    public void validate(String socialSecurityNumber) throws InvalidDriverSocialSecurityNumberException {
        if(Objects.isNull(socialSecurityNumber)){
            throw new InvalidDriverSocialSecurityNumberException("Driver social security number should not be null");
        }

        if(!socialSecurityNumber.matches("\\d+")) {
            throw new InvalidDriverSocialSecurityNumberException("Driver social security number should contain only digits");
        }

        if(socialSecurityNumber.length() != 15) {
            throw new InvalidDriverSocialSecurityNumberException("Driver social security number should contain 15 digits");
        }
    }
}
