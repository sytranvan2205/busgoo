package com.iuh.busgoo.service.impl;

import org.springframework.stereotype.Service;

import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.requestType.TimeTableCreateRequest;
import com.iuh.busgoo.service.TimeTableService;

@Service
public class TimeTableServiceImpl implements TimeTableService{

	@Override
	public DataResponse createTimeTable(TimeTableCreateRequest timeTableCreateRequest) {
		DataResponse dataResponse = new DataResponse();
		return null;
	}
	
	
}
