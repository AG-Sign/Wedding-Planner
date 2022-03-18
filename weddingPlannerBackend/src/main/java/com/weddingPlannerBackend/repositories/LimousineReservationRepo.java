package com.weddingPlannerBackend.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.weddingPlannerBackend.model.LimousineReservation;
import com.weddingPlannerBackend.model.Reservation;
import com.weddingPlannerBackend.model.WeddingHallReservation;

@Repository
public interface LimousineReservationRepo extends JpaRepository<LimousineReservation,Long> {
	
	@Query(value="SELECT COUNT(*) FROM limousine_reservation l INNER JOIN reservation r ON l.reservation_id = r.id WHERE l.limousine_activity_id = :activityId AND l.time_slot_id = :timeSlotId AND r.date =:reservationDate"
			+ "", nativeQuery = true)
	Integer getOverlappingReservations(Long activityId,Long timeSlotId,Date reservationDate);
	
	@Query(value="SELECT * FROM limousine_reservation l WHERE l.reservation_id= :reservationId"
			+ "", nativeQuery = true)
	List<LimousineReservation> getLimousineReservations (Long reservationId);

	@Query(value="SELECT COUNT(*) FROM limousine_reservation l WHERE l.time_slot_id= :timeSlotId"
			+ "", nativeQuery = true)
	Integer getCountByTimeSlotId (Long timeSlotId);

	@Modifying
	@Query(value="DELETE FROM limousine_reservation w WHERE w.reservation_id = :reservationId",
			nativeQuery = true)
	void deleteByReservationId(Long reservationId);
}
