package com.iuh.busgoo.service;

import java.io.IOException;
import java.time.LocalDate;

import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.requestType.PageRequest;

public interface ReportService {

	DataResponse salesByBusExport(LocalDate fromDate, LocalDate toDate)  throws IOException ;

	DataResponse getDataForDashBoard();

	DataResponse salesByBusExportPage(LocalDate fromDate, LocalDate toDate, PageRequest pageRequest);

	DataResponse reportPromotion(String promotionCode, LocalDate fromDate, LocalDate toDate) throws IOException;

	DataResponse reportPromotionPage(String promotionCode, LocalDate fromDate, LocalDate toDate,
			PageRequest pageRequest);

	DataResponse reportSaleByUser(String userCode, LocalDate fromDate, LocalDate toDate) throws IOException;

	DataResponse reportSaleByUserPage(String userCode, LocalDate fromDate, LocalDate toDate, PageRequest pageRequest);

}
