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
import com.iuh.busgoo.filter.PromotionFilter;
import com.iuh.busgoo.requestType.PromotionCreateRequest;
import com.iuh.busgoo.requestType.PromotionDetailRequest;
import com.iuh.busgoo.requestType.PromotionLineRq;
import com.iuh.busgoo.requestType.PromotionUpdateRequest;
import com.iuh.busgoo.service.PromotionService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController()
@RequestMapping("/api/promotion")
public class PromotionController {

	@Autowired
	private PromotionService promotionService;

	@GetMapping("/find")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse getPromotion(@RequestParam(required = false) Integer status, @RequestParam(required = false) String q,
			@RequestParam(required = false) LocalDate fromDate, @RequestParam(required = false) LocalDate toDate,
			@RequestParam Integer itemPerPage, @RequestParam Integer page, @RequestParam(required = false) String sortBy,
			@RequestParam(required = false) String orderBy) {
		try {
			PromotionFilter promotionFilter = new PromotionFilter();
			promotionFilter.setFromDate(fromDate);
			promotionFilter.setItemPerPage(itemPerPage);
			promotionFilter.setOrderBy(orderBy);
			promotionFilter.setPage(page);
			promotionFilter.setSortBy(sortBy);
			promotionFilter.setStatus(status);
			promotionFilter.setToDate(toDate);
			if(q!= null && q.trim().length() == 0) {
				promotionFilter.setQ(null);
			}else {
				promotionFilter.setQ(q);
			}
			return promotionService.getAllPromotionByFilter(promotionFilter);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}
	
	@PostMapping("/create-promotion")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse createPromotion(@RequestBody PromotionCreateRequest promotionCreateRequest) {
		try {
			return promotionService.createPromotion(promotionCreateRequest);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}
	
	@PostMapping("/delete")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse deletePromotion(@RequestBody Long promotionId) {
		try {
			return promotionService.deletePromotion(promotionId);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}
	
	@GetMapping("/get-detail")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse getPromotionLine(@RequestParam Long promotionId) {
		try {
			return promotionService.getPromotionLine(promotionId);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}
	
	@PostMapping("/create-update/promotionline")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse createPromotionLine(@RequestBody PromotionLineRq promotionLineRq) {
		try {
			return promotionService.createPromotionLine(promotionLineRq);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}
	
	@GetMapping("/delete-line")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse deletePromotionLine(@RequestParam Long promotionLineId) {
		try {
			return promotionService.deletePromotionLine(promotionLineId);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}
	
	@GetMapping("/find-detail")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse findPromotionDetail() {
		try {
			return promotionService.findAllDetail();
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}
	
	@PostMapping("/create-detail")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse createPromotionDetail(@RequestBody PromotionDetailRequest promotionDetailRequest) {
		try {
			return promotionService.createPromotionDetail(promotionDetailRequest);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}
	
	@PostMapping("/delete-detail")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse deletePromotionDetail(@RequestParam Long promotionDetailId) {
		try {
			return promotionService.deletePromotionDetail(promotionDetailId);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}
	
	@PostMapping("/update")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse updatePromotion(@RequestBody PromotionUpdateRequest promotionUpdateRequest) {
		try {
			return promotionService.updatePromotion(promotionUpdateRequest);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}
}
