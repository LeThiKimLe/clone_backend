package com.example.QuanLyNhaXe.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import com.example.QuanLyNhaXe.dto.RouteDTO;

import com.example.QuanLyNhaXe.exception.NotFoundException;

import com.example.QuanLyNhaXe.model.Route;

import com.example.QuanLyNhaXe.repository.RouteRepository;
import com.example.QuanLyNhaXe.util.Message;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RouteService {
	private final RouteRepository routeRepository;
	private final ModelMapper modelMapper;
	
	
	public List<RouteDTO> getAllRoutes() {
        List<Route> routes = routeRepository.findAll();
        if (routes.isEmpty()){
            throw new NotFoundException(Message.ROUTE_NOT_FOUND);
        }
        return routes.stream()
        		.map(route -> modelMapper.map(route, RouteDTO.class))
        		.toList();
    }
	
	public RouteDTO getRouteById(Integer routeId) {
		Route route =routeRepository.findById(routeId)
		    	.orElseThrow(() -> new NotFoundException(Message.ROUTE_NOT_FOUND));
		    	return modelMapper.map(route, RouteDTO.class);
	}
	
}
