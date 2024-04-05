package com.iuh.busgoo.dto;

import java.time.LocalDate;
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
	private LocalDate orderCreateDate;
	private User user;
	
	private UserDTO userDTO;

	private Double totalTiketPrice;

	private Double totalDiscount;

	private Double total;
	
	PromotionDTO promotionDTO = new PromotionDTO();
	
	List<OrderDetailDTO> orderDetails = new ArrayList<OrderDetailDTO>();
}
