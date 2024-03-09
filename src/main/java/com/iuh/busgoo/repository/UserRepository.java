package com.iuh.busgoo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iuh.busgoo.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User getUserByUserCode(String code);

	@Query(value = "select u.* from user u "
			+ "join account a on a.user_id = u.user_id "
			+ "where a.email = ? and a.is_active = 1 ", nativeQuery = true)
	User getUserByEmail(String email);

	User findByPhone(String phone);
	
	List<User> findByStatus(Integer status);
}
