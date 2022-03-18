package com.weddingPlannerBackend.controllers;

import com.weddingPlannerBackend.dtos.LimousineActivityDto;
import com.weddingPlannerBackend.dtos.StylistActivityDto;
import com.weddingPlannerBackend.mappers.LimousineActivityMapper;
import com.weddingPlannerBackend.mappers.ProviderMapper;
import com.weddingPlannerBackend.model.AuthenticationToken;
import com.weddingPlannerBackend.model.LimousineActivity;
import com.weddingPlannerBackend.model.Provider;
import com.weddingPlannerBackend.services.ActivityService;
import com.weddingPlannerBackend.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

@CrossOrigin
@RestController
@RequestMapping("/api/limousine_activities")
public class LimousineActivityController {
    @Autowired
    private ActivityService activityService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private LimousineActivityMapper limousineActivityMapper;

    @Autowired
    private ProviderMapper providerMapper;

    @GetMapping
    public ResponseEntity<Object> getLimousineActivities(HttpServletRequest request,
                                                         @RequestParam(required = false) Double min,
                                                         @RequestParam(required = false) Double max,
                                                         @RequestParam(required = false) String provider) {
        min = min == null ? 0 : min;
        max = max == null ? Double.MAX_VALUE : max;
        provider = provider == null ? "" : provider;
        return ResponseEntity.ok().body(activityService.getAllLimousineActivities(min, max, provider));
    }

    @GetMapping("/provider_activities")
    public ResponseEntity<Object> getLimousineActivitiesForProvider(HttpServletRequest request,
                                                                    @RequestHeader(value = "Authorization")
                                                                    String providerToken) {
        AuthenticationToken token = authenticationService.getByToken(providerToken);
        if (token == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{ \"message\": \" Invalid token\" }");
        Provider currentProvider = token.getProvider();
        return ResponseEntity.ok().body(activityService.getProviderLimousineActivities(currentProvider));
    }

    @GetMapping("/{Id}")
    public ResponseEntity<Object> getLimousineActivity(HttpServletRequest request, @PathVariable long Id) {
        LimousineActivity activity = activityService.getLimousineActivity(Id);
        if(activity == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("{ \"message\": \" Activity does not exist\" }");

        return ResponseEntity.ok().body(limousineActivityMapper.toDto(activity));
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> createLimousineActivity(@RequestBody LimousineActivityDto activityDto,
                                                          @RequestHeader(value = "Authorization") String providerToken) {
        AuthenticationToken token = authenticationService.getByToken(providerToken);
        if (token == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{ \"message\": \" Invalid token\" }");
        Provider currentProvider = token.getProvider();
        activityDto.setProvider(providerMapper.toDto(currentProvider));
        LimousineActivity newActivity = activityService.createLimousineActivity(activityDto);
        if(newActivity == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("{ \"message\": \" Activity creation failed\" }");

        return ResponseEntity.status(HttpStatus.CREATED).body(limousineActivityMapper.toDto(newActivity));
    }

    @PatchMapping(value = "/edit", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> editLimousineActivity(@RequestBody LimousineActivityDto activityDto,
                                                      @RequestHeader(value = "Authorization") String providerToken) {
        AuthenticationToken token = authenticationService.getByToken(providerToken);
        if (token == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("{ \"message\": \" Invalid token\" }");
        Provider currentProvider = token.getProvider();
        activityDto.setProvider(providerMapper.toDto(currentProvider));

        Boolean success = activityService.editLimousineActivity(activityDto);
        if(success == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("{ \"message\": \" Activity to be edited not found\" }");
        if (!success) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("{ \"message\": \" Activity to be edited not valid\" }");
        return ResponseEntity.status(HttpStatus.OK).body("{}");
    }
}
