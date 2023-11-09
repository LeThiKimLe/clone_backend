package com.example.QuanLyNhaXe.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class RequestStationDTO {
	
	 private String name;
	 private String address;
	 private Integer locationId;
	 private BigDecimal latitude;
	 private BigDecimal longitude;

}
