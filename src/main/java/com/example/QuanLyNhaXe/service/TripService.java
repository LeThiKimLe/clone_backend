package com.example.QuanLyNhaXe.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.QuanLyNhaXe.dto.BusDTO;
import com.example.QuanLyNhaXe.dto.GetTripDTO;
import com.example.QuanLyNhaXe.dto.ScheduleDTO;
import com.example.QuanLyNhaXe.dto.TripDTO;
import com.example.QuanLyNhaXe.exception.NotFoundException;
import com.example.QuanLyNhaXe.model.Booking;
import com.example.QuanLyNhaXe.model.Schedule;
import com.example.QuanLyNhaXe.model.Trip;
import com.example.QuanLyNhaXe.repository.BookingRepository;
import com.example.QuanLyNhaXe.repository.ScheduleRepository;
import com.example.QuanLyNhaXe.repository.TripRepository;
import com.example.QuanLyNhaXe.util.Message;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class TripService {
	private final TripRepository tripRepository;
	private final ModelMapper modelMapper;
	private final ScheduleRepository scheduleRepository;
	private final BookingRepository bookingRepository;
	
	public List<TripDTO> getAllTrips() {
        List<Trip> trips =tripRepository.findAll();
        if (trips.isEmpty()){
            throw new NotFoundException(Message.ROUTE_NOT_FOUND);
        }
        return trips.stream()
        		.map(trip -> modelMapper.map(trip, TripDTO.class))
        		.toList();
    }
	
	
	public List<TripDTO> getTripsForRoute(GetTripDTO getTripDTO) {
		List<Trip> trips = tripRepository.findByRouteIdAndTurn(getTripDTO.getRouteId(), getTripDTO.getTurn());
		if (trips.isEmpty()) {
			throw new NotFoundException(Message.TRIP_NOT_FOUND);
		}
		Integer routeParentId = trips.stream().findFirst().get().getRoute().getParents();	
		if (routeParentId != null) {
			List<Trip> tripsParent = tripRepository.findByRouteIdAndTurn(routeParentId, getTripDTO.getTurn());
			if (!tripsParent.isEmpty())
				trips.addAll(tripsParent);
		}
		List<TripDTO> tripsDTO = trips.stream().map(trip -> {
			List<Schedule> schedules = scheduleRepository.findByTripIdAndDepartDateAndAvailabilityGreaterThanEqual(
					trip.getId(), getTripDTO.getDepartDate(), getTripDTO.getAvailability());
			
			trip.setSchedules(schedules);
			return modelMapper.map(trip, TripDTO.class);
		}).filter(tripDTO -> !tripDTO.getSchedules().isEmpty()).toList();
		if (tripsDTO.isEmpty()) {
			throw new NotFoundException(Message.TRIP_NOT_FOUND);
		}
		return tripsDTO;

	}
	
	

}
