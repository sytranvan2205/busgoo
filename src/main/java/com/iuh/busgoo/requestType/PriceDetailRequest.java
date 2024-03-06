package com.iuh.busgoo.requestType;

import lombok.Data;

@Data
public class PriceDetailRequest {
	private String typeBusCode;
	private String routeCode;
	private Double priceValue;
	private Long priceId;
}
