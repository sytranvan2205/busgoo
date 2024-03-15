package com.iuh.busgoo.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iuh.busgoo.constant.Constant;
import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.filter.TimeTableFilter;
import com.iuh.busgoo.requestType.TimeTableCreateRequest;
import com.iuh.busgoo.service.TimeTableService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/timetable")
public class TimeTableController {
	@Autowired
	TimeTableService timeTableService;

	@PostMapping("/create")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse createTimeTable(@RequestBody TimeTableCreateRequest timeTableCreateRequest) {
		try {
			return timeTableService.createTimeTable(timeTableCreateRequest);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error !!!");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@GetMapping("/find")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse findTimeTable(@RequestParam(required = false) Integer status,
			@RequestParam(required = false) LocalDateTime departureDate, @RequestParam(required = false) Long fromId,
			@RequestParam(required = false) Long toId, @RequestParam Integer itemPerPage, @RequestParam Integer page,
			@RequestParam(required = false) String sortBy, @RequestParam(required = false) String orderBy) {
		try {
			TimeTableFilter filter = new TimeTableFilter();
			filter.setFromId(fromId);
			filter.setStatus(status);
			filter.setToId(toId);
			filter.setDepartureDate(departureDate);
			filter.setPage(page);
			filter.setItemPerPage(itemPerPage);
			filter.setOrderBy(orderBy);
			filter.setSortBy(sortBy);
			return timeTableService.findTimeTableByFilter(filter);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}

	}

}
