package com.iuh.busgoo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iuh.busgoo.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{
	List<Order> findByStatus(Integer status);

	@Query("select o from Order o where (:status is null or o.isPay = :status) "
			+ "and (:fromDate is null or o.createdDate >= :fromDate ) "
			+ "and (:toDate is null or o.createdDate <= :toDate ) "
			+ "and (:q is null or o.code like :q )"
			+ "and status = 1")
	Page<Order> findPageFilter(Integer status, LocalDate fromDate, LocalDate toDate, String q, Pageable page);

//	List<Order> findByUserId(Long userId);

	List<Order> findByUserUserIdAndStatus(Long userId, Integer status);
}
