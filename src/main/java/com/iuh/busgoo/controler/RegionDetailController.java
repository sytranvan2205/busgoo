package com.iuh.busgoo.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iuh.busgoo.constant.Constant;
import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.requestType.RegionDetailRequest;
import com.iuh.busgoo.service.RegionDetailService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/region")
public class RegionDetailController {

	@Autowired
	private RegionDetailService regionDetailService;

	@PostMapping("/find")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse findRegion(@RequestBody RegionDetailRequest detailRequest) {
		try {
			return regionDetailService.getRegionDetailByStructureAndParentId(detailRequest.getParentId(),
					detailRequest.getRegionStructureId());
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

}
