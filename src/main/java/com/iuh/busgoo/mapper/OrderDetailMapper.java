package com.iuh.busgoo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.iuh.busgoo.dto.OrderDetailDTO;
import com.iuh.busgoo.entity.OrderDetail;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderDetailMapper extends BaseMapper<OrderDetail, OrderDetailDTO> {
	
	@Mapping(target = "seatId", source = "seat.id")
	@Mapping(target = "seatName", source = "seat.seatName")
	@Mapping(target = "orderId", source = "order.id")
	OrderDetailDTO toDto(OrderDetail od);
}
