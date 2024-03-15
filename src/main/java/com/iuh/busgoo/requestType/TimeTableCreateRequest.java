package com.iuh.busgoo.requestType;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TimeTableCreateRequest {
	private Long routeId;
	private Long busId;
	private LocalDateTime startTime;
}
