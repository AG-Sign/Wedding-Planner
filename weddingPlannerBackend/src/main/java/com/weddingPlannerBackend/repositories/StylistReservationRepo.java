package com.weddingPlannerBackend.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.weddingPlannerBackend.model.StylistReservation;
import com.weddingPlannerBackend.model.WeddingHallReservation;

@Repository
public interface StylistReservationRepo extends JpaRepository<StylistReservation, Long> {

	@Query(value = "SELECT COUNT(*) FROM stylist_reservation s INNER JOIN reservation r ON s.reservation_id = r.id WHERE s.stylist_activity_id = :activityId AND s.time_slot_id = :timeSlotId AND r.date =:reservationDate"
			+ "", nativeQuery = true)
	Integer getOverlappingReservations(Long activityId, Long timeSlotId, Date reservationDate);

	@Query(value = "SELECT * FROM stylist_reservation s WHERE s.reservation_id= :reservationId"
			+ "", nativeQuery = true)
	List<StylistReservation> getStylistReservations(Long reservationId);

	@Query(value="SELECT COUNT(*) FROM stylist_reservation s WHERE s.time_slot_id= :timeSlotId"
			+ "", nativeQuery = true)
	Integer getCountByTimeSlotId (Long timeSlotId);

	@Modifying
	@Query(value="DELETE FROM stylist_reservation w WHERE w.reservation_id = :reservationId",
			nativeQuery = true)
	void deleteByReservationId(Long reservationId);
}
