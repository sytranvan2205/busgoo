package com.iuh.busgoo.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iuh.busgoo.constant.Constant;
import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.requestType.PageRequest;
import com.iuh.busgoo.service.ReportService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;

@RestController()
@RequestMapping("/api/report")
public class ReportController {
	
//	private String fileName = "template/report/Bao_cao_doanh_thu_hang_xe_theo_thoi_gian.xlsx"; 
	@Autowired
	private ReportService reportService;
	
//    private ServletContext servletContext;
//
//    @Autowired
//    public void setServletContext(ServletContext servletContext) {
//        this.servletContext = servletContext;
//    }

	@GetMapping("/sales-by-bus")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse salesReportByBus(@RequestParam LocalDate fromDate, @RequestParam LocalDate toDate) {
		try {
			return reportService.salesByBusExport(fromDate,toDate);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
		
	}
	
	@GetMapping("/sales-by-bus/page")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse salesReportByBusPage(@RequestParam LocalDate fromDate, @RequestParam LocalDate toDate, @RequestParam Integer itemPerPage, @RequestParam Integer page, @RequestParam(required = false) String sortBy,
			@RequestParam(required = false) String orderBy) {
		try {
			PageRequest pageRequest = new PageRequest();
			pageRequest.setItemPerPage(itemPerPage);
			pageRequest.setOrderBy(orderBy);
			pageRequest.setPage(page -1);
			pageRequest.setSortBy(sortBy);
			return reportService.salesByBusExportPage(fromDate,toDate,pageRequest);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
		
	}
	
	@GetMapping("/dashboard")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse getDataForDashBoard() {
		try {
			return reportService.getDataForDashBoard();
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
		
	}
	
}
