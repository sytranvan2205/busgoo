package com.iuh.busgoo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iuh.busgoo.dto.TimeTableDTO;
import com.iuh.busgoo.entity.TimeTable;

@Repository
public interface TimeTableRepository extends JpaRepository<TimeTable,Long>{
	List<TimeTable> findByRouteIdAndBusIdAndStatus(Long routeId,Long busId, Integer status);

	@Query("select new com.iuh.busgoo.dto.TimeTableDTO (b.name as busName, tpb.name as busType, CONCAT(from.fullName,'-',to.fullName) as routeDescription, r.transferTime as timeTransfer, tb.startedTime as timeStarted ) from TimeTable tb "
			+ "INNER JOIN tb.route r "
			+ "INNER JOIN tb.bus b "
			+ "INNER JOIN r.from from "
			+ "INNER JOIN r.to to "
			+ "INNER JOIN b.typeBus tpb "
			+ "where (:status is null or tb.status = :status ) "
			+ "and (:fromId is null or from.id = :fromId ) "
			+ "and (:toId is null or to.id = :toId ) "
			+ "and (:departureDate is null or tb.startedTime >= :departureDate) ")
	Page<TimeTableDTO> findByFilter(Long fromId, Long toId, Integer status, LocalDateTime departureDate, Pageable page);
}
