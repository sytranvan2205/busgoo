package com.iuh.busgoo.requestType;

import lombok.Data;

@Data
public class UserCreateRequest {
	private String fullName;
	private String phone;
	private Long regeionDetailId;
	private String addressDescription;
}
