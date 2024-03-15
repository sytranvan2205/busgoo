package com.iuh.busgoo.requestType;

import java.time.LocalDate;

import lombok.Data;

@Data
public class PromotionLineRq {
	private Long promotionId;
	private Long promotionLineId;
	private String lineCode;
	private String lineName;
	private LocalDate fromDate;
	private LocalDate toDate;
	private Integer promotionType;
}
