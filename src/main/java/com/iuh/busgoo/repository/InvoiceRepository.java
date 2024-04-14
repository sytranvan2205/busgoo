package com.iuh.busgoo.repository;

import java.time.LocalDate;
import java.util.List;

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

	
	@Query(value = "select i.* from invoice i "
			+ "inner join reservation o on i.order_id = o.id and o.status =1  "
			+ "inner join order_detail od on o.id = od.order_id "
			+ "inner join seat_order so on so.id = od.seat_id "
			+ "inner join time_table tb on tb.id = so.time_table_id and tb.status =1 "
			+ "inner join bus b on tb.bus_id = b.id and b.status = 1 "
			+ "where b.id = :id "
			+ "and (:fromDate is null or i.created_date >= :fromDate )"
			+ "and (:toDate is null or i.created_date <= :toDate) ", nativeQuery = true)
	List<Invoice> findByBusIdAndFromDateAndToDate(Long id, LocalDate fromDate, LocalDate toDate);


	List<Invoice> findByUserId(Long userId);

}
