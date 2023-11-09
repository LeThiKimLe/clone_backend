package com.example.QuanLyNhaXe.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.Optional;
import org.modelmapper.ModelMapper;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import com.example.QuanLyNhaXe.dto.BookingSimpleDTO;
import com.example.QuanLyNhaXe.dto.BookingDTO;
import com.example.QuanLyNhaXe.dto.CreateBookingDTO;
import com.example.QuanLyNhaXe.dto.SearchBookingDTO;
import com.example.QuanLyNhaXe.dto.TripDTO;
import com.example.QuanLyNhaXe.enumration.BookingStatus;
import com.example.QuanLyNhaXe.enumration.TicketState;
import com.example.QuanLyNhaXe.exception.BadRequestException;
import com.example.QuanLyNhaXe.exception.NotFoundException;
import com.example.QuanLyNhaXe.model.Booking;
import com.example.QuanLyNhaXe.model.Schedule;
import com.example.QuanLyNhaXe.model.Staff;

import com.example.QuanLyNhaXe.model.StopStation;
import com.example.QuanLyNhaXe.model.Ticket;
import com.example.QuanLyNhaXe.model.Trip;
import com.example.QuanLyNhaXe.model.User;
import com.example.QuanLyNhaXe.repository.BookingRepository;
import com.example.QuanLyNhaXe.repository.ScheduleRepository;
import com.example.QuanLyNhaXe.repository.StaffRepository;

