package com.iuh.busgoo.service;

import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.filter.InvoiceFilter;
import com.iuh.busgoo.requestType.ReturnRequest;

public interface InvoiceService {
	DataResponse getInvoiceByFilter(InvoiceFilter invoiceFilter);

	DataResponse getInvoiceById(Long invoiceId);

	DataResponse returnInvoice(ReturnRequest returnRequest);
}
