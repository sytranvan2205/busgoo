package com.iuh.busgoo.service;

import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.requestType.OrderCreateRequest;

public interface OrderService {
	DataResponse createOrder(OrderCreateRequest orderCreateRequest);
	
	DataResponse getOrderDetail(Long orderId);
	
	DataResponse deleteOrder(Long orderId);
	
	DataResponse getAllOrder();
	
}
