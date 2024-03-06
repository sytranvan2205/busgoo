package com.iuh.busgoo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iuh.busgoo.entity.User;
import com.iuh.busgoo.repository.UserRepository;
import com.iuh.busgoo.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	UserRepository userRepo;

	@Override
	public User findUserByCode(String code) {
		return userRepo.getUserByUserCode(code);
	}

	@Override
	public User findUserByEmail(String email) {
		return userRepo.getUserByEmail(email);
	}
	
}
