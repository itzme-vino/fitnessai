package com.app.activityservices.services;

import com.app.activityservices.dto.ActivityRequest;
import com.app.activityservices.dto.ActivityResponse;
import com.app.activityservices.model.Activity;
import com.app.activityservices.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityService {
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private UserValidationService userValidationService;
    private final RabbitTemplate rabbitTemplate;
    @Value("${rabbitmq.exchange.name}")
    private String exchange;
    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    public ActivityResponse createActivity(ActivityRequest request) {

        boolean isValidUser = userValidationService.validateUser(request.getUserId());
        if(!isValidUser)
        {
            throw new RuntimeException("User not found: "+request.getUserId());
        }
        Activity activity = Activity.builder()
                        .userId(request.getUserId())
                        .activityType(request.getActivityType())
                        .caloriesBurned(request.getCaloriesBurned())
                        .additionalMetrics(request.getAdditionalMetrics())
                        .startTime(request.getStartTime())
                        .duration(request.getDuration()).build();
        Activity savedActivity = activityRepository.save(activity);
        //Public to rabbitmq

        try
        {
            rabbitTemplate.convertAndSend(exchange, routingKey, savedActivity);
        }
        catch (Exception e)
        {
            log.info("Failed to publish message to RabbitMQ: {}", e.getMessage());
        }
        return mapActivityToActivityResponse(savedActivity);
    }
    private ActivityResponse mapActivityToActivityResponse(Activity activity) {
        ActivityResponse response = new ActivityResponse();
        response.setId(activity.getId());
        response.setUserId(activity.getUserId());
        response.setActivityType(activity.getActivityType());
        response.setCaloriesBurned(activity.getCaloriesBurned());
        response.setAdditionalMetrics(activity.getAdditionalMetrics());
        response.setStartTime(activity.getStartTime());
        response.setDuration(activity.getDuration());
        response.setCreatedAt(activity.getCreatedAt());
        response.setUpdatedAt(activity.getUpdatedAt());
        return response;
    }

    public List<ActivityResponse> getUserActivities(String userId) {
        List<Activity> activities = activityRepository.findByUserId(userId);
        return activities.stream().map(this::mapActivityToActivityResponse).toList();
    }
    public List<ActivityResponse> getAllActivities() {
        List<Activity> activities = activityRepository.findAll();
        return activities.stream().map(this::mapActivityToActivityResponse).toList();
    }

    public ActivityResponse getActivityById(String activityId)
    {
        return activityRepository.findById(activityId)
                .map(this::mapActivityToActivityResponse)
                .orElseThrow(() -> new RuntimeException("Activity not found for id: " + activityId));
    }
}
