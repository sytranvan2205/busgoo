package com.iuh.busgoo.service.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class FilterUserRq extends AbstractListRequest{
	private Integer status;
	
}
