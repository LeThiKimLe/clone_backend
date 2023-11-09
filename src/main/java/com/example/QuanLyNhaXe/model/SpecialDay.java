package com.example.QuanLyNhaXe.model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "special_day")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpecialDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "fee", nullable = false)
    private Integer fee;
    
    @OneToMany(mappedBy = "specialDay")
    private List<Schedule> schedules;
}