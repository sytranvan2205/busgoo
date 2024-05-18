package com.iuh.busgoo.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.iuh.busgoo.constant.Constant;
import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.dto.OrderManagerDTO;
import com.iuh.busgoo.dto.ReportDTO;
import com.iuh.busgoo.entity.Bus;
import com.iuh.busgoo.entity.Invoice;
import com.iuh.busgoo.entity.OrderDetail;
import com.iuh.busgoo.entity.PromotionLine;
import com.iuh.busgoo.entity.RegionDetail;
import com.iuh.busgoo.entity.Route;
import com.iuh.busgoo.entity.User;
import com.iuh.busgoo.repository.BusRepository;
import com.iuh.busgoo.repository.InvoiceRepository;
import com.iuh.busgoo.repository.OrderDetailRepository;
import com.iuh.busgoo.repository.OrderRepository;
import com.iuh.busgoo.repository.PromotionLineRepository;
import com.iuh.busgoo.repository.RegionDetailRepository;
import com.iuh.busgoo.repository.RouteRepository;
import com.iuh.busgoo.repository.TimeTableRepository;
import com.iuh.busgoo.repository.UserRepository;
import com.iuh.busgoo.requestType.PageRequest;
import com.iuh.busgoo.service.ReportService;

import jakarta.servlet.ServletContext;
import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

@Service
public class ReportServiceImpl implements ReportService {

	@Autowired
	private BusRepository busRepository;
	
	@Autowired
	private InvoiceRepository invoiceRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private RouteRepository routeRepository;
	
	@Autowired
    private String basePath;
	
	@Autowired
    private ServletContext servletContext;
	
	@Autowired
	private PromotionLineRepository promotionLineRepository;
	
	@Autowired
	private TimeTableRepository timeTableRepository;
	
	@Autowired
	RegionDetailRepository regionDetailRepository;
	
