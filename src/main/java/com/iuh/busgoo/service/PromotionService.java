package com.iuh.busgoo.service;

import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.filter.PromotionFilter;
import com.iuh.busgoo.requestType.PromotionCreateRequest;
import com.iuh.busgoo.requestType.PromotionLineRq;

public interface PromotionService {

	DataResponse getAllPromotionByFilter(PromotionFilter filter);

	DataResponse createPromotion(PromotionCreateRequest promotionCreateRequest);

	DataResponse getPromotionLine(Long promotionId);

	DataResponse deletePromotion(Long promotionId);

	DataResponse createPromotionLine(PromotionLineRq promotionLineRq);

}
