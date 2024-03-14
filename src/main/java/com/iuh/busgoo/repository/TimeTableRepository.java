package com.iuh.busgoo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iuh.busgoo.entity.TimeTable;

@Repository
public interface TimeTableRepository extends JpaRepository<TimeTable,Long>{

}
