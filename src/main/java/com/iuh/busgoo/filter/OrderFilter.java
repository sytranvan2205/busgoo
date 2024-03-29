package com.iuh.busgoo.filter;

import java.time.LocalDate;

import com.iuh.busgoo.service.impl.AbstractListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class OrderFilter extends AbstractListRequest{
	private LocalDate fromDate;
	private LocalDate toDate;
	private Integer status;
}
