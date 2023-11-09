package com.example.QuanLyNhaXe.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.services.kms.model.CreateAliasRequest;
import com.example.QuanLyNhaXe.dto.BookingDTO;
import com.example.QuanLyNhaXe.dto.CreatePaymentDTO;
import com.example.QuanLyNhaXe.dto.TransactionDTO;
import com.example.QuanLyNhaXe.enumration.TransactionStatus;
import com.example.QuanLyNhaXe.enumration.TransactionType;
import com.example.QuanLyNhaXe.exception.BadRequestException;
import com.example.QuanLyNhaXe.exception.NotFoundException;
import com.example.QuanLyNhaXe.model.Bill;
import com.example.QuanLyNhaXe.model.Booking;
import com.example.QuanLyNhaXe.model.Schedule;
import com.example.QuanLyNhaXe.model.Ticket;
import com.example.QuanLyNhaXe.model.Transaction;
import com.example.QuanLyNhaXe.repository.BillRepository;
import com.example.QuanLyNhaXe.repository.BookingRepository;
import com.example.QuanLyNhaXe.repository.TicketRepository;
import com.example.QuanLyNhaXe.repository.TransactionRepository;
import com.example.QuanLyNhaXe.util.Message;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {
	private final TransactionRepository transactionRepository;
	private final BookingRepository bookingRepository;
	private final BillRepository billRepository;
	private final TicketRepository ticketRepository;
	private final BookingService bookingService;
	private final ModelMapper modelMapper;
	private final UtilityService utilityService;

	@Transactional
	public TransactionDTO createTransaction(CreatePaymentDTO createPaymentDTO) {
		Integer priceBill = 0;

		Booking booking = bookingRepository.findByCode(createPaymentDTO.getBookingCode())
				.orElseThrow(() -> new NotFoundException(Message.BOOKING_NOT_FOUND));
		String billReferCode =  utilityService.generateRandomString(7);
		while (billRepository.existsByReferCode(billReferCode)) {
			billReferCode =  utilityService.generateRandomString(7);
		}
		String referenceId = utilityService.generateRandomString(10);
		while (billRepository.existsByReferCode(billReferCode)) {
			referenceId =  utilityService.generateRandomString(10);
		}
		Bill bill = Bill.builder().referCode(billReferCode).build();
		List<Ticket> tickets = booking.getTickets();
		if (!tickets.isEmpty()) {
			for (Ticket ticket : tickets) {
				ticket.setBill(bill);
				priceBill+=ticket.getTicketPrice();
			}
			
		}
		
		
		Transaction transaction = Transaction.builder().paymentTime( utilityService.convertHCMDateTime()).booking(booking)
				.amount(priceBill).paymentMethod(createPaymentDTO.getPaymentMethod()).referenceId(referenceId)
				.transactionType(TransactionType.PAYMENT.getLabel()).build();
		booking.setTransaction(transaction);

		try {
			billRepository.save(bill);
			transactionRepository.save(transaction);
			ticketRepository.saveAll(tickets);
			bookingRepository.save(booking);

		} catch (Exception e) {
			throw new BadRequestException("Đã xảy ra lỗi trong qua trình giao dịch");
		}

		return modelMapper.map(transaction, TransactionDTO.class);

	}

}
