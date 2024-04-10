package com.iuh.busgoo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.iuh.busgoo.dto.OrderManagerDTO;
import com.iuh.busgoo.entity.Order;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper extends BaseMapper<Order, OrderManagerDTO> {
	
	@Mapping(target = "userName", source = "user.fullName" )
	@Mapping(target = "userCode",source = "user.userCode")
	@Mapping(target ="createDate",source = "createdDate")
	OrderManagerDTO toDto(Order order);
}
