package com.example.QuanLyNhaXe.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EditAccountDTO {
	private String tel;
	private String name;
	private String email;
	private String address;
	private Boolean gender;
	private MultipartFile file;
}
