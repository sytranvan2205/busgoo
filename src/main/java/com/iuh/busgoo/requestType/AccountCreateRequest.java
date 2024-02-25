package com.iuh.busgoo.requestType;

import java.io.Serializable;

import lombok.Data;

@Data
public class AccountCreateRequest implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String email;
	private String password;
	private String rePassword;
	private String fullName;
	private String phone;
}
