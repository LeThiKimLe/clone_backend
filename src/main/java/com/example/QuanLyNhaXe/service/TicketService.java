package com.example.QuanLyNhaXe.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.QuanLyNhaXe.dto.CreateBookingDTO;
import com.example.QuanLyNhaXe.dto.CreatePaymentDTO;
import com.example.QuanLyNhaXe.dto.TransactionDTO;
import com.example.QuanLyNhaXe.enumration.BookingStatus;
import com.example.QuanLyNhaXe.enumration.TicketState;
import com.example.QuanLyNhaXe.exception.BadRequestException;
import com.example.QuanLyNhaXe.exception.NotFoundException;
import com.example.QuanLyNhaXe.model.Booking;
import com.example.QuanLyNhaXe.model.Ticket;
import com.example.QuanLyNhaXe.model.Transaction;
import com.example.QuanLyNhaXe.repository.BookingRepository;
import com.example.QuanLyNhaXe.repository.TicketRepository;
import com.example.QuanLyNhaXe.util.Message;
import com.example.QuanLyNhaXe.util.ResponseMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketService {
	private final TicketRepository ticketRepository;
	private final BookingRepository bookingRepository;
	private final TransactionService transactionService;
	private final UtilityService utilityService;

	
	public Object paymentTicket(CreatePaymentDTO createPaymentDTO) {
		Booking booking = bookingRepository.findByCode(createPaymentDTO.getBookingCode())
				.orElseThrow(() -> new NotFoundException(Message.BOOKING_NOT_FOUND));
		if(booking.getStatus().equals(BookingStatus.SUCCESS.getLabel())) {
			throw new BadRequestException("Thanh toán không hợp lệ");
			
		}
			
		LocalDateTime currentDateTime =utilityService.convertHCMDateTime();
		LocalDateTime bookingDateTime=booking.getBookingDate();
		boolean isExpired = bookingDateTime.plusMinutes(10).isBefore(currentDateTime);
		if(!isExpired) {
			List<Ticket> tickets = booking.getTickets();
			for (Ticket ticket : tickets) {
				ticket.setState(TicketState.PAID.getLabel());
				ticketRepository.save(ticket);
			}
			booking.setStatus(BookingStatus.SUCCESS.getLabel());
			bookingRepository.save(booking);
			return transactionService.createTransaction(createPaymentDTO);
			
		}
		throw new BadRequestException("Thanh toán không hợp lệ");
		
	}
	
	
	

}
