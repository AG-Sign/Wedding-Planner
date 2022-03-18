package com.weddingPlannerBackend.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.mail.internet.MimeMessage;

import com.weddingPlannerBackend.dtos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weddingPlannerBackend.mappers.LimousineActivityMapper;
import com.weddingPlannerBackend.mappers.StylistActivityMapper;
import com.weddingPlannerBackend.mappers.TimeSlotMapper;
import com.weddingPlannerBackend.mappers.WeddingHallActivityMapper;
import com.weddingPlannerBackend.model.AuthenticationToken;
import com.weddingPlannerBackend.model.LimousineActivity;
import com.weddingPlannerBackend.model.LimousineReservation;
import com.weddingPlannerBackend.model.Reservation;
import com.weddingPlannerBackend.model.StylistActivity;
import com.weddingPlannerBackend.model.StylistReservation;
import com.weddingPlannerBackend.model.TimeSlot;
import com.weddingPlannerBackend.model.User;
import com.weddingPlannerBackend.model.WeddingHallActivity;
import com.weddingPlannerBackend.model.WeddingHallReservation;
import com.weddingPlannerBackend.repositories.LimousineActivityRepo;
import com.weddingPlannerBackend.repositories.LimousineReservationRepo;
import com.weddingPlannerBackend.repositories.ReservationRepo;
import com.weddingPlannerBackend.repositories.StylistActivityRepo;
import com.weddingPlannerBackend.repositories.StylistReservationRepo;
import com.weddingPlannerBackend.repositories.TimeSlotRepo;
import com.weddingPlannerBackend.repositories.WeddingHallActivityRepo;
import com.weddingPlannerBackend.repositories.WeddingHallReservationRepo;

@Service
@Transactional
public class ReservationService {

	@Autowired
	private ReservationRepo reservationRepo;

	@Autowired
	private TimeSlotRepo timeSlotRepo;

	@Autowired
	private TimeSlotMapper timeSlotMapper;

	@Autowired
	private WeddingHallActivityMapper weddingHallActivityMapper;

	@Autowired
	private LimousineActivityMapper limousineActivityMapper;

	@Autowired
	private StylistActivityMapper stylistActivityMapper;

	@Autowired
	private LimousineActivityRepo limousineActivityRepo;

	@Autowired
	private LimousineReservationRepo limousineReservationRepo;

	@Autowired
	private StylistReservationRepo stylistReservationRepo;

	@Autowired
	private StylistActivityRepo stylistActivityRepo;

	@Autowired
	private WeddingHallActivityRepo weddingHallActivityRepo;

	@Autowired

	private WeddingHallReservationRepo weddingHallReservationRepo;

	@Autowired
	private JavaMailSender javaMailSender;

	public void deleteReservation(AuthenticationToken authToken) throws Exception {
		User user = authToken.getUser();
		Reservation reservation = getOnGoingReservation(user);
		long VALID_DELETE_INTERVAL = 604800000;
		if(reservation == null || reservation.getDate().getTime() - new Date().getTime() < VALID_DELETE_INTERVAL)
			throw new Exception();
		weddingHallReservationRepo.deleteByReservationId(reservation.getId());
		limousineReservationRepo.deleteByReservationId(reservation.getId());
		stylistReservationRepo.deleteByReservationId(reservation.getId());
		reservationRepo.delete(reservation);
	}

	public Reservation createReservation(AuthenticationToken authToken, String date) throws Exception {
		User user = authToken.getUser();
        if(getOnGoingReservation(user) != null)
			throw new Exception();
		return reservationRepo.saveAndFlush(generateNewReservationInfo(user, date));
	}

	// to do get date

	private Reservation generateNewReservationInfo(User user, String date) throws ParseException {
		Reservation reservation = new Reservation();
		reservation.setUser(user);
		reservation.setConfirmed(false);
		Date reservationDate = new SimpleDateFormat("dd/MM/yyyy").parse(date);
		reservation.setDate(reservationDate);
		return reservation;
	}

	private Reservation getOnGoingReservation(User user) throws Exception {
		List<Reservation> reservations = reservationRepo.getOnGoingReservationId(user.getEmail());
		if (reservations.size() > 1)
			throw new Exception();
		if(reservations.size()==0) return null;
		return reservations.get(0);

	}

