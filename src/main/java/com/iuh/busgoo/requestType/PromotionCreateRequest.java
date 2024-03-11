package com.iuh.busgoo.requestType;

import java.time.LocalDate;

import lombok.Data;

@Data
public class PromotionCreateRequest {
	private String code;
	private String name;
	private LocalDate fromDate;
	private LocalDate toDate;
	private String description;
}
