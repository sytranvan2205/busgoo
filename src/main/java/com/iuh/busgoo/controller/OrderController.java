package com.iuh.busgoo.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iuh.busgoo.constant.Constant;
import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.filter.OrderFilter;
import com.iuh.busgoo.filter.PriceFilter;
import com.iuh.busgoo.requestType.OrderCreateRequest;
import com.iuh.busgoo.service.OrderService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController()
@RequestMapping("/api/order")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@PostMapping("/create")
	@SecurityRequirement(name="bearerAuth")
	public DataResponse createOrder(@RequestBody OrderCreateRequest orderCreateRequest) {
		try {
			return orderService.createOrder(orderCreateRequest);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}
	
	@GetMapping("/find")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse getOrder(@RequestParam(required = false) Integer statusPaying,
			@RequestParam(required = false) LocalDate fromDate, @RequestParam(required = false) LocalDate toDate,
			@RequestParam Integer itemPerPage, @RequestParam Integer page, @RequestParam(required = false) String sortBy,
			@RequestParam(required = false) String orderBy) {
		try {
			OrderFilter orderFilter = new OrderFilter();
			orderFilter.setFromDate(fromDate);
			orderFilter.setItemPerPage(itemPerPage);
			orderFilter.setOrderBy(orderBy);
			orderFilter.setPage(page);
			orderFilter.setSortBy(sortBy);
			orderFilter.setStatusPaying(statusPaying);
			orderFilter.setToDate(toDate);
			return orderService.getOrderByFilter(orderFilter);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}
	
	@GetMapping("/get")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse findById(@RequestParam Long orderId) {
		try {
			return orderService.getOrderById(orderId);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}
	
		
}
