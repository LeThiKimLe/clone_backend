package com.example.QuanLyNhaXe.model;

import java.util.List;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "route")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "distance", nullable = false)
    private Integer distance;

    @ManyToOne
    @JoinColumn(name = "departure", referencedColumnName = "id")
    private Location departure;

    @ManyToOne
    @JoinColumn(name = "destination", referencedColumnName = "id")
    private Location destination;

    @Column(name = "price", nullable = false)
    private Integer price;
    
    @Column(name = "schedule", length = 250)
    private String schedule;
    
    @Column(name = "parents")
    private Integer parents;

    @Column(name = "hours")
    private float hours;
    
    
    @ManyToOne
    @JoinColumn(name="bus_type_id",referencedColumnName = "id")
    private BusType busType;
    
    @OneToMany(mappedBy = "route")
    private List<Bus> buses;
    
    @OneToMany(mappedBy = "route")
    private List<Trip> trips;
    
    
}