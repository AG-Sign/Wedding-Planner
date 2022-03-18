package com.weddingPlannerBackend.controllers;

import com.weddingPlannerBackend.dtos.LimousineActivityDto;
import com.weddingPlannerBackend.dtos.TimeSlotDto;
import com.weddingPlannerBackend.mappers.LimousineActivityMapper;
import com.weddingPlannerBackend.mappers.ProviderMapper;
import com.weddingPlannerBackend.mappers.TimeSlotMapper;
import com.weddingPlannerBackend.model.AuthenticationToken;
import com.weddingPlannerBackend.model.LimousineActivity;
import com.weddingPlannerBackend.model.Provider;
import com.weddingPlannerBackend.model.TimeSlot;
import com.weddingPlannerBackend.services.ActivityService;
import com.weddingPlannerBackend.services.AuthenticationService;
import com.weddingPlannerBackend.services.TimeSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
@RequestMapping("/api/time_slots")
public class TimeSlotController {
    @Autowired
    private TimeSlotService timeSlotService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private TimeSlotMapper timeSlotMapper;

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> createTimeSlot(@RequestBody TimeSlotDto timeSlotDto,
                                                    @RequestHeader(value = "Authorization") String providerToken) {
        AuthenticationToken token = authenticationService.getByToken(providerToken);
        if (token == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{ \"message\": \" Invalid token\" }");
        Provider currentProvider = token.getProvider();
        TimeSlot slot = timeSlotService.CreateTimeSlot(timeSlotDto, currentProvider);
        if(slot == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("{ \"message\": \" Activity creation failed\" }");

        return ResponseEntity.status(HttpStatus.CREATED).body(timeSlotMapper.toDto(slot));
    }

    @DeleteMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> deleteTimeSlot(@RequestBody TimeSlotDto timeSlotDto,
                                                    @RequestHeader(value = "Authorization") String providerToken) {
        AuthenticationToken token = authenticationService.getByToken(providerToken);
        if (token == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("{ \"message\": \"Invalid token\" }");
        Provider currentProvider = token.getProvider();

        Boolean success = timeSlotService.deleteTimeSlot(timeSlotDto, currentProvider);
        if (success == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("{ \"message\": \"TimeSlot deletion failed\" }");
        if (!success) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("{ \"message\": \"TimeSlot has active users\" }");
        return ResponseEntity.status(HttpStatus.OK).body("{}");
    }
}
