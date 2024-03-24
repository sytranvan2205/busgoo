package com.iuh.busgoo.requestType;

import lombok.Data;

@Data
public class StationCreateRequest {
	private String stationName;
	private Long regionDetailId;
	private String addressDescription;
	private String description;
}
