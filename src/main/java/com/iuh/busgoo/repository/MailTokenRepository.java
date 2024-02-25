package com.iuh.busgoo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iuh.busgoo.entity.MailToken;

@Repository
public interface MailTokenRepository extends JpaRepository<MailToken, Long>{
	@Query(value = "Select mt.* from mail_token mt where mt.token = ? ",nativeQuery = true)
	MailToken findByToken(String token);
	Long removeByToken(String token);
}
