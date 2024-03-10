package com.iuh.busgoo.filter;

import lombok.Data;

@Data
public class UserFilter {
	private String searchRequest;
	private Integer status;
}
