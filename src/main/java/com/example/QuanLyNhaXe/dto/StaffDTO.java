package com.example.QuanLyNhaXe.dto;

import java.sql.Date;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
public class StaffDTO {
	private int staffId;
	private String idCard;
	private String address;
	private String img;
	private Date beginWorkDate;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer adminId;

}
