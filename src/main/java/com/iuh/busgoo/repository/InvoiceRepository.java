package com.iuh.busgoo.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iuh.busgoo.entity.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long>{
	
//	@Query("select o from Order o where (:status is null or o.isPay = :status) "
//			+ "and (:fromDate is null or o.createdDate >= :fromDate ) "
//			+ "and (:toDate is null or o.createdDate <= :toDate ) "
//			+ "and (:q is null or o.code like :q )")
//	Page<Order> findPageFilter(Integer status, LocalDate fromDate, LocalDate toDate, String q, Pageable page);

	@Query("select o from Invoice o where (:status is null or o.status = :status) "
			+ "and (:fromDate is null or o.createdDate >= :fromDate ) "
			+ "and (:toDate is null or o.createdDate <= :toDate ) "
			+ "and (:q is null or o.code like :q )")
	Page<Invoice> findPageFilter(Integer status, LocalDate fromDate, LocalDate toDate, String q, Pageable page);

}
