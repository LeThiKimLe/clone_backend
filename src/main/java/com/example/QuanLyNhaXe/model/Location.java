package com.example.QuanLyNhaXe.model;

import java.util.List;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "location")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false, length = 45)
    private String name;
    
    @OneToMany(mappedBy = "location")
    private List<Station> stations;
    
    @OneToMany(mappedBy = "departure")
    private List<Route> departureRoutes;
    
    @OneToMany(mappedBy = "destination")
    private List<Route> destinationRoutes;
    
    
}