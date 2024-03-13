package com.iuh.busgoo.filter;

import com.iuh.busgoo.service.impl.AbstractListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class RouteFilter extends AbstractListRequest{
	private Integer status;
	private Long fromId;
	private Long toId;
}
