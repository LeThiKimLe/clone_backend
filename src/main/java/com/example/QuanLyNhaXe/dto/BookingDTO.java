package com.example.QuanLyNhaXe.dto;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

import com.example.QuanLyNhaXe.model.Trip;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {
	private String code;
	private StopStationDTO pickStation;
	private StopStationDTO dropStation;
	private LocalDateTime bookingDate;
	private Integer ticketNumber;
	private boolean isTicketing;
	private String name;
	private String email;   
	private String tel;
	private String status;
	private UserDTO bookingUser;
	private StaffDTO conductStaff;
	private TransactionDTO transaction;
	private List<TicketDTO> tickets;
	TripBookingDTO trip;

}
