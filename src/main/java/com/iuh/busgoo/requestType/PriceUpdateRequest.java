package com.iuh.busgoo.requestType;

import java.time.LocalDate;

import lombok.Data;

@Data
public class PriceUpdateRequest {
	private Long priceId;
	private LocalDate toDate;
	private LocalDate fromDate;
	private String priceDescription;
}
