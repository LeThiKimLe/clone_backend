package com.example.QuanLyNhaXe.model;

import java.util.List;

import com.example.QuanLyNhaXe.enumration.BusAvailability;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bus")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "license_plate", nullable = false)
    private String licensePlate;

    @Column(name = "manufacture_year", nullable = false)
    private int manufactureYear;

    @Column(name = "color")
    private String color;

    
    @Column(name = "availability")
    private String availability;

    @ManyToOne
    @JoinColumn(name = "current_position", referencedColumnName = "id")
    private Location currentPosition;

    @OneToOne
    @JoinColumn(name = "state_id", referencedColumnName = "id")
    private BusQuality state;

    @ManyToOne
    @JoinColumn(name = "route_id", referencedColumnName = "id")
    private Route route;

    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private BusType type;

    @OneToOne
    @JoinColumn(name = "driver_id", referencedColumnName = "driver_id")
    private Driver driver;
    
    @OneToMany(mappedBy = "bus")
    private List<Schedule> schedules;
   
}
