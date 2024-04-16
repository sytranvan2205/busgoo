package com.iuh.busgoo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	
	User findByPhoneAndStatus(String phone, Integer status);
	
	List<User> findByStatus(Integer status);
	
	
	Page<User> findByStatusAndFullNameContaining(Integer status,String fullName, Pageable pageable );
	
	Page<User> findByFullNameContaining(String fullName, Pageable pageable );
	
	Page<User> findByStatus(Integer status, Pageable pageable );

	@Query("select count(u) from User u where u.createdDate >= :firstDayOfMonth and u.createdDate <= :currentDate ")
	Long countDataForDashboard(LocalDate firstDayOfMonth, LocalDate currentDate);
	
}
