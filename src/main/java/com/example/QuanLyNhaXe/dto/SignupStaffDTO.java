package com.example.QuanLyNhaXe.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class SignupStaffDTO {

	private String name;
	private String email;
	private String tel;
	private Boolean gender;
	private String idCard;
	private String address;
	private Date beginWorkDate;
	private String img;
}
