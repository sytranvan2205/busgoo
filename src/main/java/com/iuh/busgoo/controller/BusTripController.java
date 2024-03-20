package com.iuh.busgoo.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iuh.busgoo.constant.Constant;
import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.filter.BustripFilter;
import com.iuh.busgoo.service.TimeTableService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/bustrip")
public class BusTripController {
	@Autowired
	TimeTableService timeTableService;

	@GetMapping("/get")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse findTimeTable(@RequestParam Long fromId,
			@RequestParam Long toId, @RequestParam LocalDateTime timeStarted) {
		try {
			BustripFilter filter = new BustripFilter();
			filter.setFromId(fromId);
			filter.setToId(toId);
			filter.setTimeStarted(timeStarted);
			return timeTableService.getBustripByFilter(filter);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}

	}

}
