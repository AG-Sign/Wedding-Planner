package com.weddingPlannerBackend.controllers;

import com.weddingPlannerBackend.dtos.WeddingHallActivityDto;
import com.weddingPlannerBackend.mappers.ProviderMapper;
import com.weddingPlannerBackend.mappers.WeddingHallActivityMapper;
import com.weddingPlannerBackend.model.*;
import com.weddingPlannerBackend.services.ActivityService;
import com.weddingPlannerBackend.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
@RequestMapping("/api/wedding_hall_activities")
public class WeddingHallActivityController {
    @Autowired
    private ActivityService activityService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private WeddingHallActivityMapper weddingHallActivityMapper;

    @Autowired
    private ProviderMapper providerMapper;

    @GetMapping
    public ResponseEntity<Object> getWeddingHallActivities(HttpServletRequest request,
                                                           @RequestParam(required = false) Double min,
                                                           @RequestParam(required = false) Double max,
                                                           @RequestParam(required = false) String provider) {
        min = min == null ? 0 : min;
        max = max == null ? Double.MAX_VALUE : max;
        provider = provider == null ? "" : provider;
        return ResponseEntity.ok().body(activityService.getAllWeddingHallActivities(min, max, provider));
    }

    @GetMapping("/provider_activities")
    public ResponseEntity<Object> getWeddingHallActivitiesForProvider(HttpServletRequest request,
                                                                    @RequestHeader(value = "Authorization")
                                                                    String providerToken) {
        AuthenticationToken token = authenticationService.getByToken(providerToken);
        if (token == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{ \"message\": \" Invalid token\" }");
        Provider currentProvider = token.getProvider();
        return ResponseEntity.ok().body(activityService.getProviderWeddingHallActivities(currentProvider));
    }

    @GetMapping("/{Id}")
    public ResponseEntity<Object> getLimousineActivity(HttpServletRequest request, @PathVariable long Id) {
        WeddingHallActivity activity = activityService.getWeddingHallActivity(Id);
        if(activity == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("{ \"message\": \" Activity does not exist\" }");

        return ResponseEntity.ok().body(weddingHallActivityMapper.toDto(activity));
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> createWeddingHallActivity(@RequestBody WeddingHallActivityDto activityDto,
                                                            @RequestHeader(value = "Authorization") String providerToken) {
        AuthenticationToken token = authenticationService.getByToken(providerToken);
        if (token == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{ \"message\": \" Invalid token\" }");
        Provider currentProvider = token.getProvider();
        activityDto.setProvider(providerMapper.toDto(currentProvider));
        WeddingHallActivity newActivity = activityService.createWeddingHallActivity(activityDto);
        if(newActivity == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("{ \"message\": \" Activity creation failed\" }");

        return ResponseEntity.status(HttpStatus.CREATED).body(weddingHallActivityMapper.toDto(newActivity));
    }

    @PatchMapping(value = "/edit", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> editWeddingHallActivity(@RequestBody WeddingHallActivityDto activityDto,
                                                            @RequestHeader(value = "Authorization") String providerToken) {
        AuthenticationToken token = authenticationService.getByToken(providerToken);
        if (token == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{ \"message\": \" Invalid token\" }");
        Provider currentProvider = token.getProvider();
        activityDto.setProvider(providerMapper.toDto(currentProvider));

        Boolean success = activityService.editWeddingHallActivity(activityDto);
        if(success == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("{ \"message\": \" Activity to be edited not found\" }");
        if (!success) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("{ \"message\": \" Activity to be edited not valid\" }");
        return ResponseEntity.status(HttpStatus.OK).body("{}");
    }
}
