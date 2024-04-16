package com.iuh.busgoo.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iuh.busgoo.constant.Constant;
import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.filter.InvoiceFilter;
import com.iuh.busgoo.requestType.ReturnRequest;
import com.iuh.busgoo.service.InvoiceService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController()
@RequestMapping("/api/invoice")
public class InvoiceController {
	
	@Autowired
	private InvoiceService invoiceService;
	
	@GetMapping("/find")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse getInvoice(@RequestParam(required = false) Integer status,
			@RequestParam(required = false) LocalDate fromDate, @RequestParam(required = false) LocalDate toDate,
			@RequestParam Integer itemPerPage, @RequestParam Integer page, @RequestParam(required = false) String sortBy,
			@RequestParam(required = false) String orderBy) {
		try {
			InvoiceFilter invoiceFilter = new InvoiceFilter();
			invoiceFilter.setFromDate(fromDate);
			invoiceFilter.setItemPerPage(itemPerPage);
			invoiceFilter.setOrderBy(orderBy);
			invoiceFilter.setPage(page - 1);
			invoiceFilter.setSortBy(sortBy);
			invoiceFilter.setStatus(status);
			invoiceFilter.setToDate(toDate);
			return invoiceService.getInvoiceByFilter(invoiceFilter);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}
	
	@GetMapping("/get")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse findById(@RequestParam Long invoiceId) {
		try {
			return invoiceService.getInvoiceById(invoiceId);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}
	
	@PostMapping("/return")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse returnInvoice(@RequestBody ReturnRequest returnRequest) {
		try {
			return invoiceService.returnInvoice(returnRequest);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}
	
	@GetMapping("/mobile/find")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse findInvoiceForMobile(Long userId) {
		try {
			return invoiceService.getInvoiceForMobileId(userId);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}
	
	@GetMapping("/find-invoice-return")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse getInvoiceReturn(@RequestParam(required = false) Integer status,
			@RequestParam(required = false) LocalDate fromDate, @RequestParam(required = false) LocalDate toDate,
			@RequestParam Integer itemPerPage, @RequestParam Integer page, @RequestParam(required = false) String sortBy,
			@RequestParam(required = false) String orderBy) {
		try {
			InvoiceFilter invoiceFilter = new InvoiceFilter();
			invoiceFilter.setFromDate(fromDate);
			invoiceFilter.setItemPerPage(itemPerPage);
			invoiceFilter.setOrderBy(orderBy);
			invoiceFilter.setPage(page - 1);
			invoiceFilter.setSortBy(sortBy);
			invoiceFilter.setStatus(status);
			invoiceFilter.setToDate(toDate);
			return invoiceService.getInvoiceReturnByFilter(invoiceFilter);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}
		
}
