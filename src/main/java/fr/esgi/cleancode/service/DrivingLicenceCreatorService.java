package fr.esgi.cleancode.service;

import fr.esgi.cleancode.database.InMemoryDatabase;
import fr.esgi.cleancode.model.DrivingLicence;
import fr.esgi.cleancode.service.validation.SocialSecurityNumberValidator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DrivingLicenceCreatorService {

    private final InMemoryDatabase database;
    private final DrivingLicenceIdGenerationService drivingLicenceIdGenerationService;
    private final SocialSecurityNumberValidator socialSecurityNumberValidator;

    public DrivingLicence create(String socialSecurityNumber) {
        socialSecurityNumberValidator.validate(socialSecurityNumber);
        final var drivingLicence  = DrivingLicence
                .builder()
                .id(drivingLicenceIdGenerationService.generateNewDrivingLicenceId())
                .driverSocialSecurityNumber(socialSecurityNumber)
                .build();

        return database.save(drivingLicence.getId(), drivingLicence);
    }
}
