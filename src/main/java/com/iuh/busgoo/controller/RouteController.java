package com.iuh.busgoo.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.iuh.busgoo.constant.Constant;
import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.filter.RouteFilter;
import com.iuh.busgoo.requestType.RouteCreateRequest;
import com.iuh.busgoo.service.RouteService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/route")
public class RouteController {

	@Autowired
	private RouteService routeService;

	@GetMapping("/find")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse findRoute(@RequestParam(required = false) Integer status,
			@RequestParam(required = false) Long fromId, @RequestParam(required = false) Long toId,
			@RequestParam Integer itemPerPage, @RequestParam Integer page,
			@RequestParam(required = false) String sortBy, @RequestParam(required = false) String orderBy) {
		try {
			RouteFilter filter = new RouteFilter();
			filter.setFromId(fromId);
			filter.setToId(toId);
			filter.setStatus(status);
			filter.setPage(page);
			filter.setItemPerPage(itemPerPage);
			filter.setOrderBy(orderBy);
			filter.setSortBy(sortBy);
			return routeService.findRouteByFilter(filter);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@GetMapping("/find-all")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse findAllRoute() {
		try {
			return routeService.findAll();
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@DeleteMapping("/delete/{id}")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse deleteRoute(@PathVariable Long id) {
		try {
			return routeService.deleteRoute(id);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@PostMapping("/get-bus-trip")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse getBusTrip(@RequestBody String fromCode, @RequestBody String toCode,
			@RequestBody LocalDate timeStated) {
		try {
			return routeService.getBusTripByAddressAndTime(fromCode, toCode, timeStated);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@GetMapping("/get")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse getRouteById(@RequestParam Long routeId) {
		try {
			return routeService.getRouteById(routeId);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}
	
	@PostMapping("/create")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse createRoute(@RequestBody RouteCreateRequest routeCreateRequest) {
		try {
			return routeService.createRoute(routeCreateRequest);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}
}
