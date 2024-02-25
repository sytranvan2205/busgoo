package com.iuh.busgoo.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iuh.busgoo.dto.RouteDTO;
import com.iuh.busgoo.entity.Route;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long>{
	
	@Query("Select new com.iuh.busgoo.dto.RouteDTO(r.routeCode, from.full_name, to.full_name, tb.start_time, r.transfer_time, b.id, b.name, tb.name, tb.description, pd.value ) "
			+ "from Route r "
			+ "join region_detail from on r.from_id = from.region_detail_id "
			+ "join region_detail to on r.to_id = to.region_detail_id "
			+ "join time_table tb on r.id = tb.route_id "
			+ "join bus b on b.id = tb.bus_id "
			+ "join type_bus tb on b.type_bus_id = tb.id "
			+ "join price_detail pd on pd.id = r.price_detail_id "
			+ "join price p on pd.price_id = p.id "
			+ "where from.code_name = :fromAddressCode "
			+ "and to.code_name = :toAddressCode "
			+ "and tb.start_time > :timeStated "
			+ "order by tb.start_time ASC ")
	List<RouteDTO> getRouteByAddressAndTime(@Param("fromAddressCode") String fromAddressCode,@Param("toAddressCode") String toAddressCode,@Param("timeStated") LocalDate timeStated);
}
