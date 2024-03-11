package com.iuh.busgoo.filter;

import java.time.LocalDate;

import com.iuh.busgoo.service.impl.AbstractListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper=false)
public class PriceFilter extends AbstractListRequest{
	private Integer status;
	private LocalDate fromDate;
	private LocalDate toDate;
}
