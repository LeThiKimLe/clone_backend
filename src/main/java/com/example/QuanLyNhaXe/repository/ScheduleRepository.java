package com.example.QuanLyNhaXe.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.QuanLyNhaXe.model.Schedule;
import com.example.QuanLyNhaXe.model.Ticket;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
	List<Schedule> findByTripIdAndDepartDateAndAvailabilityGreaterThanEqual(Integer tripId, Date departDate, Integer availability);
	
//	@Query("SELECT t FROM Schedule s JOIN s.bookings b JOIN b.tickets t WHERE s.id = :scheduleId")
//    List<Ticket> findTicketsByScheduleId(Integer scheduleId);

}
