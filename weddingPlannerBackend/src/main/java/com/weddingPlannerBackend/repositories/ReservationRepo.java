package com.weddingPlannerBackend.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.weddingPlannerBackend.model.Reservation;

@Repository
public interface ReservationRepo extends JpaRepository<Reservation, Long> {

	@Query(value = "SELECT * FROM reservation r LEFT JOIN user u ON r.user_email = u.email WHERE r.user_email = :userEmail AND r.date > current_date()"
			+ "", nativeQuery = true)
	List<Reservation> getOnGoingReservationId(String userEmail);

	@Transactional
	@Modifying
	@Query(value = "UPDATE RESERVATION r SET r.confirmed = 1 WHERE r.id =:reservationId" + "", nativeQuery = true)
	void confirmReservation(Long reservationId);

	@Query(value = "SELECT r.user_email FROM RESERVATION r where r.date = CURDATE() + 1" + "", nativeQuery = true)
	List<String> getUserEmailsOfTomorrowReservations();

}
