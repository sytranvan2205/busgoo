package com.iuh.busgoo.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class OrderManagerDTO {
	private Long id;
	private String code;
	private Integer isPay;
	private Integer status;
	private String userCode;
	private String userName;
	private Double total;
	private LocalDate createDate;
}
