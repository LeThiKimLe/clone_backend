package com.example.QuanLyNhaXe.controller;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.QuanLyNhaXe.dto.GetTripDTO;
import com.example.QuanLyNhaXe.dto.TripDTO;
import com.example.QuanLyNhaXe.service.TripService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/trips")
@RequiredArgsConstructor
public class TripController {
	private final TripService tripService;
	
	
	@GetMapping
    public ResponseEntity<List<TripDTO>> getTRipsForRoute(@RequestBody GetTripDTO getTripDTO) {
        return new ResponseEntity<>(tripService.getTripsForRoute(getTripDTO), HttpStatus.OK);
    }
	

}
