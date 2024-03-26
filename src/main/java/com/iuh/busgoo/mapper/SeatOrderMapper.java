package com.iuh.busgoo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.iuh.busgoo.dto.SeatOrderDTO;
import com.iuh.busgoo.entity.SeatOrder;




@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SeatOrderMapper extends BaseMapper<SeatOrder, SeatOrderDTO>{
	@Mapping(target = "orderDetailId", source ="orderDetail.id")
	@Mapping(target = "timeTableId", source = "timeTable.id")
	SeatOrderDTO toDto(SeatOrder entity);
}
