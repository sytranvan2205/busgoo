package com.iuh.busgoo.requestType;

import java.util.List;

import lombok.Data;

@Data
public class OrderCreateRequest {
	private Long userId;
	private List<Long> lstSeatOrderId;
	private Long pickupPointId;
//	private Long dropoffPointId;
}