	public boolean createLimousineReservation(AuthenticationToken authToken, Long activityId, Long timeSlotId)
			throws Exception {

		Reservation reservation = getOnGoingReservation(authToken.getUser());
		System.out.println("Reserv IS" + reservation.getUser().getName());
		Optional<LimousineActivity> limousineActivity = limousineActivityRepo.findById(activityId);
		System.out.println("activity IS " + limousineActivity.get().getId());
		Optional<TimeSlot> timeSlot = timeSlotRepo.findById(timeSlotId);
		if (!isAvaliableLimousineReservation(activityId, timeSlotId, reservation.getDate()))
			return false;

		LimousineReservation limousineReservation = new LimousineReservation();
		limousineReservation.setLimousineActivity(limousineActivity.get());
		limousineReservation.setReservation(reservation);
		limousineReservation.setTimeSlot(timeSlot.get());
		limousineReservationRepo.save(limousineReservation);
		return true;

	}

	public boolean createStylistReservation(AuthenticationToken authToken, Long activityId, Long timeSlotId)
			throws Exception {

		Reservation reservation = getOnGoingReservation(authToken.getUser());
		System.out.println("Reserv IS" + reservation.getUser().getName());
		Optional<StylistActivity> stylistActivity = stylistActivityRepo.findById(activityId);
		System.out.println("activity IS " + stylistActivity.get().getId());
		Optional<TimeSlot> timeSlot = timeSlotRepo.findById(timeSlotId);
		if (!isAvaliableStylistReservation(activityId, timeSlotId, reservation.getDate()))
			return false;
		StylistReservation stylistReservation = new StylistReservation();
		stylistReservation.setStylistActivity(stylistActivity.get());
		stylistReservation.setReservation(reservation);
		stylistReservation.setTimeSlot(timeSlot.get());
		stylistReservationRepo.save(stylistReservation);
		return true;
	}

	public boolean createWeddingHallReservation(AuthenticationToken authToken, Long activityId, Long timeSlotId)
			throws Exception {

		Reservation reservation = getOnGoingReservation(authToken.getUser());
		System.out.println("Reserv IS" + reservation.getUser().getName());
		Optional<WeddingHallActivity> weddingHallActivity = weddingHallActivityRepo.findById(activityId);
		System.out.println("activity IS " + weddingHallActivity.get().getId());
		Optional<TimeSlot> timeSlot = timeSlotRepo.findById(timeSlotId);
		if (!isAvaliableWeddingHallReservation(activityId, timeSlotId, reservation.getDate()))
			return false;
		WeddingHallReservation weddingHallReservation = new WeddingHallReservation();
		weddingHallReservation.setWeddingHallActivity(weddingHallActivity.get());
		weddingHallReservation.setReservation(reservation);
		weddingHallReservation.setTimeSlot(timeSlot.get());
		weddingHallReservationRepo.save(weddingHallReservation);
		return true;
	}

	private boolean isAvaliableWeddingHallReservation(Long activityId, Long timeSlotId, Date date) {
		return weddingHallReservationRepo.getOverlappingReservations(activityId, timeSlotId, date) == 0;
	}

	private boolean isAvaliableLimousineReservation(Long activityId, Long timeSLotId, Date date) {
		return limousineReservationRepo.getOverlappingReservations(activityId, timeSLotId, date) == 0;
	}

	private boolean isAvaliableStylistReservation(Long activityId, Long timeSlotId, Date date) {
		return stylistReservationRepo.getOverlappingReservations(activityId, timeSlotId, date) == 0;
	}

	public List<WeddingHallReservationDto> getUserWeddingHallReservations(AuthenticationToken token) throws Exception {

		List<WeddingHallReservationDto> reservations = new ArrayList<>();
		Reservation reservation = getOnGoingReservation(token.getUser());
		List<WeddingHallReservation> weddingHallReservations = weddingHallReservationRepo
				.getWeddingHallReservations(reservation.getId());

		for (WeddingHallReservation r : weddingHallReservations) {
			WeddingHallReservationDto reservationDto = new WeddingHallReservationDto();
			reservationDto.setId(r.getId());
			WeddingHallActivityDto weddingActivityDto = weddingHallActivityMapper.toDto(r.getWeddingHallActivity());
			weddingActivityDto.setTimeslots(null);
			List<TimeSlotDto> timeSlotDtos = new ArrayList<>();
			timeSlotDtos.add(timeSlotMapper.toDto(r.getTimeSlot()));

			reservationDto.setTimeSlot(timeSlotDtos);
			reservationDto.setWeddingHallActivityDto(weddingActivityDto);
			reservations.add(reservationDto);

		}
		return reservations;

	}

