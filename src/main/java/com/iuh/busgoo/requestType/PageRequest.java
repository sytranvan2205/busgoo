package com.iuh.busgoo.requestType;

import lombok.Data;

@Data
public class PageRequest {
	protected Integer itemPerPage;
	protected Integer page;
	protected String sortBy;
	protected String orderBy;
}
