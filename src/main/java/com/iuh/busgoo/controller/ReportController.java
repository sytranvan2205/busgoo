package com.iuh.busgoo.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iuh.busgoo.constant.Constant;
import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.requestType.PageRequest;
import com.iuh.busgoo.service.ReportService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

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
	
	@GetMapping("/sales-by-route")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse salesReportByRoute(@RequestParam(required = false) String routeCode, @RequestParam LocalDate fromDate, @RequestParam LocalDate toDate) {
		try {
			return reportService.salesReportByRoute(routeCode,fromDate,toDate);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
		
	}
	
	@GetMapping("/sales-by-bus/page")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse salesReportByRoutePage(@RequestParam(required = false) String routeCode,@RequestParam LocalDate fromDate, @RequestParam LocalDate toDate, @RequestParam Integer itemPerPage, @RequestParam Integer page, @RequestParam(required = false) String sortBy,
			@RequestParam(required = false) String orderBy) {
		try {
			PageRequest pageRequest = new PageRequest();
			pageRequest.setItemPerPage(itemPerPage);
			pageRequest.setOrderBy(orderBy);
			pageRequest.setPage(page -1);
			pageRequest.setSortBy(sortBy);
			return reportService.salesReportByRoutePage(routeCode,fromDate,toDate,pageRequest);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
		
	}
	
//	@GetMapping("/download/{fileName}")
//	public ResponseEntity<Resource> downloadFileExcel(@PathVariable String fileName) throws MalformedURLException {
//		Resource resource = new UrlResource(Paths.get(fileName).toUri());
//		if(resource.exists() && resource.isReadable()) {
//            return ResponseEntity.ok()
//                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
//                    .body(resource);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//	}
	
    @GetMapping("/download")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Resource> downloadFile(@RequestParam String fileName) throws IOException {
//        String fileName = "Bao_cao_doanh_thu_hang_xe_1714925318619.xlsx";
        String filePath = "upload/report/" + fileName;
        Path path = Paths.get(filePath);

        Resource resource = null;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
	
	@GetMapping("/promotion-export")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse reportPromotion(@RequestParam(required = false) String promotionCode,@RequestParam LocalDate fromDate, @RequestParam LocalDate toDate) {
		try {
			return reportService.reportPromotion(promotionCode,fromDate,toDate);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}
	
	@GetMapping("/sale-of-customer")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse reportSaleByUser(@RequestParam(required = false) String userCode,@RequestParam LocalDate fromDate, @RequestParam LocalDate toDate) {
		try {
			return reportService.reportSaleByUser(userCode,fromDate,toDate);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}
	
	@GetMapping("/sale-of-customer/page")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse reportSaleByUserPage(@RequestParam(required = false) String userCode, @RequestParam LocalDate fromDate, @RequestParam LocalDate toDate, @RequestParam Integer itemPerPage, @RequestParam Integer page, @RequestParam(required = false) String sortBy,
			@RequestParam(required = false) String orderBy) {
		try {
			PageRequest pageRequest = new PageRequest();
			pageRequest.setItemPerPage(itemPerPage);
			pageRequest.setOrderBy(orderBy);
			pageRequest.setPage(page -1);
			pageRequest.setSortBy(sortBy);
			return reportService.reportSaleByUserPage(userCode,fromDate,toDate,pageRequest);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
		
	}
	
	@GetMapping("/promotion-export/page")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse reportPromotionPage(@RequestParam(required = false) String promotionCode, @RequestParam LocalDate fromDate, @RequestParam LocalDate toDate, @RequestParam Integer itemPerPage, @RequestParam Integer page, @RequestParam(required = false) String sortBy,
			@RequestParam(required = false) String orderBy) {
		try {
			PageRequest pageRequest = new PageRequest();
			pageRequest.setItemPerPage(itemPerPage);
			pageRequest.setOrderBy(orderBy);
			pageRequest.setPage(page -1);
			pageRequest.setSortBy(sortBy);
			return reportService.reportPromotionPage(promotionCode,fromDate,toDate,pageRequest);
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
	
	@GetMapping("/return-invoice-report")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse reportInvoiceReturn(@RequestParam LocalDate fromDate, @RequestParam LocalDate toDate) {
		try {
			return reportService.reportInvoiceReturn(fromDate,toDate);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}
	
	@GetMapping("/return-invoice-report/page")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse reportInvoiceReturnPage(@RequestParam LocalDate fromDate, @RequestParam LocalDate toDate, @RequestParam Integer itemPerPage, @RequestParam Integer page, @RequestParam(required = false) String sortBy,
			@RequestParam(required = false) String orderBy) {
		try {
			PageRequest pageRequest = new PageRequest();
			pageRequest.setItemPerPage(itemPerPage);
			pageRequest.setOrderBy(orderBy);
			pageRequest.setPage(page -1);
			pageRequest.setSortBy(sortBy);
			return reportService.reportInvoiceReturnPage(fromDate,toDate,pageRequest);
		} catch (Exception e) {
			DataResponse dataResponse = new DataResponse();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
		
	}
	
}
