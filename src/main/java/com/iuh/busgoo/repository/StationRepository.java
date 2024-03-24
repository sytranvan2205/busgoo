package com.iuh.busgoo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iuh.busgoo.entity.Station;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {

	List<Station> findByRegionDetailId(Long regionDetailId);

}
