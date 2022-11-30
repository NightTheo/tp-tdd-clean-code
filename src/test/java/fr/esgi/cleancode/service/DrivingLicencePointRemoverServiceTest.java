package fr.esgi.cleancode.service;

import fr.esgi.cleancode.database.InMemoryDatabase;
import fr.esgi.cleancode.exception.ResourceNotFoundException;
import fr.esgi.cleancode.model.DrivingLicence;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class DrivingLicencePointRemoverServiceTest {

    @InjectMocks
    private DrivingLicencePointRemoverService drivingLicencePointRemoverService;

    @Mock
    private InMemoryDatabase database;

    @ParameterizedTest
    @ValueSource(ints = {1,5,12})
    void should_remove_points(int pointsToRemove) {
        final int initialPoints = 12;

        final var id = UUID.randomUUID();
        final var mockInitialDrivingLicence = DrivingLicence
                .builder()
                .id(id)
                .availablePoints(initialPoints)
                .build();

        when(database.findById(id)).thenReturn(Optional.of(mockInitialDrivingLicence));
        when(database.save(eq(id), any(DrivingLicence.class))).thenAnswer(mock -> mock.getArguments()[1]);

        DrivingLicence modified = drivingLicencePointRemoverService.remove(id, pointsToRemove);

        assertThat(modified.getAvailablePoints()).isEqualTo(initialPoints - pointsToRemove);

        verify(database).findById(id);
        verify(database).save(id, any(DrivingLicence.class));
        verifyNoMoreInteractions(database);
    }


    @Test
    void should_throw_ResourceNotFoundException_if_not_found() {
        final var id = UUID.randomUUID();

        when(database.findById(any(UUID.class))).thenReturn(Optional.empty());

        final var e = assertThrows(ResourceNotFoundException.class,
                ()->drivingLicencePointRemoverService.remove(id, 10));

        assertThat(e.getMessage()).isEqualTo("No such Drinving Licence " + id);

        verify(database).findById(id);
        verifyNoMoreInteractions(database);
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,5,12})
    void available_points_should_be_zero_when_remove_more_than_available_points(int pointsToRemove) {
        final int initialPoints = 1;

        final var id = UUID.randomUUID();
        final var mockInitialDrivingLicence = DrivingLicence
                .builder()
                .id(id)
                .availablePoints(initialPoints)
                .build();

        when(database.findById(id)).thenReturn(Optional.of(mockInitialDrivingLicence));
        when(database.save(eq(id), any(DrivingLicence.class))).thenAnswer(mock -> mock.getArguments()[1]);

        DrivingLicence modified = drivingLicencePointRemoverService.remove(id, pointsToRemove);

        assertThat(modified.getAvailablePoints()).isEqualTo(0);

        verify(database).findById(id);
        verify(database).save(id, any(DrivingLicence.class));
        verifyNoMoreInteractions(database);
    }

}