package fr.esgi.cleancode.service;


import fr.esgi.cleancode.database.InMemoryDatabase;
import fr.esgi.cleancode.exception.ResourceNotFoundException;
import fr.esgi.cleancode.model.DrivingLicence;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class DrivingLicencePointRemoverService {

    private final InMemoryDatabase database;
    public DrivingLicence remove(UUID id, int pointsToRemove) {
        final var drivingLicence = database.findById(id);

        if (drivingLicence.isEmpty()) {
            throw new ResourceNotFoundException("No such Driving Licence " + id);
        }

        final var modifiedPointsCount = this.calculatePoints(drivingLicence.get().getAvailablePoints(), pointsToRemove);

        final var modifiedDrivingLicence = drivingLicence.get()
                .withAvailablePoints(modifiedPointsCount);

        return database.save(id, modifiedDrivingLicence);
    }

    private int calculatePoints(int initialPoints, int pointsToRemove) {

        if (initialPoints < pointsToRemove) {
            return 0;
        }

        return initialPoints - pointsToRemove;
    }
}
