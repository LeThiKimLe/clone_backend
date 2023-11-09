package com.example.QuanLyNhaXe.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.QuanLyNhaXe.dto.BookingDTO;
import com.example.QuanLyNhaXe.dto.CreateBookingDTO;
import com.example.QuanLyNhaXe.dto.SearchBookingDTO;
import com.example.QuanLyNhaXe.service.BookingService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor

public class BookingController {
	private final BookingService bookingService;
	@SecurityRequirement(name="bearerAuth")
	@PostMapping("/booking-users")
	public ResponseEntity<Object> createBooking(@RequestBody CreateBookingDTO createBookingDTO, @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
		return new ResponseEntity<>(bookingService.booking(createBookingDTO,authorization), HttpStatus.OK);
	}
	
	@PostMapping("/booking-guest")
	public ResponseEntity<Object> createBooking(@RequestBody CreateBookingDTO createBookingDTO) {
		return new ResponseEntity<>(bookingService.bookingForGuest(createBookingDTO), HttpStatus.OK);
	}

	@PutMapping("/cancel")
	public ResponseEntity<Object> bookingCancle(@RequestParam String bookingCode) {
		return new ResponseEntity<>(bookingService.bookingCancel(bookingCode), HttpStatus.OK);
	}
	
	@PostMapping("/tickets")
	public ResponseEntity<Object> searchTickets(@RequestBody SearchBookingDTO searchBookingDTO) {
		return new ResponseEntity<>(bookingService.searchTicketsByBooking(searchBookingDTO), HttpStatus.OK);
	}
	
	@SecurityRequirement(name="bearerAuth")
	@PostMapping("/booking-history")
	public ResponseEntity<Object> searchBookingHistory( @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
		return new ResponseEntity<>(bookingService.searchBookingHistory(authorization), HttpStatus.OK);
	}

}