	public List<LimousineReservationDto> getUserLimousineReservations(AuthenticationToken token) throws Exception {
		List<LimousineReservationDto> reservations = new ArrayList<>();
		Reservation reservation = getOnGoingReservation(token.getUser());
		List<LimousineReservation> limousineReservations = limousineReservationRepo
				.getLimousineReservations(reservation.getId());
		for (LimousineReservation r : limousineReservations) {
			LimousineReservationDto reservationDto = new LimousineReservationDto();
			reservationDto.setId(r.getId());
			LimousineActivityDto limousineDto = limousineActivityMapper.toDto(r.getLimousineActivity());
			limousineDto.setTimeslots(null);
			List<TimeSlotDto> timeSlotDtos = new ArrayList<>();
			timeSlotDtos.add(timeSlotMapper.toDto(r.getTimeSlot()));

			reservationDto.setTimeSlot(timeSlotDtos);
			reservationDto.setLimousineActivityDto(limousineDto);
			reservations.add(reservationDto);
		}

		return reservations;

	}

	public List<StylistReservationDto> getUserStylistReservations(AuthenticationToken token) throws Exception {
		List<StylistReservationDto> reservations = new ArrayList<>();
		Reservation reservation = getOnGoingReservation(token.getUser());
		List<StylistReservation> stylistReservations = stylistReservationRepo
				.getStylistReservations(reservation.getId());
		for (StylistReservation r : stylistReservations) {
			StylistReservationDto reservationDto = new StylistReservationDto();
			reservationDto.setId(r.getId());
			StylistActivityDto stylistDto = stylistActivityMapper.toDto(r.getStylistActivity());
			stylistDto.setTimeslots(null);
			reservationDto.setStylistActivityDto(stylistDto);
			List<TimeSlotDto> timeSlotDtos = new ArrayList<>();
			timeSlotDtos.add(timeSlotMapper.toDto(r.getTimeSlot()));
			reservationDto.setTimeSlot(timeSlotDtos);
			reservations.add(reservationDto);
		}

		return reservations;

	}

	public void confirmReservation(AuthenticationToken token) throws Exception {

		Reservation reservation = getOnGoingReservation(token.getUser());
		reservationRepo.confirmReservation(reservation.getId());
		MimeMessage msg = javaMailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(msg, true);

		helper.setTo(token.getUser().getEmail());

		helper.setSubject("CONGRATULATIONS!");

		helper.setText("Congratulations wish you endless happiness", true);

		helper.addAttachment("congrats.jpg", new ClassPathResource("congrats.jpg"));
		javaMailSender.send(msg);
	}

	public void deleteWeddingHallReservation(AuthenticationToken authToken, long reservationId) throws Exception {
		User user = authToken.getUser();
		WeddingHallReservation reservation = weddingHallReservationRepo.findById(reservationId).get();
		long VALID_DELETE_INTERVAL = 604800;
		if(reservation.getReservation().getDate().getTime() - new Date().getTime() < VALID_DELETE_INTERVAL ||
				!reservation.getReservation().getUser().getEmail().equals(user.getEmail()))
			throw new Exception();
		weddingHallReservationRepo.delete(reservation);
	}

	public void deleteLimousineReservation(AuthenticationToken authToken, long reservationId) throws Exception {
		User user = authToken.getUser();
		LimousineReservation reservation = limousineReservationRepo.findById(reservationId).get();
		long VALID_DELETE_INTERVAL = 604800;
		if(reservation.getReservation().getDate().getTime() - new Date().getTime() < VALID_DELETE_INTERVAL ||
				!reservation.getReservation().getUser().getEmail().equals(user.getEmail()))
			throw new Exception();
		limousineReservationRepo.delete(reservation);
	}

	public void deleteStylistReservation(AuthenticationToken authToken, long reservationId) throws Exception {
		User user = authToken.getUser();
		StylistReservation reservation = stylistReservationRepo.findById(reservationId).get();
		long VALID_DELETE_INTERVAL = 604800;
		if(reservation.getReservation().getDate().getTime() - new Date().getTime() < VALID_DELETE_INTERVAL ||
				!reservation.getReservation().getUser().getEmail().equals(user.getEmail()))
			throw new Exception();
		stylistReservationRepo.delete(reservation);
	}
}
