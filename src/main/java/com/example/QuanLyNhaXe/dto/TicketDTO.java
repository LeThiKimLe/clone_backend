package com.example.QuanLyNhaXe.dto;

import com.example.QuanLyNhaXe.model.Schedule;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketDTO {
	private Integer id;
	private String seat;
	private String state;
	private boolean checkedIn;
    private ScheduleTicketsDTO schedule;

}
