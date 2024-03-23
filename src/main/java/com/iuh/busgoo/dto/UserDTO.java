package com.iuh.busgoo.dto;

import lombok.Data;

@Data
public class UserDTO {
	private Long userId;
	private String userCode;
	private String fullName;
	private String phone;
	private Integer status;
	private String address;
	private Long addressId;
	private String addressDescription;
}
