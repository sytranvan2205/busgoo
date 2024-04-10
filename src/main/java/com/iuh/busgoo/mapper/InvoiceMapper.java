package com.iuh.busgoo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.iuh.busgoo.dto.InvoiceDTO;
import com.iuh.busgoo.entity.Invoice;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InvoiceMapper extends BaseMapper<Invoice, InvoiceDTO> {
	
	InvoiceDTO toDto(Invoice invoice);
}
