package com.example.QuanLyNhaXe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchBookingDTO {
	String tel;
	String bookingCode;
	String capchaToken;

}
