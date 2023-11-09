package com.example.QuanLyNhaXe.dto;

import java.math.BigDecimal;

import com.example.QuanLyNhaXe.model.Station;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StopStationDTO {
	
	private Integer id;
	private float arrivalTime;
	private Integer reduceFee;
	private String stationType;
	private StationDTO station;

}
