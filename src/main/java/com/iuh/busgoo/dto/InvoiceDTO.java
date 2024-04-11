package com.iuh.busgoo.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import lombok.Data;

@Data
public class InvoiceDTO {
	
	private Long id;

	private String code;

	private Long orderId;

	private Long userId;
	
	private String userName;
	
	private String userCode;
	
	private String strLstSeatName;
	
	private String busTrip;

	private LocalDate timeBooking;
	
	private LocalDateTime timeStarted;

	private Double total;

	private Double totalDiscount;

	private Double totalTiketPrice;

	private Integer status;
	
	private String userAddress;
	private String userPhone;
	
	private List<OrderDetailDTO> list;
	
	private LocalDate dateStarted;
	
	private LocalTime startedTime;
}
