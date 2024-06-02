package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.TrainingProvider;
import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.internal.UserRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.stream.Collectors;
import java.util.Date;
import java.util.List;

/**
 * Implementation of the {@link TrainingProvider} interface.
 * Provides services related to trainings.
 */
@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingProvider {

    private final TrainingRepository trainingRepository;

    private final UserRepository userRepository;

    /**
     * Retrieves a training by its ID.
     *
     * @param trainingId The ID of the training.
     * @return The training with the specified ID.
     */
    @Override
    public Training getTraining(final Long trainingId) {
        return trainingRepository.getReferenceById(trainingId);
    }

    /**
     * Retrieves all trainings.
     *
     * @return A list of all trainings.
     */
    @Override
    public List<Training> getAllTrainings() {
        return trainingRepository.findAll();
    }

    /**
     * Retrieves all trainings for a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of all trainings for the specified user.
     */
    @Override
    public List<Training> getAllTrainingsForUser(long userId) {
        return trainingRepository.findAll().stream().filter(
                training -> training.getUser().getId().equals(userId)).collect(Collectors.toList());
    }

    /**
     * Adds a new training.
     *
     * @param training The training to be added.
     * @return The added training.
     * @throws IllegalArgumentException if the training already has an ID.
     */
    @Override
    public Training addTraining(Training training) {
        if (training.getId() != null) {
            throw new IllegalArgumentException("Training has already DB ID, update is not permitted!");
        }

        User user = training.getUser();
        if (user.getId() == null) {
            userRepository.save(user);
        }

        return trainingRepository.save(training);
    }

    /**
     * Retrieves all finished trainings before a specified date.
     *
     * @param date The date to compare with.
     * @return A list of finished trainings.
     */
    @Override
    public List<Training> getAllFinishTrainings(Date date){
        return trainingRepository.findAll().stream()
                .filter(training -> training.getEndTime().compareTo(date) > 0)
                .toList();
    }

    /**
     * Retrieves all trainings of a specific activity type.
     *
     * @param activity The activity type.
     * @return A list of trainings of the specified activity type.
     */
    @Override
    public List<Training> getAllTrainingTypes(ActivityType activity) {
        return trainingRepository.findAll().stream().filter(training -> training.getActivityType().equals(activity)).collect(Collectors.toList());
    }

    /**
     * Deletes all trainings for a specific user.
     *
     * @param userId The ID of the user whose trainings will be deleted.
     */
    public void deleteUserTrainings(long userId) {
        List<Training> trainings = trainingRepository.findAll().stream()
                .filter(training -> training.getUser().getId().equals(userId))
                .toList();
        trainingRepository.deleteAll(trainings);
    }

    /**
     * Updates an existing training.
     *
     * @param training The training to be updated.
     * @return The updated training.
     */
    public Training updateTraining(Training training) {
        return trainingRepository.save(training);
    }
}
