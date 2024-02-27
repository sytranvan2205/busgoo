package com.iuh.busgoo.requestType;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class PriceCreateRequest {
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate toDate;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate fromDate;
	private String typeBusCode;
	private String routeCode;
	private String priceDescription;
	private Double priceValue;
}
