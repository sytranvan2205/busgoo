package com.iuh.busgoo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iuh.busgoo.dto.RouteDTO;
import com.iuh.busgoo.entity.Route;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long>{
	
	@Query(value = "Select r.code as routeCode, from.full_name as fromAddress, to.full_name as toAddress, "
			+ "tb.start_time as startTime, r.transfer_time as transferTime, b.id as busId, b.name as nameBus, "
			+ "tbs.name as typeBusName, tbs.description as typeBusDescription, pd.value price ) "
			+ "from Route r "
			+ "join region_detail from on r.id = from.from_id "
			+ "join region_detail to on r.id = to.to_id "
			+ "join time_table tb on r.id = tb.route_id "
			+ "join bus b on b.id = tb.bus_id "
			+ "join type_bus tbs on b.type_bus_id = tbs.id "
			+ "join price p on p.route_id = r.id and p.type_bus_id = tbs.id "
			+ "join price_detail pd on pd.price_id = p.id "
			+ "where from.regeion_detail_code = :fromAddressCode "
			+ "and to.regeion_detail_code = :toAddressCode "
			+ "and tb.start_time > :timeStated "
			+ "order by tb.start_time ASC ",nativeQuery = true)
	List<RouteDTO> getRouteByAddressAndTime(@Param("fromAddressCode") String fromAddressCode,@Param("toAddressCode") String toAddressCode,@Param("timeStated") LocalDate timeStated);

	Route findByCode(String code);
	
	List<Route> findByFromDetailCodeAndToDetailCodeAndStatus(String fromDetailCode,String toDetailCode, Integer status);

	List<Route> findByStatus(Integer status);

	@Query("Select r from Route r "
			+ "INNER JOIN r.to as to "
			+ "INNER JOIN r.from as from where"
			+ "(:status is null or r.status = :status ) "
			+ "and (:fromId is null or from.id = :fromId) "
			+ "and (:toId is null or to.id = :toId )")
	Page<Route> findRouteByFilter(Integer status, Long fromId, Long toId, Pageable page);
	
}
