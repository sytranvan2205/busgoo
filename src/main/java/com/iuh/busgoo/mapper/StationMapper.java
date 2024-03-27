package com.iuh.busgoo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.iuh.busgoo.dto.StationDTO;
import com.iuh.busgoo.entity.Station;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StationMapper extends BaseMapper<Station, StationDTO> {
	
	@Mapping(target = "addressId", source ="regionDetail.id")
	@Mapping(target = "address", source ="regionDetail.fullName")
	@Mapping(target = "addressParent", source ="regionDetail.regionParent.fullName")
	StationDTO toDto(Station station);
}
