package com.example.QuanLyNhaXe.dto;

import java.sql.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignupDriverDTO {
	private String tel;
	private String name;
	private String email;
	private Boolean gender;
	private String idCard;
	private String address;
	private Date beginWorkDate;
	private String img;
	private Date issueDate;
	private String licenseNumber;

}
