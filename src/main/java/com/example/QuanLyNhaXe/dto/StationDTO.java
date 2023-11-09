package com.example.QuanLyNhaXe.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class StationDTO {
	
	 private Integer id;
	 private String name;
	 private String address;
	 private LocationDTO location;
	 private BigDecimal latitude;
	 private BigDecimal longitude;

}
