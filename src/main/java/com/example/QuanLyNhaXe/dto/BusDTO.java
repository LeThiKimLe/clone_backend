package com.example.QuanLyNhaXe.dto;

import java.math.BigDecimal;
import java.util.List;

import com.example.QuanLyNhaXe.enumration.BusAvailability;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusDTO {

	private Integer id;
	private int manufactureYear;
	private String color;
	private String availability;
	private LocationDTO currentPosition;
	private BusQualityDTO state;
	private DriverDTO driver;
	

}
