package com.iuh.busgoo.service;

import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.requestType.StationCreateRequest;

public interface StationService {

	DataResponse createStation(StationCreateRequest stationCreateRequest);

	DataResponse createStation(Long regionDetailId);

	DataResponse getStation(Long regionDetailId);

}
