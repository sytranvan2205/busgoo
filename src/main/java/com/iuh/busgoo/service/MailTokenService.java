package com.iuh.busgoo.service;

import com.iuh.busgoo.entity.Account;
import com.iuh.busgoo.entity.MailToken;

public interface MailTokenService {
	MailToken createMailToken(Account account);

	void saveMailToken(MailToken token);

	MailToken findByToken(String token);

	void removeToken(String token);
}
