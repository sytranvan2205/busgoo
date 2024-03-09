package com.iuh.busgoo.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.iuh.busgoo.constant.Constant;
import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.entity.User;
import com.iuh.busgoo.repository.UserRepository;
import com.iuh.busgoo.requestType.UserCreateRequest;
import com.iuh.busgoo.service.UserService;
import com.iuh.busgoo.utils.PageUtils;

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

	@Override
	public DataResponse findUserActive(Pageable pageable) {
		DataResponse dataResponse = new DataResponse();
		try {
			List<User> users = userRepo.findByStatus(1);
			if(users != null && users.size() > 0) {
				Page<User> pageUser = PageUtils.createPageFromList(users, pageable);
				dataResponse.setResponseMsg("Get user success !!!");
				dataResponse.setRespType(Constant.HTTP_SUCCESS);
				Map<String, Object> reponseValue = new HashMap<String, Object>();
				reponseValue.put("users", pageUser);
				dataResponse.setValueReponse(reponseValue);
				return dataResponse;
			}else {
				dataResponse.setResponseMsg("Get user success !!!");
				dataResponse.setRespType(Constant.HTTP_SUCCESS);
				return dataResponse;
			}
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@Override
	public DataResponse createUser(UserCreateRequest userCreateRequest) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