import com.example.QuanLyNhaXe.repository.StopStationRepository;
import com.example.QuanLyNhaXe.repository.TicketRepository;
import com.example.QuanLyNhaXe.repository.TripRepository;
import com.example.QuanLyNhaXe.repository.UserRepository;
import com.example.QuanLyNhaXe.util.Message;
import com.example.QuanLyNhaXe.util.ResponseMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService {
	private final BookingRepository bookingRepository;
	private final ScheduleRepository scheduleRepository;

	private final StopStationRepository stopStationRepository;
	private final TicketRepository ticketRepository;
	private final TripRepository tripRepository;
	private final CaptchaService captchaService;
	private final UserService userService;
	private final ModelMapper modelMapper;
	private final EmailService emailService;
	private final UtilityService utilityService;

	@Transactional
	public Object booking(CreateBookingDTO createBookingDTO, String authorization) {
		User user = null;
		Staff staff = null;

		user = userService.getUserByAuthorizationHeader(authorization);
		if (user.getAccount().getRole().getId() != 4 && user.getAccount().getRole().getId() != 3) {
			staff = user.getStaff();
			user = null;
		}

		Trip trip = tripRepository.findById(createBookingDTO.getTripId())
				.orElseThrow(() -> new NotFoundException(Message.TRIP_NOT_FOUND));
		Schedule schedule = scheduleRepository.findById(createBookingDTO.getScheduleId())
				.orElseThrow(() -> new NotFoundException(Message.SCHEDULE_NOT_FOUND));

		StopStation pickStation = stopStationRepository.findById(createBookingDTO.getPickStationId())
				.orElseThrow(() -> new NotFoundException(Message.STATION_NOT_FOUND));
		StopStation dropStation = stopStationRepository.findById(createBookingDTO.getDropStationId())
				.orElseThrow(() -> new NotFoundException(Message.STATION_NOT_FOUND));
		if (createBookingDTO.getSeatName().size() != createBookingDTO.getTicketNumber()) {
			throw new BadRequestException("Số lượng vé và danh sách tên ghế chưa khớp");
		}

		String bookingCode = utilityService.generateRandomString(6);
		while (bookingRepository.existsByCode(bookingCode)) {
			bookingCode = utilityService.generateRandomString(6);
		}
		
		Integer price = schedule.getTicketPrice();
		if (schedule.getSpecialDay() != null) {
			price += schedule.getSpecialDay().getFee();
		}
		price -= pickStation.getReduceFee();
		price -= dropStation.getReduceFee();

		Booking booking = Booking.builder().code(bookingCode).trip(trip).bookingUser(user).conductStaff(staff)
				.pickStation(pickStation).dropStation(dropStation).tel(createBookingDTO.getTel())
				.name(createBookingDTO.getName()).email(createBookingDTO.getEmail())
				.ticketNumber(createBookingDTO.getTicketNumber()).status(BookingStatus.RESERVE.getLabel())
				.bookingDate(utilityService.convertHCMDateTime()).build();
		try {

			if (schedule.getAvailability() - createBookingDTO.getTicketNumber() >= 0) {
				schedule.setAvailability(schedule.getAvailability() - createBookingDTO.getTicketNumber());
				scheduleRepository.save(schedule);
			} else
				throw new BadRequestException("Vé chỉ còn lại: " + schedule.getAvailability().toString());
			bookingRepository.save(booking);
			for (String name : createBookingDTO.getSeatName()) {
				if (ticketRepository.existsByScheduleIdAndSeat(createBookingDTO.getScheduleId(), name))
					throw new BadRequestException(
							"Một hoặc nhiều vé đã chọn đã có người đặt rồi!!! Vui lòng chọn vé khác");
				Ticket ticket = Ticket.builder().booking(booking).schedule(schedule).seat(name).ticketPrice(price)
						.state(TicketState.PENDING_PAYMENT.getLabel()).build();
				ticketRepository.save(ticket);
			}

		}

		catch (DataAccessException e) {
			return new ResponseMessage(Message.INACCURATE_DATA);
		}
		if (emailService.checkEmail(booking.getEmail())) {
			emailService.sendBookingInformation(booking);
		}

		return modelMapper.map(booking, BookingSimpleDTO.class);
	}

	public Object searchTicketsByBooking(SearchBookingDTO searchBookingDTO) {

		if (captchaService.captchaVerification(searchBookingDTO.getCapchaToken())) {
			Booking booking = bookingRepository
					.findByCodeAndTel(searchBookingDTO.getBookingCode(), searchBookingDTO.getTel())
					.orElseThrow(() -> new NotFoundException(Message.BOOKING_NOT_FOUND));
			return modelMapper.map(booking, BookingDTO.class);
		}
		throw new BadRequestException("Captcha không hợp lệ");

	}

	public Object searchBookingHistory(String authorizationHeader) {

		User user = userService.getUserByAuthorizationHeader(authorizationHeader);

		List<Booking> bookings = bookingRepository.findByBookingUserId(user.getId())
				.orElseThrow(() -> new NotFoundException(Message.BOOKING_NOT_FOUND));
		;

		if (bookings.isEmpty()) {
			throw new NotFoundException(Message.BOOKING_NOT_FOUND);
		}

		return bookings.stream().map(booking -> modelMapper.map(booking, BookingDTO.class)).toList();

	}

	public ResponseMessage bookingCancel(String bookingCode) {
		Booking booking = bookingRepository.findByCode(bookingCode)
				.orElseThrow(() -> new NotFoundException(Message.BOOKING_NOT_FOUND));
		if (booking.getStatus().equals(BookingStatus.RESERVE.getLabel())) {
			List<Ticket> tickets = booking.getTickets();
			booking.setStatus(BookingStatus.CANCELED.getLabel());
			bookingRepository.save(booking);
			bookingRepository.save(booking);
			for (Ticket ticket : tickets) {
				ticket.setState(TicketState.CANCELED.getLabel());
			}

			ticketRepository.saveAll(tickets);
			return new ResponseMessage("Đã hủy lượt đặt vé");
		}
		return new ResponseMessage("Yêu cầu hủy không hợp lệ");

	}

	@Transactional
	public Object bookingForGuest(CreateBookingDTO createBookingDTO) {
		User user = null;
		Staff staff = null;

		if (createBookingDTO.getEmail() == "" || createBookingDTO.getName() == "" || createBookingDTO.getTel() == "")
			throw new BadRequestException("Thông tin đặt vé không được để trống");

		Trip trip = tripRepository.findById(createBookingDTO.getTripId())
				.orElseThrow(() -> new NotFoundException(Message.TRIP_NOT_FOUND));
		Schedule schedule = scheduleRepository.findById(createBookingDTO.getScheduleId())
				.orElseThrow(() -> new NotFoundException(Message.SCHEDULE_NOT_FOUND));

		StopStation pickStation = stopStationRepository.findById(createBookingDTO.getPickStationId())
				.orElseThrow(() -> new NotFoundException(Message.STATION_NOT_FOUND));
		StopStation dropStation = stopStationRepository.findById(createBookingDTO.getDropStationId())
				.orElseThrow(() -> new NotFoundException(Message.STATION_NOT_FOUND));

		if (createBookingDTO.getSeatName().size() != createBookingDTO.getTicketNumber()) {
			throw new BadRequestException("Số lượng vé và danh sách tên ghế chưa khớp");
		}

		Integer price = schedule.getTicketPrice();
		if (schedule.getSpecialDay() != null) {
			price += schedule.getSpecialDay().getFee();
		}
		price -= pickStation.getReduceFee();
		price -= dropStation.getReduceFee();

		String bookingCode = utilityService.generateRandomString(6);
		while (bookingRepository.existsByCode(bookingCode)) {
			bookingCode = utilityService.generateRandomString(6);
		}

		Booking booking = Booking.builder().code(bookingCode).trip(trip).bookingUser(user).conductStaff(staff)
				.pickStation(pickStation).dropStation(dropStation).tel(createBookingDTO.getTel())
				.name(createBookingDTO.getName()).email(createBookingDTO.getEmail())
				.ticketNumber(createBookingDTO.getTicketNumber()).status(BookingStatus.RESERVE.getLabel())
				.bookingDate(utilityService.convertHCMDateTime()).build();
		try {

			if (schedule.getAvailability() - createBookingDTO.getTicketNumber() >= 0) {
				schedule.setAvailability(schedule.getAvailability() - createBookingDTO.getTicketNumber());
				scheduleRepository.save(schedule);
			} else
				throw new BadRequestException("Vé chỉ còn lại: " + schedule.getAvailability().toString());
			bookingRepository.save(booking);
			for (String name : createBookingDTO.getSeatName()) {
				if (ticketRepository.existsByScheduleIdAndSeat(createBookingDTO.getScheduleId(), name))
					throw new BadRequestException(
							"Một hoặc nhiều vé đã chọn đã có người đặt rồi!!! Vui lòng chọn vé khác");
				Ticket ticket = Ticket.builder().booking(booking).schedule(schedule).seat(name).ticketPrice(price)
						.state(TicketState.PENDING_PAYMENT.getLabel()).build();
				ticketRepository.save(ticket);

			}

		}

		catch (DataAccessException e) {
			return new ResponseMessage(Message.INACCURATE_DATA);
		}
		if (emailService.checkEmail(booking.getEmail())) {
			emailService.sendBookingInformation(booking);
		}
		return modelMapper.map(booking, BookingSimpleDTO.class);
	}

}
