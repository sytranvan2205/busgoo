package com.iuh.busgoo.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ReportDTO {
	private String busType;
	private String busName;
	private Long revenue;
	private Long ticketPrice;
	private Long discount;
	private String busCode;
	private String typeBusCode;
	private String promotionCode;
	private String promotionName;
	private String promotionType;
	private LocalDate promotionFDate;
	private LocalDate promotionTDate;
//	private Long discount;
}
