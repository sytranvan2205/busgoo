package com.iuh.busgoo.controler;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iuh.busgoo.constant.Constant;
import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.service.RouteService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/route")
public class RouteController {
	
	@Autowired
	private RouteService routeService;
	
	@GetMapping("/find")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse findRoute() {
		try {
			return routeService.getAllRoute();
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
	
	@PostMapping("/delete")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse deleteRoute(@RequestBody Long routeId) {
		try {
			return routeService.deleteRoute(routeId);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}
	
	@PostMapping("/get-bus-trip")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse getBusTrip(@RequestBody String fromCode,@RequestBody String toCode, @RequestBody LocalDate timeStated) {
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
}
