package com.app.activityservices.dto;

import com.app.activityservices.model.ActivityType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ActivityRequest {
    private String userId;
    private ActivityType activityType;
    private Integer duration;
    private Integer caloriesBurned;
    private Map<String, Object> additionalMetrics;
    private LocalDateTime startTime;
}
