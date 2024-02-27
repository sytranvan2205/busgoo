package com.iuh.busgoo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iuh.busgoo.entity.TypeBus;

@Repository
public interface TypeBusRepository extends JpaRepository<TypeBus, Long>{
	TypeBus findByCode(String code);

}
