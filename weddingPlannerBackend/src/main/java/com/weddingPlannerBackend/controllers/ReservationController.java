package com.weddingPlannerBackend.controllers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.weddingPlannerBackend.model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.weddingPlannerBackend.dtos.LimousineReservationDto;
import com.weddingPlannerBackend.dtos.StylistReservationDto;
import com.weddingPlannerBackend.dtos.WeddingHallReservationDto;
import com.weddingPlannerBackend.model.AuthenticationToken;
import com.weddingPlannerBackend.services.AuthenticationService;
import com.weddingPlannerBackend.services.ReservationService;

@CrossOrigin
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private ReservationService reservationService;

	@PostMapping(consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> createReservation(@RequestBody HashMap<String, String> map,
			@RequestHeader(value = "Authorization") String customerToken) throws ParseException {
		AuthenticationToken token = authenticationService.getByToken(customerToken);
		if (token == null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{ \"message\": \" Invalid token\" }");
		try{
			 reservationService.createReservation(token, map.get("date"));
		}
		catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( "{ \"message\": \" user already has reservation\" }");
		}

		return ResponseEntity.status(HttpStatus.CREATED).body("OK");
	}

	@DeleteMapping
	public ResponseEntity<Object> deleteReservation(@RequestHeader(value = "Authorization") String customerToken) throws ParseException {
		AuthenticationToken token = authenticationService.getByToken(customerToken);
		if (token == null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{ \"message\": \" Invalid token\" }");
		try{
			reservationService.deleteReservation(token);
		}
		catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( "{ \"message\": \" Invalid delete reservation\" }");
		}

		return ResponseEntity.status(HttpStatus.OK).body("OK");
	}

	@GetMapping(value = "/weddingHall_reservations", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> getWeddingHallReservation(
			@RequestHeader(value = "Authorization") String customerToken) throws Exception {
		List<WeddingHallReservationDto> reservations = new ArrayList<>();
		AuthenticationToken token = authenticationService.getByToken(customerToken);
		try {
			reservations = reservationService.getUserWeddingHallReservations(token);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.ACCEPTED)
					.body(new ArrayList<>());
		}
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(reservations);
	}

	@GetMapping(value = "/limousine_reservations", consumes = "application/json", produces = "application/json")

	public ResponseEntity<Object> getLimousineReservation(@RequestHeader(value = "Authorization") String customerToken)
			throws Exception {
		List<LimousineReservationDto> reservations = new ArrayList<>();
		AuthenticationToken token = authenticationService.getByToken(customerToken);
		try {
			reservations = reservationService.getUserLimousineReservations(token);
			System.out.println("SIZE" + reservations.size());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.ACCEPTED)
					.body(new ArrayList<>());
		}

		return ResponseEntity.status(HttpStatus.ACCEPTED).body(reservations);
	}

	@GetMapping(value = "/stylist_reservations", consumes = "application/json", produces = "application/json")

	public ResponseEntity<Object> getStylistReservation(@RequestHeader(value = "Authorization") String customerToken) {
		List<StylistReservationDto> reservations = new ArrayList<>();
		AuthenticationToken token = authenticationService.getByToken(customerToken);
		try {
			reservations = reservationService.getUserStylistReservations(token);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.ACCEPTED)
					.body(new ArrayList<>());
		}

		return ResponseEntity.status(HttpStatus.ACCEPTED).body(reservations);
	}

	@PostMapping(value = "/reserve_limousine/{activityId}/{timeSlotId}", consumes = "application/json", produces = "application/json")

	
	public ResponseEntity<Object> reserveLimousine(@PathVariable Long activityId, @PathVariable long timeSlotId,
			@RequestHeader(value = "Authorization") String customerToken) throws Exception {
		AuthenticationToken token = authenticationService.getByToken(customerToken);
		if (token == null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{ \"message\": \" Invalid token\" }");
		try {

			if (!reservationService.createLimousineReservation(token, activityId, timeSlotId))

				return ResponseEntity.status(HttpStatus.CONFLICT).body("{ \"message\": \" unavailable \" }");
		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body("{ \"message\": \" User has many on going reservations\" }");

		}
		// return micro reservation Dto
		return null;
	}

	@PostMapping(value = "/reserve_stylist/{activityId}/{timeSlotId}", consumes = "application/json", produces = "application/json")
	// TODO
	public ResponseEntity<Object> reserveStylist(@PathVariable Long activityId, @PathVariable long timeSlotId,
			@RequestHeader(value = "Authorization") String customerToken) throws Exception {
		AuthenticationToken token = authenticationService.getByToken(customerToken);
		if (token == null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{ \"message\": \" Invalid token\" }");
		try {

			if (!reservationService.createStylistReservation(token, activityId, timeSlotId))
				return ResponseEntity.status(HttpStatus.CONFLICT).body("{ \"message\": \" unavailable \" }");

		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body("{ \"message\": \" User has many on going reservations\" }");

		}
		// return micro reservation Dto
		return null;
	}

	@GetMapping(consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> getUserCurrentReservation(
			@RequestHeader(value = "Authorization") String customerToken) {

		return null;
	}

	@PostMapping(value = "/confirm", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> confirmReservation(@RequestHeader(value = "Authorization") String customerToken)
			throws Exception {
		AuthenticationToken token = authenticationService.getByToken(customerToken);
		reservationService.confirmReservation(token);
		return null;

	}

	@DeleteMapping(value = "/delete_stylist_reservation/{Id}")
	public ResponseEntity<Object> deleteStylistReservation(@RequestHeader(value = "Authorization") String customerToken,
															   @PathVariable long Id)
			throws Exception {
		AuthenticationToken token = authenticationService.getByToken(customerToken);
		reservationService.deleteStylistReservation(token, Id);
		return ResponseEntity.status(HttpStatus.OK).body("OK");
	}


	@DeleteMapping(value = "/delete_limousine_reservation/{Id}")
	public ResponseEntity<Object> deleteLimousineReservation(@RequestHeader(value = "Authorization") String customerToken,
															   @PathVariable long Id)
			throws Exception {
		AuthenticationToken token = authenticationService.getByToken(customerToken);
		reservationService.deleteLimousineReservation(token, Id);
		return ResponseEntity.status(HttpStatus.OK).body("OK");
	}


	@DeleteMapping(value = "/delete_wedding_hall_reservation/{Id}")
	public ResponseEntity<Object> deleteWeddingHallReservation(@RequestHeader(value = "Authorization") String customerToken,
															   @PathVariable long Id)
			throws Exception {
		AuthenticationToken token = authenticationService.getByToken(customerToken);
		reservationService.deleteWeddingHallReservation(token, Id);
		return ResponseEntity.status(HttpStatus.OK).body("OK");
	}

	@PostMapping(value = "/reserve_weddingHall/{activityId}/{timeSlotId}", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> reserveWeddingHall(@PathVariable Long activityId, @PathVariable long timeSlotId,
			@RequestHeader(value = "Authorization") String customerToken) throws Exception {
		System.out.println("WEDDINGHALL");
		AuthenticationToken token = authenticationService.getByToken(customerToken);
		if (token == null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{ \"message\": \" Invalid token\" }");

		try {

			if (!reservationService.createWeddingHallReservation(token, activityId, timeSlotId))
				return ResponseEntity.status(HttpStatus.CONFLICT).body("{ \"message\": \" unavailable \" }");

		} catch (Exception e) {
            e.printStackTrace();
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body("{ \"message\": \" User has many on going reservations\" }");

		}
		return ResponseEntity.status(HttpStatus.CREATED).body("OK");
	}

}
