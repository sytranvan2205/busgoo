package com.iuh.busgoo.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iuh.busgoo.constant.Constant;
import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.entity.Order;
import com.iuh.busgoo.repository.OrderRepository;
import com.iuh.busgoo.requestType.OrderCreateRequest;
import com.iuh.busgoo.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService{
	@Autowired
	private OrderRepository orderRepository;

	@Override
	public DataResponse createOrder(OrderCreateRequest orderCreateRequest) {
		return null;
	}

	@Override
	public DataResponse getOrderDetail(Long orderId) {
		return null;
	}

	@Override
	public DataResponse deleteOrder(Long orderId) {
		DataResponse dataResponse = new DataResponse();
		try {
			Optional<Order> tmp = orderRepository.findById(orderId);
			if(tmp != null) {
				Order order = tmp.get();
				order.setStatus(0);
				orderRepository.save(order);
				dataResponse.setResponseMsg("Delete order success");
				dataResponse.setRespType(Constant.HTTP_SUCCESS);
				return dataResponse;
			}else {
				throw new Exception();
			}
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@Override
	public DataResponse getAllOrder() {
		DataResponse dataResponse = new DataResponse();
		try {
			List<Order> orders = orderRepository.findByStatus(1);
			dataResponse.setResponseMsg("Get orders success!!!");
			dataResponse.setRespType(Constant.HTTP_SUCCESS);
			Map<String, Object> respValue = new HashMap<>();
			respValue.put("orders", orders);
			dataResponse.setValueReponse(respValue);
			return dataResponse;
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

}
