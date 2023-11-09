package com.example.QuanLyNhaXe.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.QuanLyNhaXe.dto.SeatMapDTO;
import com.example.QuanLyNhaXe.service.SeatMapService;



import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/seat-map")
@RequiredArgsConstructor
public class SeatMapController {
//	private final SeatMapService seatMapService ;
//	
//	
//	@GetMapping
//    public ResponseEntity<List<SeatMapDTO>> getAll() {
//        return new ResponseEntity<>(seatMapService.getAllSeatMaps(), HttpStatus.OK);
//    }

}
