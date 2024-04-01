package com.iuh.busgoo.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromotionDTO {
	private String promotionCode;
	private String promotionLineName;
	private Integer promotionType;
	private BigDecimal discount;
	private BigDecimal conditionApply;
	private BigDecimal maxDiscount;
}
