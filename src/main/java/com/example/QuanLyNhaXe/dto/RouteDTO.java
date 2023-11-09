package com.example.QuanLyNhaXe.dto;





import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteDTO {
	
	private Integer id;
	private Integer distance;
	private LocationDTO departure;
	private LocationDTO destination;
	private Integer price;
	private String schedule;
	private Integer parents;
	private float hours;
	private BusTypeDTO busType;

}
