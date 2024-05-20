package com.iuh.busgoo.requestType;

import lombok.Data;

@Data
public class UserUpdateRequest {
	private Long userId;
	private String userCode;
	private String userName;
	private Long addressId;
	private String addressDesciption;
}
