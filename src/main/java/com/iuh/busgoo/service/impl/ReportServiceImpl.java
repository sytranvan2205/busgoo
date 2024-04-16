package com.iuh.busgoo.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iuh.busgoo.constant.Constant;
import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.dto.ReportDTO;
import com.iuh.busgoo.entity.Bus;
import com.iuh.busgoo.entity.Invoice;
import com.iuh.busgoo.repository.BusRepository;
import com.iuh.busgoo.repository.InvoiceRepository;
import com.iuh.busgoo.repository.OrderRepository;
import com.iuh.busgoo.repository.UserRepository;
import com.iuh.busgoo.service.ReportService;

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
			String fileName = Constant.RESOURCE_TEMPLATE_PATH + Constant.SALE_TEMPLATE_REPORT;
			Map<String, Object> beans = new HashMap<String, Object>();
			beans.put("fromDate", strFromDate);
			beans.put("toDate", strToDate);
			List<ReportDTO> dtos = new ArrayList<ReportDTO>();
			List<Bus> list = busRepository.findByStatus(1);
			for(Bus bus : list) {
				ReportDTO reportDTO = new ReportDTO();
				reportDTO.setBusName(bus.getName());
				reportDTO.setBusType(bus.getTypeBus().getName());
				List<Invoice> invoices = invoiceRepository.findByBusIdAndFromDateAndToDate(bus.getId(),fromDate,toDate);
				if(invoices == null || invoices.size() ==0) {
					reportDTO.setRevenue(0L);
				}else {
					Long value = 0L;
					for(Invoice invoice : invoices) {
						value += invoice.getTotal().longValue();
					}
					reportDTO.setRevenue(value);
				}
				dtos.add(reportDTO);
			}
			beans.put("lstData", dtos);
			Long total = 0L;
			for(ReportDTO dto :dtos) {
				total += dto.getRevenue();
			}
			beans.put("total", total);
			inputStream = new BufferedInputStream(new FileInputStream(classLoader.getResource(fileName).getFile()));
			XLSTransformer transformer = new XLSTransformer();
			resultWorkbook = transformer.transformXLS(inputStream, beans);
			os = new BufferedOutputStream(new FileOutputStream("C:\\tmp\\test.xlsx"));
			resultWorkbook.write(os);
			os.flush();
			dataResponse.setResponseMsg("Export report success !!!");
			dataResponse.setRespType(Constant.HTTP_SUCCESS);
			Map<String, Object> respValue = new HashMap<>();
			respValue.put("outputPath","C:\\tmp\\test.xlsx");
			dataResponse.setValueReponse(respValue);
			return dataResponse;
		} catch (ParsePropertyException | InvalidFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			dataResponse.setResponseMsg("System error");
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

}
