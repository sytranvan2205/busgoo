package com.iuh.busgoo.requestType;

import lombok.Data;

@Data
public class PromotionDetailRequest {
	private Long promotionLineId;
	private String detailCode;
	private Double discount;
	private Integer maxDiscount;
	private Integer conditionApply;
	private Long promotionDetailId;
}
