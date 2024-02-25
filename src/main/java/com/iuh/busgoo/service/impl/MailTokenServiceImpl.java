package com.iuh.busgoo.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.iuh.busgoo.entity.Account;
import com.iuh.busgoo.entity.MailToken;
import com.iuh.busgoo.repository.MailTokenRepository;
import com.iuh.busgoo.service.MailTokenService;
import com.iuh.busgoo.utils.RandomUtil;

@Service
public class MailTokenServiceImpl implements MailTokenService{

	@Value("${jdj.secure.token.validity}")
	private int tokenValidityInSeconds;
	
	@Autowired
	private MailTokenRepository mailTokenRepository;
	
	@Override
	public MailToken createMailToken(Account account) {
		String tokenValue = RandomUtil.generateRandomStringNumber(6).toUpperCase();
		MailToken secureToken = new MailToken();
		secureToken.setToken(tokenValue);
		secureToken.setExpireAt(LocalDateTime.now().plusSeconds(getTokenValidityInSeconds()));
		secureToken.setAccount(account);
		this.saveMailToken(secureToken);
		return secureToken;
	}

	@Override
	public void saveMailToken(MailToken token) {
		mailTokenRepository.save(token);
		
	}

	@Override
	public MailToken findByToken(String token) {
		return mailTokenRepository.findByToken(token);
	}

	@Override
	public void removeToken(String token) {
		MailToken mailToken = mailTokenRepository.findByToken(token);
		
	}
	
	public int getTokenValidityInSeconds() {
		return tokenValidityInSeconds;
	}
}
