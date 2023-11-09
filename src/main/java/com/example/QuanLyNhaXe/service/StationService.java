package com.example.QuanLyNhaXe.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.QuanLyNhaXe.dto.LocationDTO;
import com.example.QuanLyNhaXe.dto.RequestStationDTO;
import com.example.QuanLyNhaXe.dto.StationDTO;
import com.example.QuanLyNhaXe.exception.NotFoundException;
import com.example.QuanLyNhaXe.model.Location;
import com.example.QuanLyNhaXe.model.Station;
import com.example.QuanLyNhaXe.repository.CustomerRepository;
import com.example.QuanLyNhaXe.repository.DriverRepository;
import com.example.QuanLyNhaXe.repository.LocationRepository;
import com.example.QuanLyNhaXe.repository.StaffRepository;
import com.example.QuanLyNhaXe.repository.StationRepository;
import com.example.QuanLyNhaXe.repository.UserRepository;
import com.example.QuanLyNhaXe.util.Message;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StationService {
	private final LocationRepository locationRepository;
	private final StationRepository stationRepository;
	private final ModelMapper modelMapper;
	public StationDTO createStation(RequestStationDTO stationDTO) {
		Location location=locationRepository.findById(stationDTO.getLocationId())
				.orElseThrow(() -> new NotFoundException(Message.LOCATION_NOT_FOUND));
		
		Station createStation=Station.builder()
				.name(stationDTO.getName())
				.address(stationDTO.getAddress())
				.latitude(stationDTO.getLatitude())
				.longitude(stationDTO.getLongitude())
				.location(location)
				.build();
		stationRepository.save(createStation);
		return modelMapper.map(createStation, StationDTO.class);
		
		
	}

}
