package com.iuh.busgoo.dto;

import java.util.ArrayList;
import java.util.List;

import com.iuh.busgoo.entity.User;

import lombok.Data;

@Data
public class OrderDTO {
	private Long orderId;
	private String code;
	private Integer isPay;
	private Integer status;
	
	private User user;

	private Double totalTiketPrice;

	private Double totalDiscount;

	private Double total;
	
	List<PromotionDTO> promotionDTOs = new ArrayList<PromotionDTO>();
	
	List<OrderDetailDTO> orderDetails = new ArrayList<OrderDetailDTO>();
}
