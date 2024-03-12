package com.iuh.busgoo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iuh.busgoo.entity.RegionDetail;

@Repository
public interface RegionDetailRepository extends JpaRepository<RegionDetail, Long>{
	
	@Query(value = "select rd.* from region_detail rd join region_structure rs on rd.regeion_structure_id = rs.region_structure_id where rd.status =1 and rs.region_code = ? ", nativeQuery = true)
	List<RegionDetail> getLstRegionDetailByStructureCode(String structureCode);
	
	@Query(value = "select rd.* from region_detail rd join region_structure rs on rd.regeion_structure_id = rs.region_structure_id where rd.status =1 and rs.region_code = ?1 and rd.region_parent_id = ?2 ", nativeQuery = true)
	List<RegionDetail> getLstRegionDetailByParentIdAndStructureCode(@Param("region_code") Long regionParentId,@Param("region_code") String structureCode);
	
	RegionDetail findRegionDetailByDetailCode(String detailCode);
	
	List<RegionDetail> findByStatusAndRegionParentIdAndRegionStructureId(Integer status, Long regionParentId, Long RegionStructureId);
	
	List<RegionDetail> findByStatusAndRegionStructureId(Integer status, Long regionStructureId);
	
	
	
}
