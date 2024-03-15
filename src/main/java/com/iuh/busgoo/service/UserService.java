package com.iuh.busgoo.service;

import org.springframework.data.domain.Pageable;

import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.entity.User;
import com.iuh.busgoo.requestType.UserCreateRequest;
import com.iuh.busgoo.service.impl.FilterUserRq;

public interface UserService {
	User findUserByCode(String code);
	
	User findUserByEmail(String email);
	
	DataResponse findUserActive(Pageable pageable);
	
	DataResponse createUser(UserCreateRequest userCreateRequest);
	
	DataResponse findUserByPhone(String phone);
	
	DataResponse findUserByFilter(FilterUserRq filterUserRq);

	DataResponse getUserById(Long userId);

	DataResponse deleteUser(Long id);
}
