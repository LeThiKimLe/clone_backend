package com.example.QuanLyNhaXe.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.QuanLyNhaXe.dto.EditAccountDTO;
import com.example.QuanLyNhaXe.dto.RequestStationDTO;
import com.example.QuanLyNhaXe.dto.StationDTO;
import com.example.QuanLyNhaXe.dto.UserDTO;
import com.example.QuanLyNhaXe.service.StaffService;
import com.example.QuanLyNhaXe.service.StationService;
import com.example.QuanLyNhaXe.service.UserService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
@SecurityRequirement(name="bearerAuth")
@RestController
@RequestMapping("/station")
@RequiredArgsConstructor
public class StationController {
	private final StationService stationService;
	
	@PostMapping("/create")
	public ResponseEntity<StationDTO> createStation(@RequestBody RequestStationDTO requestStationDTO){
		return new ResponseEntity<>(stationService.createStation(requestStationDTO),HttpStatus.OK);
	}
	
	
}
