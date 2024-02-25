package com.iuh.busgoo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iuh.busgoo.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
	Role findByCodeAndStatus(String code,Integer status);
}
