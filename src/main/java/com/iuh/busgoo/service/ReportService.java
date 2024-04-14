package com.iuh.busgoo.service;

import java.io.IOException;
import java.time.LocalDate;

import com.iuh.busgoo.dto.DataResponse;

public interface ReportService {

	DataResponse salesByBusExport(LocalDate fromDate, LocalDate toDate)  throws IOException ;

}
