package com.example.QuanLyNhaXe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.QuanLyNhaXe.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
	Optional<List<Ticket>> findByBookingCode(String bookingCode);
	boolean existsByScheduleIdAndSeat(Integer scheduleId, String name);

}
