package com.iuh.busgoo.service;

import com.iuh.busgoo.entity.User;

public interface UserService {
	User findUserByCode(String code);
	
	User fundUserByEmail(String email);
}
