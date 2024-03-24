package com.iuh.busgoo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iuh.busgoo.entity.PriceDetail;

@Repository
public interface PriceDetailRepository extends JpaRepository<PriceDetail, Long>{
	List<PriceDetail> findByRouteCodeAndTypeBusCode(String roleCode, String typeBusCode);
	
	List<PriceDetail> findByRouteIdAndTypeBusIdAndStatusAndPriceId(Long routeId, Long typeBusId, Integer status, Long priceId);
	
	List<PriceDetail> findByPriceId(Long priceId);
	
	@Query(value=" select pd.* from price_detail pd "
			+ "INNER JOIN price p on pd.price_id = p.id and p.status = 1 "
			+ "INNER JOIN type_bus tpb on pd.type_bus_id = tpb.id and tpb.status = 1 "
			+ "INNER JOIN bus b on tpb.id = b.type_bus_id and b.status =1 "
			+ "INNER JOIN route r on r.id = pd.route_id and r.status =1 "
			+ "INNER JOIN time_table tb on tb.route_id = r.id and tb.bus_id = b.id and tb.status =1 "
			+ "INNER JOIN seat_order so on so.time_table_id = tb.id "
			+ "where p.from_date <= :currentDate "
			+ "and  :currentDate <= p.to_date "
			+ "and so.id = :seatOrderId "
			+ "and pd.status = 1 ",nativeQuery = true)
	PriceDetail findBySeatOrderIdAndCurrDate(Long seatOrderId, LocalDate currentDate);
}
