package com.weddingPlannerBackend.repositories;

import java.util.Date;
import java.util.List;

import com.weddingPlannerBackend.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.weddingPlannerBackend.model.WeddingHallReservation;

@Repository
public interface WeddingHallReservationRepo extends JpaRepository<WeddingHallReservation, Long> {
	
	@Query(value="SELECT COUNT(*) FROM wedding_hall_reservation w INNER JOIN reservation r ON w.reservation_id = r.id WHERE w.wedding_hall_activity_id = :activityId AND w.time_slot_id = :timeSlotId AND r.date =:reservationDate"
			+ "", nativeQuery = true)
	Integer getOverlappingReservations(Long activityId,Long timeSlotId,Date reservationDate);
	
	@Query(value="SELECT * FROM wedding_hall_reservation w WHERE w.reservation_id= :reservationId"
			+ "", nativeQuery = true)
	List<WeddingHallReservation> getWeddingHallReservations (Long reservationId);

	@Query(value="SELECT COUNT(*) FROM wedding_hall_reservation w WHERE w.time_slot_id= :timeSlotId"
			+ "", nativeQuery = true)
	Integer getCountByTimeSlotId (Long timeSlotId);

	@Modifying
	@Query(value="DELETE FROM wedding_hall_reservation w WHERE w.reservation_id = :reservationId",
			nativeQuery = true)
	void deleteByReservationId(Long reservationId);
}
