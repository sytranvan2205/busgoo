package com.iuh.busgoo.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.iuh.busgoo.constant.Constant;
import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.filter.PriceFilter;
import com.iuh.busgoo.requestType.PriceCreateRequest;
import com.iuh.busgoo.requestType.PriceDetailRequest;
import com.iuh.busgoo.requestType.PriceUpdateRequest;
import com.iuh.busgoo.service.PriceService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController()
@RequestMapping("/api/price")
public class PriceController {

	@Autowired
	private PriceService priceService;

	@PostMapping("/create")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse createPrice(@RequestBody PriceCreateRequest priceCreateRequest) {
		DataResponse dataResponse = new DataResponse();
		try {
			if (priceCreateRequest == null) {
				throw new Exception();
			}
			return dataResponse = priceService.createPrice(priceCreateRequest);
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}
	
	@PostMapping("/update")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse updatePrice(@RequestBody PriceUpdateRequest priceUpdateRequest) {
		try {
			return priceService.updatePrice(priceUpdateRequest);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@GetMapping("/find")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse getPrice(@RequestParam(required = false) Integer status,
			@RequestParam(required = false) LocalDate fromDate, @RequestParam(required = false) LocalDate toDate,
			@RequestParam Integer itemPerPage, @RequestParam Integer page, @RequestParam(required = false) String sortBy,
			@RequestParam(required = false) String orderBy) {
		try {
			PriceFilter priceFilter = new PriceFilter();
			priceFilter.setFromDate(fromDate);
			priceFilter.setItemPerPage(itemPerPage);
			priceFilter.setOrderBy(orderBy);
			priceFilter.setPage(page);
			priceFilter.setSortBy(sortBy);
			priceFilter.setStatus(status);
			priceFilter.setToDate(toDate);
			return priceService.getAllPriceByFilter(priceFilter);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@DeleteMapping("/delete/{id}")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse deletePrice(@PathVariable Long id) {
		try {
			return priceService.deletePrice(id);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@PostMapping("/create-detail")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse createPriceDetail(@RequestBody PriceDetailRequest priceDetailRequest) {
		try {
			return priceService.createPriceDetail(priceDetailRequest);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@GetMapping("/find-detail")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse getLstPriceDetail(@RequestParam Long priceId) {
		try {
			return priceService.getPriceDetailByPriceId(priceId);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}
	
	@GetMapping("/get")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse getPriceById(@RequestParam Long priceId) {
		try {
			return priceService.findPriceById(priceId);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}
	
	@GetMapping("/get-detail")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse getPriceDetailById(@RequestParam Long priceDetailId) {
		try {
			return priceService.findPriceDetailById(priceDetailId);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}
	
	@DeleteMapping("/delete-price-detail/{id}")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse deletePriceDetail(@PathVariable Long id) {
		try {
			return priceService.deletePriceDetailById(id);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}
}
