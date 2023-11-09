package com.example.QuanLyNhaXe.model;

import java.sql.Date;
import java.sql.Time;

import java.util.List;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "trip")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trip {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "turn")
	private boolean turn;

	@ManyToOne
	@JoinColumn(name = "start_station", referencedColumnName = "id")
	private Station startStation;

	@ManyToOne
	@JoinColumn(name = "end_station", referencedColumnName = "id")
	private Station endStation;

	@ManyToOne
	@JoinColumn(name = "route_id", referencedColumnName = "id")
	private Route route;
	
	@OneToMany(mappedBy = "trip")
	private List<Schedule> schedules;
	
	@OneToMany(mappedBy = "trip")
	private List<Booking> bookings;
	
	@OneToMany(mappedBy = "trip")
	private List<StopStation> stopStations;
	

	

	

}