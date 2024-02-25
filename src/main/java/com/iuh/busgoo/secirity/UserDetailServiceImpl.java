package com.iuh.busgoo.secirity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.iuh.busgoo.entity.Account;
import com.iuh.busgoo.repository.AccountRepository;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	private AccountRepository accountRepo;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Account account = accountRepo.findAccountByEmailAndIsActive(email,1);
		if (account == null) {
			throw new UsernameNotFoundException("Account is not exist");
		}
		if(account.getUser() == null) {
			throw new UsernameNotFoundException("User is not exist");
		}
		if(account.getRole() == null) {
			throw new UsernameNotFoundException("Role is not exist");
		}
		return new CustomUserDetail(account);
	}

}
