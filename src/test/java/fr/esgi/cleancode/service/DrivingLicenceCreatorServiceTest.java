package fr.esgi.cleancode.service;

import fr.esgi.cleancode.database.InMemoryDatabase;
import fr.esgi.cleancode.exception.InvalidDriverSocialSecurityNumberException;
import fr.esgi.cleancode.model.DrivingLicence;
import fr.esgi.cleancode.service.validation.SocialSecurityNumberValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DrivingLicenceCreatorServiceTest {

    @InjectMocks
    private DrivingLicenceCreatorService creatorService;

    @Mock
    private DrivingLicenceIdGenerationService idGenerationService;

    @Mock
    private InMemoryDatabase database;

    @Mock
    private SocialSecurityNumberValidator socialSecurityNumberValidator;

    @Test
    void should_create() {
        final var id = UUID.randomUUID();
        final var socialSecurityNumber = "123456789012345";

        final var mockDrivingLicence = DrivingLicence
                .builder()
                .id(id)
                .driverSocialSecurityNumber(socialSecurityNumber)
                .build();

        when(idGenerationService.generateNewDrivingLicenceId()).thenReturn(id);
        when(database.save(eq(id), any(DrivingLicence.class))).thenReturn(mockDrivingLicence);
        doNothing().when(socialSecurityNumberValidator).validate(socialSecurityNumber);

        final var actual = creatorService.create(socialSecurityNumber);

        assertThat(actual).isEqualTo(mockDrivingLicence);
        assertThat(actual.getAvailablePoints()).isEqualTo(12);

        verify(database).save(id, mockDrivingLicence);
        verifyNoMoreInteractions(database);
        verify(idGenerationService).generateNewDrivingLicenceId();
        verifyNoMoreInteractions(idGenerationService);
        verify(socialSecurityNumberValidator).validate(socialSecurityNumber);
        verifyNoMoreInteractions(socialSecurityNumberValidator);
    }

    @Test
    void should_not_create() {
        final var socialSecurityNumber = "12345";

        doThrow(InvalidDriverSocialSecurityNumberException.class).when(socialSecurityNumberValidator).validate(anyString());

        assertThrows(InvalidDriverSocialSecurityNumberException.class, () -> creatorService.create(socialSecurityNumber));

        verify(socialSecurityNumberValidator).validate(socialSecurityNumber);
        verifyNoInteractions(database);
        verifyNoInteractions(idGenerationService);
    }
}