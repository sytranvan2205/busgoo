package com.iuh.busgoo.requestType;

import lombok.Data;

@Data
public class LoginRequest {
	private String email;
	private String password;
}
