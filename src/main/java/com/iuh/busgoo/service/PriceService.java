package com.iuh.busgoo.service;

import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.requestType.PriceCreateRequest;

public interface PriceService {
	DataResponse createPrice(PriceCreateRequest priceCreateRequest );
	
	DataResponse getAllPrice();
}
