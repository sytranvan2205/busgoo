package com.iuh.busgoo.service;

import com.iuh.busgoo.entity.User;

public interface UserService {
	User findUserByCode(String code);
	
	User findUserByEmail(String email);
}
