package fr.esgi.cleancode.service;

import fr.esgi.cleancode.database.InMemoryDatabase;
import fr.esgi.cleancode.exception.InvalidDriverSocialSecurityNumberException;
import fr.esgi.cleancode.model.DrivingLicence;
import fr.esgi.cleancode.service.validation.SocialSecurityNumberValidator;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DrivingLicenceCreatorService {

    private final InMemoryDatabase database;
    private final DrivingLicenceIdGenerationService drivingLicenceIdGenerationService;
    private final SocialSecurityNumberValidator socialSecurityNumberValidator;

    public Either<InvalidDriverSocialSecurityNumberException, DrivingLicence> create(String socialSecurityNumber) {
        try {
            socialSecurityNumberValidator.validate(socialSecurityNumber);
        } catch (InvalidDriverSocialSecurityNumberException e) {
            return Either.left(new InvalidDriverSocialSecurityNumberException(e.getMessage()));
        }
        final var drivingLicence  = DrivingLicence
                .builder()
                .id(drivingLicenceIdGenerationService.generateNewDrivingLicenceId())
                .driverSocialSecurityNumber(socialSecurityNumber)
                .build();

        return Either.right(database.save(drivingLicence.getId(), drivingLicence));
    }
}
