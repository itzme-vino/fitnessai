package com.app.activityservices.controller;

import com.app.activityservices.dto.ActivityRequest;
import com.app.activityservices.dto.ActivityResponse;
import com.app.activityservices.services.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {
    @Autowired
    private ActivityService activityService;

    @PostMapping("/create")
    public ResponseEntity<ActivityResponse> createActivity(@RequestBody ActivityRequest request, @RequestHeader("X-User-ID") String userId) {
        if(userId != null)
        {
            request.setUserId(userId);
        }
        return ResponseEntity.ok(activityService.createActivity(request));
    }

    @GetMapping
    public ResponseEntity<List<ActivityResponse>> getUserActivities(@RequestHeader("X-User-ID") String userId) {
        return ResponseEntity.ok(activityService.getUserActivities(userId));
    }
//    @GetMapping
//    public ResponseEntity<List<ActivityResponse>> getAllUserActivities() {
//        return ResponseEntity.ok(activityService.getAllActivities());
//    }
    @GetMapping("/{activityId}")
    public ResponseEntity<ActivityResponse> getActivityById(@PathVariable String activityId) {
        return ResponseEntity.ok(activityService.getActivityById(activityId));
    }
}