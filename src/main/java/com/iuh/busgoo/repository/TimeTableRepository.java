package com.iuh.busgoo.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iuh.busgoo.dto.BustripDTO;
import com.iuh.busgoo.dto.TimeTableDTO;
import com.iuh.busgoo.entity.TimeTable;

@Repository
public interface TimeTableRepository extends JpaRepository<TimeTable, Long> {
	List<TimeTable> findByRouteIdAndBusIdAndStatus(Long routeId, Long busId, Integer status);

	@Query("select new com.iuh.busgoo.dto.TimeTableDTO (tb.id as id,tb.code as code,b.name as busName, tpb.name as busType, CONCAT(from.fullName,'-',to.fullName) as routeDescription, r.transferTime as timeTransfer, tb.startedTime as timeStarted, tb.status as status ) from TimeTable tb "
			+ "INNER JOIN tb.route r " + "INNER JOIN tb.bus b " + "INNER JOIN r.from from " + "INNER JOIN r.to to "
			+ "INNER JOIN b.typeBus tpb " + "where (:status is null or tb.status = :status ) "
			+ "and (:fromId is null or from.id = :fromId ) " + "and (:toId is null or to.id = :toId ) "
			+ "and (:departureDate is null or tb.startedTime >= :departureDate) ")
	Page<TimeTableDTO> findByFilter(Long fromId, Long toId, Integer status, LocalDateTime departureDate, Pageable page);

	@Query(value = "select tb.id as timeTableId, tb.started_time as timeStated, pd.id as priceDetailId, pd.value as priceValue, r.id as routeId, r.transfer_time as transferTime, f.full_Name as fromName, t.full_Name as toName "
			+ "from time_table tb " + "INNER JOIN route r on tb.route_id =  r.id and r.status = 1 "
			+ "INNER JOIN bus b on tb.bus_id = b.id and b.status = 1  "
			+ "INNER JOIN region_detail f on r.from_id = f.id and f.status = 1 "
			+ "INNER JOIN region_detail t on r.to_id = t.id and t.status = 1 "
			+ "INNER JOIN type_bus tpb on b.type_bus_id = tpb.id and tpb.status = 1 "
			+ "INNER JOIN price_detail pd on pd.route_id = r.id and pd.type_bus_id = tpb.id and pd.status = 1 "
			+ "INNER JOIN price p on pd.price_id = p.id and p.status = 1 " + "where  "
			+ "(:fromId is null or f.id = :fromId ) " + "and (:toId is null or t.id = :toId ) "
			+ "and (:timeStarted is null or tb.started_time >= :timeStarted) " + "and p.from_date <= :currDate "
			+ "and p.to_date >= :currDate " + "order by tb.started_time asc ", nativeQuery = true)
	List<Object[]> findBusTripByFilter(Long fromId, Long toId, LocalDate timeStarted, LocalDate currDate);
}
