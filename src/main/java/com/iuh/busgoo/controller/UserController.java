package com.iuh.busgoo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iuh.busgoo.constant.Constant;
import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.requestType.ChangePassRequest;
import com.iuh.busgoo.requestType.UserCreateRequest;
import com.iuh.busgoo.requestType.UserUpdateRequest;
import com.iuh.busgoo.service.UserService;
import com.iuh.busgoo.service.impl.FilterUserRq;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController()
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/create")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse createUser(@RequestBody UserCreateRequest userCreateRequest) {
		DataResponse dataResponse = new DataResponse();
		try {
			if (userCreateRequest == null) {
				throw new Exception();
			}
			return dataResponse = userService.createUser(userCreateRequest);
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@GetMapping("/get-by-phone")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse getUserByPhone(@RequestParam String phone) {
		DataResponse dataResponse = new DataResponse();
		try {
			return userService.findUserByPhone(phone);
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@GetMapping("/find")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse getListUser(@RequestParam(required = false) String q, @RequestParam(required = false) Integer status,
			@RequestParam Integer itemPerPage, @RequestParam Integer page, @RequestParam(required = false) String sortBy,
			@RequestParam(required = false) String orderBy) {
		DataResponse dataResponse = new DataResponse();
		FilterUserRq filterUserRq = new FilterUserRq();
		filterUserRq.setStatus(status);
		filterUserRq.setItemPerPage(itemPerPage);
		filterUserRq.setPage(page - 1);
		filterUserRq.setQ(q);
		filterUserRq.setSortBy(sortBy);
		filterUserRq.setOrderBy(orderBy);
		try {
			return userService.findUserByFilter(filterUserRq);
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@GetMapping("/get")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse findUserById(@RequestParam Long userId) {
		try {
			return userService.getUserById(userId);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}
	@DeleteMapping("/delete/{userId}")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse deleteUserById(@PathVariable Long userId) {
		try {
			return userService.deleteUser(userId);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}
	
	@PostMapping("/change-pass")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse changePassword(@RequestBody ChangePassRequest changePassRequest) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth.getPrincipal() == null) {
				DataResponse dataResponse = new DataResponse();
				dataResponse.setResponseMsg("System error");
				dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
				return dataResponse;
			}
			return userService.changePass(changePassRequest);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}
	
	@PostMapping("/change-info")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse changeInfo(@RequestBody UserUpdateRequest userUpdateRequest) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth.getPrincipal() == null || userUpdateRequest.getUserId() == null) {
				DataResponse dataResponse = new DataResponse();
				dataResponse.setResponseMsg("System error");
				dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
				return dataResponse;
			}
			return userService.changeInfo(userUpdateRequest);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}
}
