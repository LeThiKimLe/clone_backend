package com.example.QuanLyNhaXe.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.QuanLyNhaXe.dto.CreateBookingDTO;
import com.example.QuanLyNhaXe.dto.CreatePaymentDTO;
import com.example.QuanLyNhaXe.service.BookingService;
import com.example.QuanLyNhaXe.service.TicketService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {
	private final TicketService ticketService;
	@PutMapping("/payment")
	public ResponseEntity<Object> payment(@RequestBody CreatePaymentDTO createPaymentDTO){
		return new ResponseEntity<>(ticketService.paymentTicket(createPaymentDTO), HttpStatus.OK);
	}

}
