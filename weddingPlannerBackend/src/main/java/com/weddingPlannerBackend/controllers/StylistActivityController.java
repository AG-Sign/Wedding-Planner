package com.weddingPlannerBackend.controllers;

import com.weddingPlannerBackend.dtos.StylistActivityDto;
import com.weddingPlannerBackend.dtos.WeddingHallActivityDto;
import com.weddingPlannerBackend.mappers.ProviderMapper;
import com.weddingPlannerBackend.mappers.StylistActivityMapper;
import com.weddingPlannerBackend.mappers.WeddingHallActivityMapper;
import com.weddingPlannerBackend.model.AuthenticationToken;
import com.weddingPlannerBackend.model.Provider;
import com.weddingPlannerBackend.model.StylistActivity;
import com.weddingPlannerBackend.model.WeddingHallActivity;
import com.weddingPlannerBackend.services.ActivityService;
import com.weddingPlannerBackend.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
@RequestMapping("/api/stylist_activities")
public class StylistActivityController {
    @Autowired
    private ActivityService activityService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private StylistActivityMapper stylistActivityMapper;

    @Autowired
    private ProviderMapper providerMapper;

    @GetMapping
    public ResponseEntity<Object> getStylistActivities( HttpServletRequest request,
                                                        @RequestParam(required = false) Double min,
                                                        @RequestParam(required = false) Double max,
                                                        @RequestParam(required = false) String provider) {
        min = min == null ? 0 : min;
        max = max == null ? Double.MAX_VALUE : max;
        provider = provider == null ? "" : provider;
        return ResponseEntity.ok().body(activityService.getAllStylistActivities(min, max, provider));
    }

    @GetMapping("/provider_activities")
    public ResponseEntity<Object> getStylistActivitiesForProvider(HttpServletRequest request,
                                                                    @RequestHeader(value = "Authorization")
                                                                    String providerToken) {
        AuthenticationToken token = authenticationService.getByToken(providerToken);
        if (token == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{ \"message\": \" Invalid token\" }");
        Provider currentProvider = token.getProvider();
        return ResponseEntity.ok().body(activityService.getProviderStylistActivities(currentProvider));
    }

    @GetMapping("/{Id}")
    public ResponseEntity<Object> getStylistActivity(HttpServletRequest request, @PathVariable long Id) {
        StylistActivity activity = activityService.getStylistActivity(Id);
        if(activity == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("{ \"message\": \" Activity does not exist\" }");

        return ResponseEntity.ok().body(stylistActivityMapper.toDto(activity));
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> createStylistActivity(@RequestBody StylistActivityDto activityDto,
                                                        @RequestHeader(value = "Authorization") String providerToken) {
        AuthenticationToken token = authenticationService.getByToken(providerToken);
        if (token == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{ \"message\": \" Invalid token\" }");
        Provider currentProvider = token.getProvider();
        activityDto.setProvider(providerMapper.toDto(currentProvider));
        StylistActivity newActivity = activityService.createStylistActivity(activityDto);
        if(newActivity == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("{ \"message\": \" Activity creation failed\" }");

        return ResponseEntity.status(HttpStatus.CREATED).body(stylistActivityMapper.toDto(newActivity));
    }

    @PatchMapping(value = "/edit", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> editStylistActivity(@RequestBody StylistActivityDto activityDto,
                                                          @RequestHeader(value = "Authorization") String providerToken) {
        AuthenticationToken token = authenticationService.getByToken(providerToken);
        if (token == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("{ \"message\": \" Invalid token\" }");
        Provider currentProvider = token.getProvider();
        activityDto.setProvider(providerMapper.toDto(currentProvider));

        Boolean success = activityService.editStylistActivity(activityDto);
        if(success == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("{ \"message\": \" Activity to be edited not found\" }");
        if (!success) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("{ \"message\": \" Activity to be edited not valid\" }");
        return ResponseEntity.status(HttpStatus.OK).body("{}");
    }
}
