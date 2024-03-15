package com.iuh.busgoo.requestType;

import java.time.LocalDate;

import lombok.Data;

@Data
public class PromotionUpdateRequest {
	private Long promotionId;
//	private String code;
	private String name;
	private Integer status;
	private LocalDate fromDate;
	private LocalDate toDate;
	private String description;
}
