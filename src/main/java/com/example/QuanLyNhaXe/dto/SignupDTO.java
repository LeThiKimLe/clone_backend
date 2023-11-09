package com.example.QuanLyNhaXe.dto;

import com.example.QuanLyNhaXe.dto.SignupDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class SignupDTO {
	private String tel;
	private String password;
	private String name;
	private String email;
	private Boolean gender;
}
