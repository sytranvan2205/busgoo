package com.iuh.busgoo.dto;

import lombok.Data;

@Data
public class UserDTO {
	private Long id;
	private String userCode;
	private String fullName;
	private String phone;
	private Integer status;
	private String address;
}
