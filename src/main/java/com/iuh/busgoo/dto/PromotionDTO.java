package com.iuh.busgoo.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PromotionDTO {
	private String promotionCode;
	private String promotionLineName;
	private Integer promotionType;
	private BigDecimal discount;
}
