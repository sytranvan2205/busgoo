package com.iuh.busgoo.service.impl;

import lombok.Data;

@Data
public class AbstractListRequest {
	protected String q;
	protected Integer itemPerPage;
	protected Integer page;
	protected String sortBy;
	protected String orderBy;
}
