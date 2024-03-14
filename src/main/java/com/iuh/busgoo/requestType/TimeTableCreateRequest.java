package com.iuh.busgoo.requestType;

import java.time.LocalTime;

import lombok.Data;

@Data
public class TimeTableCreateRequest {
	private Long routeId;
	private Long busId;
	private LocalTime startTime;
}
