package com.iuh.busgoo.requestType;

import lombok.Data;

@Data
public class PriceDetailRequest {
	private Long typeBusId;
	private Long routeId;
	private Double priceValue;
	private Long priceId;
}
