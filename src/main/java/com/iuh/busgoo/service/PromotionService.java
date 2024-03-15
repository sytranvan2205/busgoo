package com.iuh.busgoo.service;

import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.filter.PromotionFilter;
import com.iuh.busgoo.requestType.PromotionCreateRequest;
import com.iuh.busgoo.requestType.PromotionDetailRequest;
import com.iuh.busgoo.requestType.PromotionLineRq;
import com.iuh.busgoo.requestType.PromotionUpdateRequest;

public interface PromotionService {

	DataResponse getAllPromotionByFilter(PromotionFilter filter);

	DataResponse createPromotion(PromotionCreateRequest promotionCreateRequest);

	DataResponse getPromotionLine(Long promotionId);

	DataResponse deletePromotion(Long promotionId);

	DataResponse createPromotionLine(PromotionLineRq promotionLineRq);

	DataResponse deletePromotionLine(Long promotionLineId);

	DataResponse findAllDetail();

	DataResponse createPromotionDetail(PromotionDetailRequest promotionDetailRequest);

	DataResponse deletePromotionDetail(Long promotionDetailId);

	DataResponse updatePromotion(PromotionUpdateRequest promotionUpdateRequest);

	DataResponse findDetailByLineId(Long promotionLineId);

	DataResponse findById(Long promotionId);

}
