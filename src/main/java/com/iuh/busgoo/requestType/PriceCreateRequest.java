package com.iuh.busgoo.requestType;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class PriceCreateRequest {
	private LocalDate toDate;
	private LocalDate fromDate;
//	private String typeBusCode;
//	private String routeCode;
	private String priceDescription;
//	private Double priceValue;
}
