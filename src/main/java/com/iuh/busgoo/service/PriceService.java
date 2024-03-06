package com.iuh.busgoo.service;

import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.requestType.PriceCreateRequest;
import com.iuh.busgoo.requestType.PriceDetailRequest;

public interface PriceService {
	DataResponse createPrice(PriceCreateRequest priceCreateRequest );
	
	DataResponse getAllPrice();
	
	DataResponse getPriceById(Long id);
	
	DataResponse deletePrice(Long id);
	
	DataResponse createPriceDetail(PriceDetailRequest request);
	
	DataResponse getPriceDetailByPriceId(Long priceId);
}
