package com.iuh.busgoo.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.entity.Account;
import com.iuh.busgoo.entity.Role;
import com.iuh.busgoo.entity.User;
import com.iuh.busgoo.repository.AccountRepository;
import com.iuh.busgoo.repository.RoleRepository;
import com.iuh.busgoo.repository.UserRepository;
import com.iuh.busgoo.service.AccountService;

@SpringBootApplication
public class AppTesting implements CommandLineRunner{

	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	AccountRepository accountRepo;
	
	@Autowired
	RoleRepository roleRepo;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	AccountService accountService;
	
	@Override
	public void run(String... args) throws Exception {
		//create role test
		Role role = new Role();
		role.setCode("ADMIN");
		role.setName("ADMIN");
		role.setStatus(1);
		roleRepo.save(role);
		
		Role roleUser = new Role();
		roleUser.setCode("USER");
		roleUser.setName("USER");
		roleUser.setStatus(1);
		roleRepo.save(roleUser);
		
		
		//create user test
		User user = new User();
		user.setUserCode("US01");
		user.setFullName("Sỹ Trần Văn");
		user.setPhone("0822159420");
		user.setStatus(1);
		userRepo.save(user);
		
		//create account test
		Account account = new Account();
		account.setEmail("sytranvan2205@gmail.com");
		account.setPassword(passwordEncoder.encode("123456"));
		account.setIsActive(0);
		account.setRole(role);
		account.setUser(user);
		
		accountRepo.save(account);
		System.out.println(account);
		
//		DataResponse dataResponse =  accountService.createTokenAccount("sytranvan2205@gmail.com");
//		accountService.activeAccount(account.getEmail(), dataResponse.getValueReponse().get("token").toString());
	}

}
