package com.iuh.busgoo.requestType;

import java.time.LocalTime;

import lombok.Data;

@Data
public class RouteCreateRequest {
	private Long fromId;
	private Long toId;
	private String transferTime;


}
