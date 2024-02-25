package com.iuh.busgoo.service;

import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.requestType.AccountCreateRequest;

public interface AccountService {
	DataResponse createAccount(AccountCreateRequest accountCreateRequest);
	
	DataResponse activeAccount(String email, String token);
	
	DataResponse createTokenAccount(String email);
}
