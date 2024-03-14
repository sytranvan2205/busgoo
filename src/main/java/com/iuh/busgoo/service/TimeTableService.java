package com.iuh.busgoo.service;

import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.requestType.TimeTableCreateRequest;

public interface TimeTableService {

	DataResponse createTimeTable(TimeTableCreateRequest timeTableCreateRequest);

}
