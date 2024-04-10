package com.iuh.busgoo.service;

import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.filter.InvoiceFilter;

public interface InvoiceService {
	DataResponse getInvoiceByFilter(InvoiceFilter invoiceFilter);

	DataResponse getInvoiceById(Long invoiceId);
}
