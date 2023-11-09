package com.example.QuanLyNhaXe.dto;

import java.util.Date;

import com.example.QuanLyNhaXe.enumration.BusOverallState;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusQualityDTO {

	private Integer id;
	private String brake;
	private String lighting;
	private String tire;
	private String steering;
	private String mirror;
	private String airCondition;
	private String electric;
	private String fuel;
	private Date updatedAt;
	private String overallState;
	

}
