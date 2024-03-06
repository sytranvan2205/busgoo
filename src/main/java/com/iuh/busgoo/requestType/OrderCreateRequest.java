package com.iuh.busgoo.requestType;

import lombok.Data;

@Data
public class OrderCreateRequest {
	private Long userId;
	private Long seatId;
	
}
