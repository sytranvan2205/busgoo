package com.iuh.busgoo.requestType;

import lombok.Data;

@Data
public class ChangePassRequest {
	private String email;
	private String password;
	private String newPassword;
}
