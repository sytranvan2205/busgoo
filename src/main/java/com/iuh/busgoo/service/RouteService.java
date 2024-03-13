package com.iuh.busgoo.service;

import java.time.LocalDate;

import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.requestType.RouteCreateRequest;

public interface RouteService {
	
	DataResponse getAllRoute();

	DataResponse getRouteByAddress(String fromCode, String toCode);

	DataResponse createRote(RouteCreateRequest routeCreateRequest);

	DataResponse deleteRoute(Long routeId);
	
	DataResponse getBusTripByAddressAndTime(String fromCode, String toCode,LocalDate timeStated );

	DataResponse findAll();

	DataResponse getRouteById(Long routeId);
}
