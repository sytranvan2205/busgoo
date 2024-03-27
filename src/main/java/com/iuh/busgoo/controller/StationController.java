package com.iuh.busgoo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iuh.busgoo.constant.Constant;
import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.requestType.OrderCreateRequest;
import com.iuh.busgoo.requestType.StationCreateRequest;
import com.iuh.busgoo.service.StationService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController()
@RequestMapping("/api/station")
public class StationController {
	
	@Autowired
	private StationService stationService;
	
	@PostMapping("/create")
	@SecurityRequirement(name="bearerAuth")
	public DataResponse createStation(@RequestBody StationCreateRequest stationCreateRequest) {
		try {
			return stationService.createStation(stationCreateRequest);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}
	
//	@GetMapping("/find")
//	@SecurityRequirement(name = "bearerAuth")
//	public DataResponse getListStation(@RequestParam(required = false) String q, @RequestParam(required = false) Integer status,
//			@RequestParam Integer itemPerPage, @RequestParam Integer page, @RequestParam(required = false) String sortBy,
//			@RequestParam(required = false) String orderBy) {
//		try {
//			return stationService.findUserByFilter(filterUserRq);
//		} catch (Exception e) {
//			stationService.setResponseMsg("System error");
//			stationService.setRespType(Constant.SYSTEM_ERROR_CODE);
//			return stationService;
//		}
//	}
	@GetMapping("/get")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse getStationByRegion(@RequestParam Long regionDetailId) {
		DataResponse dataResponse = new DataResponse();
		try {
			return stationService.getStation(regionDetailId);
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}
}
