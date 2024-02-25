package com.iuh.busgoo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iuh.busgoo.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
	Account findAccountByEmailAndIsActive(String email,Integer isActive);
	
	Account findAccountByEmail(String email);

}
