package com.iuh.busgoo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.iuh.busgoo.dto.UserDTO;
import com.iuh.busgoo.entity.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends BaseMapper<User, UserDTO> {
	@Mapping(target = "address", source = "regeionDetail.fullName")
    UserDTO toDto(User entity);
}
