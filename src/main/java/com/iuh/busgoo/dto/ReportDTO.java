package com.iuh.busgoo.dto;

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
}
