package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.user.internal.UserServiceImpl;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * REST controller for managing trainings.
 */
@RestController
@RequestMapping("/v1/trainings")
@RequiredArgsConstructor
public class TrainingController {
    private final UserServiceImpl userS;
    private final TrainingMapper trainM;
    private final TrainingServiceImpl trainS;

    /**
     * Retrieves all trainings.
     *
     * @return A list of all trainings.
     */
    @GetMapping("all")
    public List<TrainingDto> getAllTrainings() {
        return trainS.getAllTrainings()
                .stream()
                .map(trainM::toDto)
                .toList();
    }

    /**
     * Retrieves all trainings for a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of trainings for the specified user.
     */
    @GetMapping("{userId}")
    public List<TrainingDto> getUserTrainings(@PathVariable long userId) {
        return trainS.getAllTrainingsForUser(userId).stream().map(trainM::toDto).toList();
    }

    /**
     * Adds a new training for a specific user.
     *
     * @param trainingDto The training data transfer object.
     * @param userId The ID of the user.
     * @return The added training data transfer object.
     */
    @PostMapping("add_training")
    @ResponseStatus(HttpStatus.CREATED)
    public TrainingDto addTraining(@RequestBody TrainingDto trainingDto, @PathVariable long userId) {
        User user = userS.getUser(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Training training = trainS.addTraining(trainM.toEntity(trainingDto, user, null));
        return trainM.toDto(training);
    }

    /**
     * Retrieves all trainings of a specific activity type.
     *
     * @param activityType The type of the activity.
     * @return A list of trainings of the specified activity type.
     */
    @GetMapping("getActivity")
    public List<TrainingDto> getTrainingsByType(@RequestParam String activityType) {
        ActivityType type = ActivityType.valueOf(activityType);
        return trainS.getAllTrainingTypes(type)
                .stream()
                .map(trainM::toDto)
                .toList();
    }

    /**
     * Updates the activity type of a specific training.
     *
     * @param id The ID of the training to be updated.
     * @param trainingDto The training data transfer object containing the new activity type.
     * @return The updated training data transfer object.
     */
    @PutMapping("update_activity/{id}")
    public TrainingDto updateTraining(@PathVariable Long id, @RequestBody UpdateTrainingDto trainingDto) {
        Training originalTraining = trainS.getTraining(id);
        Training training = trainS.updateTraining(trainM.toUpdateTraining(trainingDto, originalTraining.getUser(), id));
        return trainM.toDto(training);
    }

    /**
     * Retrieves all trainings that finished before a specific date.
     *
     * @param date The date to compare with.
     * @return A list of finished trainings.
     */
    @GetMapping("finish_training/{finishedDate}")
    public List<TrainingDto> getAllFinishedTrainings(@PathVariable("finishedDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        return trainS.getAllFinishTrainings(date).stream().map(trainM::toDto).toList();
    }
}
