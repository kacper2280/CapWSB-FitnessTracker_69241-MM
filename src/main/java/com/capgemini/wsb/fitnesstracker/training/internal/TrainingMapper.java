package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.user.internal.UserMapper;
import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TrainingMapper {
    private final UserMapper userMapper;
    TrainingDto toDto(Training training) {
        TrainingDto trainingDto = new TrainingDto();
        trainingDto.setId(training.getId());
        trainingDto.setUser(userMapper.toDto(training.getUser()));
        trainingDto.setAverageSpeed(training.getAverageSpeed());
        trainingDto.setDistance(training.getDistance());
        trainingDto.setStartTime(training.getStartTime());
        trainingDto.setEndTime(training.getEndTime());
        return trainingDto;
    }
    public Training toEntity(TrainingDto trainingDto,  User user, Object o) {
        return new Training(
                user,
                trainingDto.getStartTime(),
                trainingDto.getEndTime(),
                trainingDto.getActivityType(),
                trainingDto.getDistance(),
                trainingDto.getAverageSpeed()
        );
    }
    Training toUpdateTraining(UpdateTrainingDto trainingDto, User user, Long id) {
        return new Training(id,
                user,
                trainingDto.startTime(),
                trainingDto.endTime(),
                trainingDto.activityType(),
                trainingDto.distance(),
                trainingDto.averageSpeed());
    }
}
