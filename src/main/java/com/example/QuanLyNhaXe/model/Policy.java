package com.example.QuanLyNhaXe.model;

import java.util.List;

import com.example.QuanLyNhaXe.enumration.Action;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "policy")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Policy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "action", nullable = false)   
    private String action;

    @Column(name = "time_required", nullable = false)
    private Integer timeRequired;

    @Column(name = "number_required", nullable = false)
    private Integer numberRequired;

    @Column(name = "refund_rate", nullable = false)
    private Integer refundRate;
    
    @OneToMany(mappedBy = "policy")
    private List<History> histories;
}
