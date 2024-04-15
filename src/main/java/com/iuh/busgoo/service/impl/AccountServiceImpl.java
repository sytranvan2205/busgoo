package com.iuh.busgoo.service.impl;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.iuh.busgoo.constant.Constant;
import com.iuh.busgoo.constant.StatusType;
import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.entity.Account;
import com.iuh.busgoo.entity.MailToken;
import com.iuh.busgoo.entity.Role;
import com.iuh.busgoo.entity.User;
import com.iuh.busgoo.mail.Mail;
import com.iuh.busgoo.mail.MailService;
import com.iuh.busgoo.repository.AccountRepository;
import com.iuh.busgoo.repository.RoleRepository;
import com.iuh.busgoo.repository.UserRepository;
import com.iuh.busgoo.requestType.AccountCreateRequest;
import com.iuh.busgoo.service.AccountService;
import com.iuh.busgoo.service.MailTokenService;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	private MailTokenService mailTokenService;
	
	@Autowired
	private MailService mailService;
	
	@Override
	@Transactional(rollbackOn  = {SQLException.class})
	public DataResponse createAccount(AccountCreateRequest accountCreateRequest) {
		DataResponse response = new DataResponse();
		try {
			if(accountCreateRequest.getEmail() == null) {
				response.setRespType(Constant.EMAIL_NOT_NULL);
				response.setResponseMsg("Email is not null");
				return response;
			}
			if(accountCreateRequest.getPassword() == null) {
				response.setRespType(Constant.PASSWORD_NOT_NULL);
				response.setResponseMsg("Password is not null");
				return response;
			}
			if(accountCreateRequest.getFullName()==null) {
				response.setRespType(Constant.USER_NOT_NULL);
				response.setResponseMsg("Username is not null");
				return response;
			}
			if(accountCreateRequest.getPhone() == null) {
				response.setRespType(Constant.PHONE_IS_NULL);
				response.setResponseMsg("Phone is not null");
				return response;
			}
			Account checkEmailExist = accountRepository.findAccountByEmail(accountCreateRequest.getEmail().trim());
			if(checkEmailExist!= null) {
				response.setRespType(Constant.EMAIL_IS_EXIST);
				response.setResponseMsg("Email has exist");
				return response;
			}
			User checkPhoneExist = userRepo.findByPhone(accountCreateRequest.getPhone());
			User userTmp;
			if(checkPhoneExist != null) {
				Account checkAccountExist = accountRepository.findAccountByUserUserId(checkPhoneExist.getUserId());
				if (checkAccountExist != null) {
					response.setRespType(Constant.PHONE_IS_EXIST);
					response.setResponseMsg("Phone has exist");
					return response;
				}else {
					userTmp = checkPhoneExist;
					userTmp.setFullName(accountCreateRequest.getFullName());
					userTmp = userRepo.save(userTmp);
				}
			}else {
				// create new user
				userTmp = new User();
				userTmp.setFullName(accountCreateRequest.getFullName());
				userTmp.setPhone(accountCreateRequest.getPhone());
				Long countUser = userRepo.count();
				userTmp.setUserCode("US" + (countUser++).toString());
				userTmp.setStatus(1);
				userTmp = userRepo.save(userTmp);
			}
			
			
			// authorization
			Role role = roleRepository.findByCodeAndStatus(Constant.USER,1);
			if(role == null) {
				throw new Exception("Role is not exist");
			}

			Account accountTmp = new Account();
			accountTmp.setEmail(accountCreateRequest.getEmail());
			accountTmp.setRole(role);
			accountTmp.setIsActive(0);
			accountTmp.setUser(userTmp);
			accountTmp.setPassword(passwordEncoder.encode(accountCreateRequest.getPassword()));
			Account account = accountRepository.save(accountTmp);
			
			response.setRespType(Constant.HTTP_SUCCESS);
			response.setResponseMsg("create account success !!!");
			
			//create value response
			Map<String, Object> responseMap = new HashMap<>();
			responseMap.put("data",account);
			response.setValueReponse(responseMap);

		} catch (Exception e) {
			response.setResponseMsg("System error");
			response.setRespType(Constant.SYSTEM_ERROR_CODE);
			return response;
		}
		return response;
	}

	@Override
	public DataResponse activeAccount(String email, String token) {
		DataResponse dataResponse = new DataResponse();
		try {
			MailToken mailToken = mailTokenService.findByToken(token);
			Account account = accountRepository.findAccountByEmail(email);
			if(mailToken == null) {
				dataResponse.setRespType(Constant.MAIL_TOKEN_NOT_EXIST);
				dataResponse.setResponseMsg("Mail token not exist");
				return dataResponse;
			}if(mailToken.getExpireAt().compareTo(LocalDateTime.now())<0) {
				dataResponse.setRespType(Constant.MAIL_TOKEN_EXPIRE);
				dataResponse.setResponseMsg("Mail token has expire");
				return dataResponse;
			}if(account == null) {
				dataResponse.setRespType(Constant.ACCOUNT_NOT_EXIST);
				dataResponse.setResponseMsg("Account is not exist");
				return dataResponse;
			}
			Account accountCheck = mailToken.getAccount();
			if(account.equals(accountCheck)) {
				account.setIsActive(1);
				accountRepository.save(account);
				dataResponse.setResponseMsg("Verify successful !!!");
				dataResponse.setRespType(Constant.HTTP_SUCCESS);
				return dataResponse;
			}else {
				dataResponse.setResponseMsg("Verify faill !!!");
				dataResponse.setRespType(Constant.ACCOUNT_VERIFY_FAILD);
				System.out.println("verify success");
				return dataResponse;
			}
		} catch (Exception e) {
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			dataResponse.setResponseMsg("System error");
			return dataResponse;
		}
	}

	@Override
	public DataResponse createTokenAccount(String email) {
		DataResponse response = new DataResponse();
		try {
			Account account = accountRepository.findAccountByEmail(email);
			if(account == null) {
				response.setResponseMsg("Account is not exist");
				response.setRespType(Constant.ACCOUNT_NOT_EXIST);
				return response;
			}
			if(account.getIsActive().equals(StatusType.HOAT_DONG.getValue())) {
				response.setResponseMsg("Account has been verified");
				response.setRespType(Constant.ACCOUNT_HAS_BEEN_VERIFIED);
				return response;
			}else {
				// create token
				MailToken token = mailTokenService.createMailToken(account);
				Map<String, Object> maps = new HashMap<>();
				maps.put("user", account);
				maps.put("token", token.getToken());
				
				Mail mail = new Mail();
				mail.setFrom("imusicstudio4nvb@gmail.com");
				mail.setSubject("Registration");
				mail.setTo(account.getEmail());
				mail.setModel(maps);
				try {
					mailService.sendEmail(mail);
				} catch (MessagingException e) {
					e.printStackTrace();
					throw new Exception();
				}
				response.setResponseMsg("Create successfull !!!");
				response.setRespType(Constant.HTTP_SUCCESS);
				Map<String,Object> tokenTest = new HashMap<>();
				tokenTest.put("token", token.getToken());
				response.setValueReponse(tokenTest);
				return response;
			}
		} catch (Exception e) {
			response.setResponseMsg("System error");
			response.setRespType(Constant.SYSTEM_ERROR_CODE);
			return response;
		}
	}


	
	

}
