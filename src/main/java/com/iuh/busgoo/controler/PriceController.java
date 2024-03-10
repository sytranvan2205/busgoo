package com.iuh.busgoo.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iuh.busgoo.constant.Constant;
import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.requestType.PriceCreateRequest;
import com.iuh.busgoo.requestType.PriceDetailRequest;
import com.iuh.busgoo.service.PriceService;

import io.swagger.v3.oas.annotations.Operation;
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
			if(priceCreateRequest == null) {
				throw new Exception();
			}
			return dataResponse = priceService.createPrice(priceCreateRequest);
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}
	
	@GetMapping("/find")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse getPrice() {
		try {
			return priceService.getAllPrice();
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}
	
	@PostMapping("/delete")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse deletePrice(@RequestBody Long priceId) {
		try {
			return priceService.deletePrice(priceId);
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
	
	@GetMapping("/get-detail")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse getLstPriceDetail (@RequestParam Long priceId) {
		try {
			return priceService.getPriceDetailByPriceId(priceId);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}
}