	 private static final Logger LOGGER = LogManager.getLogger(ReportServiceImpl.class);
	
	
	@Override
	public DataResponse salesByBusExport(LocalDate fromDate, LocalDate toDate) throws IOException {
		DataResponse dataResponse = new DataResponse();
		Workbook resultWorkbook = null;
		InputStream inputStream = null;
		OutputStream os = null;
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
//			classLoader.getResource(null)
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			String strFromDate = fromDate.format(formatter);
			String strToDate = toDate.format(formatter);
			String templateName = Constant.RESOURCE_TEMPLATE_PATH + Constant.SALE_TEMPLATE_REPORT;
			Map<String, Object> beans = new HashMap<String, Object>();
			beans.put("fromDate", strFromDate);
			beans.put("toDate", strToDate);
			List<ReportDTO> dtos = new ArrayList<ReportDTO>();
			List<Bus> list = busRepository.findByStatus(1);
			for(Bus bus : list) {
				ReportDTO reportDTO = new ReportDTO();
				reportDTO.setBusName(bus.getName());
				reportDTO.setBusCode(bus.getCode());
				reportDTO.setTypeBusCode(bus.getTypeBus().getCode());
				reportDTO.setBusType(bus.getTypeBus().getName());
				List<Invoice> invoices = invoiceRepository.findByBusIdAndFromDateAndToDate(bus.getId(),fromDate,toDate);
				if(invoices == null || invoices.size() ==0) {
					reportDTO.setRevenue(0L);
					reportDTO.setDiscount(0L);
					reportDTO.setTicketPrice(0L);
				}else {
					Long value = 0L;
					Long tiketPrice = 0L;
					Long discount = 0L;
					for(Invoice invoice : invoices) {
						value += invoice.getTotal().longValue();
						tiketPrice += invoice.getTotalTiketPrice() != null ? invoice.getTotalTiketPrice().longValue(): 0L ;
						discount += invoice.getTotalDiscount()!= null ? invoice.getTotalDiscount().longValue() : 0L ;
					}
					reportDTO.setRevenue(value);
					reportDTO.setDiscount(discount);
					reportDTO.setTicketPrice(tiketPrice);
				}
				dtos.add(reportDTO);
			}
			beans.put("lstData", dtos);
			Long total = 0L;
			for(ReportDTO dto :dtos) {
				total += dto.getRevenue();
			}
			beans.put("total", total);
			inputStream = getClass().getClassLoader().getResourceAsStream(templateName);
			XLSTransformer transformer = new XLSTransformer();
			resultWorkbook = transformer.transformXLS(inputStream, beans);
			
			String timestamp = String.valueOf(Instant.now().toEpochMilli());
			String fileName = Constant.SALE_BY_BUS_FILE_NAME+"_"+timestamp+Constant.EXCEL_EXTENSION;
			String reportPath = Constant.EXPORT_REPORT_PATH;
			// Tạo thư mục nếu chưa tồn tại
			File directory = new File(reportPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }
			os = new BufferedOutputStream(new FileOutputStream(reportPath + fileName));

			resultWorkbook.write(os);
			os.flush();
			dataResponse.setResponseMsg("Export report success !!!");
			dataResponse.setRespType(Constant.HTTP_SUCCESS);
			Map<String, Object> respValue = new HashMap<>();
			respValue.put("fileName",fileName);
			dataResponse.setValueReponse(respValue);
			return dataResponse;
		} catch (ParsePropertyException | InvalidFormatException | IOException e) {
			// TODO Auto-generated catch block
			LOGGER.error("An error occurred",e);
			e.printStackTrace();
			dataResponse.setResponseMsg("Failed to upload file.");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		} finally {
			if(inputStream != null) {
				inputStream.close();
			}
			if(os != null) {
				os.close();
			}
		}
	}

	@Override
	public DataResponse getDataForDashBoard() {
		DataResponse dataResponse = new DataResponse();
		try {
			LocalDate currentDate = LocalDate.now();
			LocalDate firstDayOfMonth = currentDate.with(TemporalAdjusters.firstDayOfMonth());
			LocalDate firstDayOfPreviousMonth = currentDate.with(TemporalAdjusters.firstDayOfMonth()).minusMonths(1);
			LocalDate lastDayOfPreviousMonth = currentDate.with(TemporalAdjusters.firstDayOfMonth()).minusDays(1);
			Long countInvoiceInCurrMonth = invoiceRepository.countDataForDashboard(firstDayOfMonth,currentDate);
			Long countNewUserInCurrMonth = userRepository.countDataForDashboard(firstDayOfMonth,currentDate);
			Double sumTotalInvoiceInMonth = invoiceRepository.sumTotalInvoiceInMonth(firstDayOfMonth,currentDate);
			Long countOrderInprogress = orderRepository.countOrderInprogress(firstDayOfMonth,currentDate);
			
			Long countInvoiceLastMonth = invoiceRepository.countDataForDashboard(firstDayOfPreviousMonth,lastDayOfPreviousMonth);
			Long countNewUserLastMonth = userRepository.countDataForDashboard(firstDayOfPreviousMonth,lastDayOfPreviousMonth);
			Double sumTotalInvoiceLastMonth = invoiceRepository.sumTotalInvoiceInMonth(firstDayOfPreviousMonth,lastDayOfPreviousMonth);
			Long countOrderInprogressLastMonth = orderRepository.countOrderInprogress(firstDayOfPreviousMonth,lastDayOfPreviousMonth);
			
//			Double percentInvoiceNew = (1- (countInvoiceLastMonth*1.0/countInvoiceInCurrMonth))*100;
//			Double percentUserNew = (1- (countNewUserLastMonth*1.0/countNewUserInCurrMonth))*100;
//			Double percentIncome = (1- (sumTotalInvoiceLastMonth*1.0/sumTotalInvoiceInMonth))*100;
//			Double percentOrderInprogress = (1- (countOrderInprogressLastMonth*1.0/countOrderInprogress))*100;
			
			dataResponse.setResponseMsg("Get data success !!!");
			dataResponse.setRespType(Constant.HTTP_SUCCESS);
			Map<String, Object> respValue = new HashMap<>();
			respValue.put("countInvoice",countInvoiceInCurrMonth);
			respValue.put("countUser", countNewUserInCurrMonth);
			respValue.put("income", sumTotalInvoiceInMonth);
			respValue.put("countOrderInprogress", countOrderInprogress);
//			respValue.put("percentInvoiceNew", percentInvoiceNew);
//			respValue.put("percentUserNew", percentUserNew);
//			respValue.put("percentIncome", percentIncome);
//			respValue.put("percentOrderInprogress", percentOrderInprogress);
			dataResponse.setValueReponse(respValue);
			return dataResponse;
		} catch (Exception e) {
			e.printStackTrace();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@Override
	public DataResponse salesByBusExportPage(LocalDate fromDate, LocalDate toDate, PageRequest pageRequest) {
		DataResponse dataResponse = new DataResponse();
		try {
			Pageable page;
			Sort sort;
			if(pageRequest.getSortBy() != null && pageRequest.getOrderBy() != null) {
				if(pageRequest.getSortBy().toUpperCase().equals("ASC")) {
					sort = Sort.by(pageRequest.getOrderBy()).ascending();
				}else {
					sort = Sort.by(pageRequest.getOrderBy()).descending();
				}
				page= org.springframework.data.domain.PageRequest.of(pageRequest.getPage(), pageRequest.getItemPerPage(), sort);
			}else {
				page = org.springframework.data.domain.PageRequest.of(pageRequest.getPage(), pageRequest.getItemPerPage());
			}
			List<ReportDTO> dtos = new ArrayList<ReportDTO>();
			List<Bus> list = busRepository.findByStatus(1);
			for(Bus bus : list) {
				ReportDTO reportDTO = new ReportDTO();
				reportDTO.setBusName(bus.getName());
				reportDTO.setBusCode(bus.getCode());
				reportDTO.setTypeBusCode(bus.getTypeBus().getCode());
				reportDTO.setBusType(bus.getTypeBus().getName());
				List<Invoice> invoices = invoiceRepository.findByBusIdAndFromDateAndToDate(bus.getId(),fromDate,toDate);
				if(invoices == null || invoices.size() ==0) {
					reportDTO.setRevenue(0L);
					reportDTO.setDiscount(0L);
					reportDTO.setTicketPrice(0L);
				}else {
					Long value = 0L;
					Long tiketPrice = 0L;
					Long discount = 0L;
					for(Invoice invoice : invoices) {
						value += invoice.getTotal().longValue();
						tiketPrice += invoice.getTotalTiketPrice() != null ? invoice.getTotalTiketPrice().longValue(): 0L ;
						discount += invoice.getTotalDiscount()!= null ? invoice.getTotalDiscount().longValue() : 0L ;
					}
					reportDTO.setRevenue(value);
					reportDTO.setDiscount(discount);
					reportDTO.setTicketPrice(tiketPrice);
				}
				dtos.add(reportDTO);
			}
			Page<ReportDTO> reportDTOPage = new PageImpl<>(dtos, page, dtos.size());
			dataResponse.setResponseMsg("Get data report success !!!");
			dataResponse.setRespType(Constant.HTTP_SUCCESS);
			Map<String, Object> respValue = new HashMap<>();
			respValue.put("data",reportDTOPage);
			dataResponse.setValueReponse(respValue);
			return dataResponse;
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@Override
	public DataResponse reportPromotion(String promotionCode, LocalDate fromDate, LocalDate toDate) throws IOException {
		DataResponse dataResponse = new DataResponse();
		Workbook resultWorkbook = null;
		InputStream inputStream = null;
		OutputStream os = null;
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
//			classLoader.getResource(null)
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			String strFromDate = fromDate.format(formatter);
			String strToDate = toDate.format(formatter);
			String templateName = Constant.RESOURCE_TEMPLATE_PATH + Constant.PROMOTION_TEMPLATE_REPORT;
			Map<String, Object> beans = new HashMap<String, Object>();
			beans.put("fromDate", strFromDate);
			beans.put("toDate", strToDate);
			List<ReportDTO> dtos = new ArrayList<ReportDTO>();
			List<PromotionLine> lstLine = promotionLineRepository.findPromotionLineForReport(promotionCode, fromDate,
					toDate);
			for (PromotionLine line : lstLine) {
				ReportDTO data = new ReportDTO();
				data.setPromotionCode(line.getCode());
				data.setPromotionName(line.getLineName());
				data.setPromotionFDate(line.getFromDate());
				data.setPromotionTDate(line.getToDate());
				Long totalDiscount = Long.valueOf(0);
				Long tiketPrice = 0L;
				Long value = 0L;
				List<Invoice> lstInvoice = invoiceRepository.findInvoiceByPromotionReport(line.getId());
				for (Invoice invoice : lstInvoice) {
					totalDiscount += invoice.getTotalDiscount().longValue();
					tiketPrice += invoice.getTotalTiketPrice() != null ? invoice.getTotalTiketPrice().longValue(): 0L ;
					value += invoice.getTotal().longValue();
				}
				data.setTicketPrice(tiketPrice);
				data.setRevenue(value);
				data.setDiscount(totalDiscount);
				dtos.add(data);
			}
			beans.put("lstData", dtos);
			Long total = 0L;
			for (ReportDTO dto : dtos) {
				total += dto.getRevenue();
			}
			beans.put("total", total);
			inputStream = getClass().getClassLoader().getResourceAsStream(templateName);
			XLSTransformer transformer = new XLSTransformer();
			resultWorkbook = transformer.transformXLS(inputStream, beans);

			String timestamp = String.valueOf(Instant.now().toEpochMilli());

			String fileName = Constant.REPORT_PROMOTION + "_" + timestamp + Constant.EXCEL_EXTENSION;
			String reportPath = Constant.EXPORT_REPORT_PATH;
			// Tạo thư mục nếu chưa tồn tại
			File directory = new File(reportPath);
			if (!directory.exists()) {
				directory.mkdirs();
			}
			os = new BufferedOutputStream(new FileOutputStream(reportPath + fileName));

			resultWorkbook.write(os);
			os.flush();
			dataResponse.setResponseMsg("Export report success !!!");
			dataResponse.setRespType(Constant.HTTP_SUCCESS);
			Map<String, Object> respValue = new HashMap<>();
			String fileExportUrl = fileName;
			fileExportUrl = fileExportUrl.replaceAll("\\\\", "/");
			respValue.put("outputPath", fileExportUrl);
			dataResponse.setValueReponse(respValue);
			return dataResponse;
		} catch (ParsePropertyException | InvalidFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			dataResponse.setResponseMsg("Failed to upload file.");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
			if (os != null) {
				os.close();
			}
		}

	}

	@Override
	public DataResponse reportPromotionPage(String promotionCode, LocalDate fromDate, LocalDate toDate,
			PageRequest pageRequest) {
		DataResponse dataResponse = new DataResponse();
		try {
			Pageable page;
			Sort sort;
			if (pageRequest.getSortBy() != null && pageRequest.getOrderBy() != null) {
				if (pageRequest.getSortBy().toUpperCase().equals("ASC")) {
					sort = Sort.by(pageRequest.getOrderBy()).ascending();
				} else {
					sort = Sort.by(pageRequest.getOrderBy()).descending();
				}
				page = org.springframework.data.domain.PageRequest.of(pageRequest.getPage(),
						pageRequest.getItemPerPage(), sort);
			} else {
				page = org.springframework.data.domain.PageRequest.of(pageRequest.getPage(),
						pageRequest.getItemPerPage());
			}
			
			List<ReportDTO> dtos = new ArrayList<ReportDTO>();
			List<PromotionLine> lstLine = promotionLineRepository.findPromotionLineForReport(promotionCode, fromDate, toDate);
			for (PromotionLine line : lstLine) {
				ReportDTO data = new ReportDTO();
				data.setPromotionCode(line.getCode());
				data.setPromotionName(line.getLineName());
				data.setPromotionFDate(line.getFromDate());
				data.setPromotionTDate(line.getToDate());
				Long totalDiscount = Long.valueOf(0);
				Long tiketPrice = Long.valueOf(0);
				Long value = Long.valueOf(0);
				List<Invoice> lstInvoice = invoiceRepository.findInvoiceByPromotionReport(line.getId());
				for (Invoice invoice : lstInvoice) {
					totalDiscount += invoice.getTotalDiscount().longValue();
					tiketPrice += invoice.getTotalTiketPrice() != null ? invoice.getTotalTiketPrice().longValue(): 0L ;
					value += invoice.getTotal().longValue();
				}
				data.setDiscount(totalDiscount);
				dtos.add(data);
			}
			Page<ReportDTO> reportDTOPage = new PageImpl<>(dtos, page, dtos.size());
			dataResponse.setResponseMsg("Get data report success !!!");
			dataResponse.setRespType(Constant.HTTP_SUCCESS);
			Map<String, Object> respValue = new HashMap<>();
			respValue.put("data",reportDTOPage);
			dataResponse.setValueReponse(respValue);
			return dataResponse;

		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@Override
	public DataResponse reportSaleByUser(String userCode, LocalDate fromDate, LocalDate toDate) throws IOException {
		DataResponse dataResponse = new DataResponse();
		Workbook resultWorkbook = null;
		InputStream inputStream = null;
		OutputStream os = null;
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
//			classLoader.getResource(null)
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			String strFromDate = fromDate.format(formatter);
			String strToDate = toDate.format(formatter);
			String templateName = Constant.RESOURCE_TEMPLATE_PATH + Constant.CUSTOMER_SALE_TEMPLATE_REPORT;
			Map<String, Object> beans = new HashMap<String, Object>();
			beans.put("fromDate", strFromDate);
			beans.put("toDate", strToDate);
			List<ReportDTO> dtos = new ArrayList<ReportDTO>();
			List<User> users = userRepository.findUserByReport(userCode);
			for (User user : users) {
				ReportDTO data = new ReportDTO();
				data.setCusCode(user.getUserCode());
				data.setCusName(user.getFullName());
				data.setAddress(user.getAddressDescription());
				if(user.getRegeionDetail() != null ) {
					data.setDistrict(user.getRegeionDetail().getFullName());
					data.setProvince(user.getRegeionDetail().getRegionParent().getFullName());
				}
				List<Invoice> invoices = invoiceRepository.findInvoiceByUserReport(user.getUserId(),fromDate,toDate);
				if(invoices == null || invoices.size() ==0) {
					
				}else {
					Long value = 0L;
					Long tiketPrice = 0L;
					Long discount = 0L;
					for(Invoice invoice : invoices) {
						value += invoice.getTotal().longValue();
						tiketPrice += invoice.getTotalTiketPrice() != null ? invoice.getTotalTiketPrice().longValue(): 0L ;
						discount += invoice.getTotalDiscount()!= null ? invoice.getTotalDiscount().longValue() : 0L ;
					}
					data.setRevenue(value);
					data.setDiscount(discount);
					data.setTicketPrice(tiketPrice);
					dtos.add(data);
				}
			}
			beans.put("lstData", dtos);
			Long total = 0L;
			for (ReportDTO dto : dtos) {
				total += dto.getRevenue();
			}
			beans.put("total", total);
			inputStream = getClass().getClassLoader().getResourceAsStream(templateName);
			XLSTransformer transformer = new XLSTransformer();
			resultWorkbook = transformer.transformXLS(inputStream, beans);

			String timestamp = String.valueOf(Instant.now().toEpochMilli());

			String fileName = Constant.SALE_BY_CUSTOMER + "_" + timestamp + Constant.EXCEL_EXTENSION;
			String reportPath = Constant.EXPORT_REPORT_PATH;
			// Tạo thư mục nếu chưa tồn tại
			File directory = new File(reportPath);
			if (!directory.exists()) {
				directory.mkdirs();
			}
			os = new BufferedOutputStream(new FileOutputStream(reportPath + fileName));

			resultWorkbook.write(os);
			os.flush();
			dataResponse.setResponseMsg("Export report success !!!");
			dataResponse.setRespType(Constant.HTTP_SUCCESS);
			Map<String, Object> respValue = new HashMap<>();
			String fileExportUrl = fileName;
			fileExportUrl = fileExportUrl.replaceAll("\\\\", "/");
			respValue.put("outputPath", fileExportUrl);
			dataResponse.setValueReponse(respValue);
			return dataResponse;
		} catch (ParsePropertyException | InvalidFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			dataResponse.setResponseMsg("Failed to upload file.");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
			if (os != null) {
				os.close();
			}
		}

	}

	@Override
	public DataResponse reportSaleByUserPage(String userCode, LocalDate fromDate, LocalDate toDate,
			PageRequest pageRequest) {
		DataResponse dataResponse = new DataResponse();
		try {
			Pageable page;
			Sort sort;
			if (pageRequest.getSortBy() != null && pageRequest.getOrderBy() != null) {
				if (pageRequest.getSortBy().toUpperCase().equals("ASC")) {
					sort = Sort.by(pageRequest.getOrderBy()).ascending();
				} else {
					sort = Sort.by(pageRequest.getOrderBy()).descending();
				}
				page = org.springframework.data.domain.PageRequest.of(pageRequest.getPage(),
						pageRequest.getItemPerPage(), sort);
			} else {
				page = org.springframework.data.domain.PageRequest.of(pageRequest.getPage(),
						pageRequest.getItemPerPage());
			}
			
			List<ReportDTO> dtos = new ArrayList<ReportDTO>();
			List<User> users = userRepository.findUserByReport(userCode);
			for (User user : users) {
				ReportDTO data = new ReportDTO();
				data.setCusCode(user.getUserCode());
				data.setCusName(user.getFullName());
				data.setAddress(user.getAddressDescription());
				if(user.getRegeionDetail() != null) {
					data.setDistrict(user.getRegeionDetail().getFullName());
					data.setProvince(user.getRegeionDetail().getRegionParent().getFullName());
				}
				List<Invoice> invoices = invoiceRepository.findInvoiceByUserReport(user.getUserId(),fromDate,toDate);
				if(invoices == null || invoices.size() ==0) {
				}else {
					Long value = 0L;
					Long tiketPrice = 0L;
					Long discount = 0L;
					for(Invoice invoice : invoices) {
						value += invoice.getTotal().longValue();
						tiketPrice += invoice.getTotalTiketPrice() != null ? invoice.getTotalTiketPrice().longValue(): 0L ;
						discount += invoice.getTotalDiscount()!= null ? invoice.getTotalDiscount().longValue() : 0L ;
					}
					data.setRevenue(value);
					data.setDiscount(discount);
					data.setTicketPrice(tiketPrice);
					dtos.add(data);
				}
			}
			Page<ReportDTO> reportDTOPage = new PageImpl<>(dtos, page, dtos.size());
			dataResponse.setResponseMsg("Get data report success !!!");
			dataResponse.setRespType(Constant.HTTP_SUCCESS);
			Map<String, Object> respValue = new HashMap<>();
			respValue.put("data",reportDTOPage);
			dataResponse.setValueReponse(respValue);
			return dataResponse;

		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public DataResponse reportInvoiceReturn(LocalDate fromDate, LocalDate toDate) throws IOException {
		DataResponse dataResponse = new DataResponse();
		Workbook resultWorkbook = null;
		InputStream inputStream = null;
		OutputStream os = null;
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		try {

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			String strFromDate = fromDate.format(formatter);
			String strToDate = toDate.format(formatter);
			String templateName = Constant.RESOURCE_TEMPLATE_PATH + Constant.REPORT_INVOICE_RETURN_TEMPLATE;
			Map<String, Object> beans = new HashMap<String, Object>();
			beans.put("fromDate", strFromDate);
			beans.put("toDate", strToDate);
			List<ReportDTO> dtos = new ArrayList<ReportDTO>();
			List<Invoice> lstInvoiceReturn = invoiceRepository.findInvoiceReturnForReport(fromDate,toDate);
			for (Invoice invoice : lstInvoiceReturn) {
				ReportDTO data = new ReportDTO();
				User user = userRepository.getById(invoice.getUserId());
				if(user != null) {
					data.setCusCode(user.getUserCode());
					data.setCusName(user.getFullName());
				}
				data.setInvoiceCode(invoice.getCode());
				data.setInvoiceCreatedDate(invoice.getCreatedDate());
				data.setInvoiceReturnCode("RT_"+invoice.getCode());
				data.setInvoiceReturnDate(invoice.getLastModifiedDate());
				data.setReason(invoice.getReason());
				data.setDiscount(invoice.getTotalDiscount() != null ? invoice.getTotalDiscount().longValue() : 0L);
				dtos.add(data);
			}
			beans.put("lstData", dtos);
			Long total = 0L;
			for (ReportDTO dto : dtos) {
				total += dto.getDiscount();
			}
			beans.put("total", total);
			inputStream = getClass().getClassLoader().getResourceAsStream(templateName);
			XLSTransformer transformer = new XLSTransformer();
			resultWorkbook = transformer.transformXLS(inputStream, beans);

			String timestamp = String.valueOf(Instant.now().toEpochMilli());

			String fileName = Constant.REPORT_INVOICE_RETURN + "_" + timestamp + Constant.EXCEL_EXTENSION;
			String reportPath = Constant.EXPORT_REPORT_PATH;
			// Tạo thư mục nếu chưa tồn tại
			File directory = new File(reportPath);
			if (!directory.exists()) {
				directory.mkdirs();
			}
			os = new BufferedOutputStream(new FileOutputStream(reportPath + fileName));

			resultWorkbook.write(os);
			os.flush();
			dataResponse.setResponseMsg("Export report success !!!");
			dataResponse.setRespType(Constant.HTTP_SUCCESS);
			Map<String, Object> respValue = new HashMap<>();
			String fileExportUrl =fileName;
			fileExportUrl = fileExportUrl.replaceAll("\\\\", "/");
			respValue.put("outputPath", fileExportUrl);
			dataResponse.setValueReponse(respValue);
			return dataResponse;
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@Override
	public DataResponse reportInvoiceReturnPage(LocalDate fromDate, LocalDate toDate, PageRequest pageRequest) {
		DataResponse dataResponse = new DataResponse();
		try {
			Pageable page;
			Sort sort;
			if (pageRequest.getSortBy() != null && pageRequest.getOrderBy() != null) {
				if (pageRequest.getSortBy().toUpperCase().equals("ASC")) {
					sort = Sort.by(pageRequest.getOrderBy()).ascending();
				} else {
					sort = Sort.by(pageRequest.getOrderBy()).descending();
				}
				page = org.springframework.data.domain.PageRequest.of(pageRequest.getPage(),
						pageRequest.getItemPerPage(), sort);
			} else {
				page = org.springframework.data.domain.PageRequest.of(pageRequest.getPage(),
						pageRequest.getItemPerPage());
			}
			
			List<ReportDTO> dtos = new ArrayList<ReportDTO>();
			List<Invoice> lstInvoiceReturn = invoiceRepository.findInvoiceReturnForReport(fromDate,toDate);
			for (Invoice invoice : lstInvoiceReturn) {
				ReportDTO data = new ReportDTO();
				User user = userRepository.getById(invoice.getUserId());
				if(user != null) {
					data.setCusCode(user.getUserCode());
					data.setCusName(user.getFullName());
				}
				data.setInvoiceCode(invoice.getCode());
				data.setInvoiceCreatedDate(invoice.getCreatedDate());
				data.setInvoiceReturnCode("RT_"+invoice.getCode());
				data.setInvoiceReturnDate(invoice.getLastModifiedDate());
				data.setReason(invoice.getReason());
				data.setDiscount(invoice.getTotalDiscount() != null ? invoice.getTotalDiscount().longValue() : 0L);
				dtos.add(data);
			}
			
			Page<ReportDTO> reportDTOPage = new PageImpl<>(dtos, page, dtos.size());
			dataResponse.setResponseMsg("Get data report success !!!");
			dataResponse.setRespType(Constant.HTTP_SUCCESS);
			Map<String, Object> respValue = new HashMap<>();
			respValue.put("data",reportDTOPage);
			dataResponse.setValueReponse(respValue);
			return dataResponse;
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@Override
	public DataResponse salesReportByRoute(String routeCode, LocalDate fromDate, LocalDate toDate) throws IOException {
		DataResponse dataResponse = new DataResponse();
		Workbook resultWorkbook = null;
		InputStream inputStream = null;
		OutputStream os = null;
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			String strFromDate = fromDate.format(formatter);
			String strToDate = toDate.format(formatter);
			String templateName = Constant.RESOURCE_TEMPLATE_PATH + Constant.REPORT_SALE_BY_ROUTE;
			Map<String, Object> beans = new HashMap<String, Object>();
			beans.put("fromDate", strFromDate);
			beans.put("toDate", strToDate);
			List<Route> routes = routeRepository.findRouteActiveForReport(routeCode);
			List<ReportDTO> dtos = new ArrayList<ReportDTO>();
			for(Route route: routes) {
				List<Invoice> lstInvoice = invoiceRepository.findInvoiceForReportByRoute(route.getId(),fromDate,toDate);
				if(lstInvoice != null && !lstInvoice.isEmpty()) {
					ReportDTO data = new ReportDTO();
					data.setRouteCode(route.getCode());
					data.setRegionFrom(route.getFrom().getFullName());
					data.setRegionTo(route.getTo().getFullName());
					Long value = 0L;
					Long tiketPrice = 0L;
					Long discount = 0L;
					for(Invoice invoice : lstInvoice) {
						value += invoice.getTotal().longValue();
						tiketPrice += invoice.getTotalTiketPrice() != null ? invoice.getTotalTiketPrice().longValue(): 0L ;
						discount += invoice.getTotalDiscount()!= null ? invoice.getTotalDiscount().longValue() : 0L ;
					}
					data.setRevenue(value);
					data.setDiscount(discount);
					data.setTicketPrice(tiketPrice);
					dtos.add(data);
				}
			}
			beans.put("lstData", dtos);
			Long total = 0L;
			for (ReportDTO dto : dtos) {
				total += dto.getRevenue();
			}
			beans.put("total", total);
			inputStream = getClass().getClassLoader().getResourceAsStream(templateName);
			XLSTransformer transformer = new XLSTransformer();
			resultWorkbook = transformer.transformXLS(inputStream, beans);

			String timestamp = String.valueOf(Instant.now().toEpochMilli());

			String fileName = Constant.REPORT_SALE_BY_ROUTE_NAME + "_" + timestamp + Constant.EXCEL_EXTENSION;
			String reportPath = Constant.EXPORT_REPORT_PATH;
			// Tạo thư mục nếu chưa tồn tại
			File directory = new File(reportPath);
			if (!directory.exists()) {
				directory.mkdirs();
			}
			os = new BufferedOutputStream(new FileOutputStream(reportPath + fileName));

			resultWorkbook.write(os);
			os.flush();
			dataResponse.setResponseMsg("Export report success !!!");
			dataResponse.setRespType(Constant.HTTP_SUCCESS);
			Map<String, Object> respValue = new HashMap<>();
			String fileExportUrl =fileName;
			fileExportUrl = fileExportUrl.replaceAll("\\\\", "/");
			respValue.put("outputPath", fileExportUrl);
			dataResponse.setValueReponse(respValue);
			return dataResponse;
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@Override
	public DataResponse salesReportByRoutePage(String routeCode,LocalDate fromDate, LocalDate toDate, PageRequest pageRequest) {

		DataResponse dataResponse = new DataResponse();
		try {
			Pageable page;
			Sort sort;
			if(pageRequest.getSortBy() != null && pageRequest.getOrderBy() != null) {
				if(pageRequest.getSortBy().toUpperCase().equals("ASC")) {
					sort = Sort.by(pageRequest.getOrderBy()).ascending();
				}else {
					sort = Sort.by(pageRequest.getOrderBy()).descending();
				}
				page= org.springframework.data.domain.PageRequest.of(pageRequest.getPage(), pageRequest.getItemPerPage(), sort);
			}else {
				page = org.springframework.data.domain.PageRequest.of(pageRequest.getPage(), pageRequest.getItemPerPage());
			}
			List<ReportDTO> dtos = new ArrayList<ReportDTO>();
			List<Route> routes = routeRepository.findRouteActiveForReport(routeCode);
			for(Route route: routes) {
				List<Invoice> lstInvoice = invoiceRepository.findInvoiceForReportByRoute(route.getId(),fromDate,toDate);
				if(lstInvoice != null && !lstInvoice.isEmpty()) {
					ReportDTO data = new ReportDTO();
					data.setRouteCode(route.getCode());
					data.setRegionFrom(route.getFrom().getFullName());
					data.setRegionTo(route.getTo().getFullName());
					Long value = 0L;
					Long tiketPrice = 0L;
					Long discount = 0L;
					for(Invoice invoice : lstInvoice) {
						value += invoice.getTotal().longValue();
						tiketPrice += invoice.getTotalTiketPrice() != null ? invoice.getTotalTiketPrice().longValue(): 0L ;
						discount += invoice.getTotalDiscount()!= null ? invoice.getTotalDiscount().longValue() : 0L ;
					}
					data.setRevenue(value);
					data.setDiscount(discount);
					data.setTicketPrice(tiketPrice);
					dtos.add(data);
				}
			}
			Page<ReportDTO> reportDTOPage = new PageImpl<>(dtos, page, dtos.size());
			dataResponse.setResponseMsg("Get data report success !!!");
			dataResponse.setRespType(Constant.HTTP_SUCCESS);
			Map<String, Object> respValue = new HashMap<>();
			respValue.put("data",reportDTOPage);
			dataResponse.setValueReponse(respValue);
			return dataResponse;
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	
	}
	
}
