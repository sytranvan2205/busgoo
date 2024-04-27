package com.iuh.busgoo.service;

import java.io.IOException;
import java.time.LocalDate;

import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.requestType.PageRequest;

public interface ReportService {

	DataResponse salesByBusExport(LocalDate fromDate, LocalDate toDate)  throws IOException ;

	DataResponse getDataForDashBoard();

	DataResponse salesByBusExportPage(LocalDate fromDate, LocalDate toDate, PageRequest pageRequest);

}
