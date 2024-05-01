package com.iuh.busgoo.controller;

import java.util.HashMap;
import java.util.Map;

import com.iuh.busgoo.entity.User;
import com.iuh.busgoo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iuh.busgoo.constant.Constant;
import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.jwt.JwtTokenProvider;
import com.iuh.busgoo.requestType.AccountCreateRequest;
import com.iuh.busgoo.requestType.LoginRequest;
import com.iuh.busgoo.secirity.CustomUserDetail;
import com.iuh.busgoo.service.AccountService;

@RestController
@RequestMapping("/auth")
public class AuthenController {
	@Autowired
    AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	private AccountService accountService;


	@Autowired
	private UserService userService;
	
	@PostMapping("/login")
	public DataResponse authenticateUser(@RequestBody LoginRequest loginRequest) {
		DataResponse dataResponse = null;
		try {
			dataResponse = new DataResponse();
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
					);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			String jwt = tokenProvider.generateToken((CustomUserDetail) authentication.getPrincipal());
			User user = userService.findUserByEmail(loginRequest.getEmail());
			
			dataResponse.setResponseMsg("Login successful!!!");
			dataResponse.setRespType(Constant.HTTP_SUCCESS);
			Map<String, Object> reponseValue = new HashMap<>();
			reponseValue.put("token", jwt);
			reponseValue.put("id", user.getUserId());
			reponseValue.put("name", user.getFullName());
			reponseValue.put("email", loginRequest.getEmail());
			dataResponse.setValueReponse(reponseValue);
			
		} catch (Exception e) {
			e.printStackTrace();
			dataResponse.setResponseMsg("Login fail !!!");
			dataResponse.setRespType(Constant.USER_NOT_EXIST);
			return dataResponse;
		}
		return dataResponse;
	}
	
	@PostMapping("/register")
	public DataResponse createAccount(@RequestBody AccountCreateRequest accountCreateRequest) {
		DataResponse dataResponse = null;
		try {
			if(accountCreateRequest.getPassword().equals(accountCreateRequest.getRePassword())) {
				dataResponse = accountService.createAccount(accountCreateRequest);
			}else {
				throw new Exception("Invalid password");
			}
		} catch (Exception e) {
			dataResponse = new DataResponse();
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			dataResponse.setResponseMsg("System is error");
			return dataResponse;
		}
		return dataResponse;
	}
	
	@GetMapping("/createToken")
	public DataResponse createToken(@RequestParam String email) {
		DataResponse dataResponse = new DataResponse();
		try {
			if(email == null) {
				dataResponse.setRespType(Constant.EMAIL_IS_EXIST);
				dataResponse.setResponseMsg("Email is not exist");
				return dataResponse;
			}else {
				dataResponse = accountService.createTokenAccount(email);
				return dataResponse;
			}
		} catch (Exception e) {
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			dataResponse.setResponseMsg("System is error");
			return dataResponse;
		}
	}
	
	@GetMapping("/verify")
	public DataResponse verifyAccount(@RequestParam String email, @RequestParam String token) {
		DataResponse dataResponse = new DataResponse();
		try {
			dataResponse = accountService.activeAccount(email, token);
			return dataResponse;
		} catch (Exception e) {
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			dataResponse.setResponseMsg("System is error");
			return dataResponse;
		}
	}
	
	@PostMapping("/api/logout")
    public DataResponse logout() {
		DataResponse dataResponse = new DataResponse();
		try {
	        SecurityContextHolder.clearContext();
	        dataResponse.setResponseMsg("Logout successful !!!");
	        dataResponse.setRespType(Constant.HTTP_SUCCESS);
		} catch (Exception e) {
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			dataResponse.setResponseMsg("System is error");
		}
		return dataResponse;
    }
	
}
