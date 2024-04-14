package com.iuh.busgoo.service;

import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.entity.Order;
import com.iuh.busgoo.filter.OrderFilter;
import com.iuh.busgoo.requestType.OrderCreateRequest;

public interface OrderService {
	DataResponse createOrder(OrderCreateRequest orderCreateRequest);
	
	DataResponse getOrderDetail(Long orderId);
	
	DataResponse deleteOrder(Long orderId);
	
	DataResponse getAllOrder();

	Order findOrderById(Long orderId);

	DataResponse getOrderByFilter(OrderFilter orderFilter);

	DataResponse getOrderById(Long orderId);

	DataResponse getOrderByCurrentUser(String userCode);

	DataResponse getListOrderByUserId(Long userId);
	
}
