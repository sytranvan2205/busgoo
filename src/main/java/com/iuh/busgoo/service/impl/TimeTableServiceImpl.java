package com.iuh.busgoo.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iuh.busgoo.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iuh.busgoo.constant.Constant;
import com.iuh.busgoo.dto.BustripDTO;
import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.dto.TimeTableDTO;
import com.iuh.busgoo.filter.BustripFilter;
import com.iuh.busgoo.filter.TimeTableFilter;
import com.iuh.busgoo.repository.BusRepository;
import com.iuh.busgoo.repository.RouteRepository;
import com.iuh.busgoo.repository.SeatOrderRepository;
import com.iuh.busgoo.repository.SeatRepository;
import com.iuh.busgoo.repository.TimeTableRepository;
import com.iuh.busgoo.requestType.TimeTableCreateRequest;
import com.iuh.busgoo.service.TimeTableService;

@Service
public class TimeTableServiceImpl implements TimeTableService {

	@Autowired
	private BusRepository busRepository;

	@Autowired
	private RouteRepository routeRepository;

	@Autowired
	private TimeTableRepository timeTableRepository;
	
	@Autowired
	private SeatRepository seatRepository;
	
	@Autowired
	private SeatOrderRepository seatOrderRepository;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public DataResponse createTimeTable(TimeTableCreateRequest timeTableCreateRequest) {
		DataResponse dataResponse = new DataResponse();
		try {
			LocalDateTime currDate = LocalDateTime.now();
			if (timeTableCreateRequest.getBusId() == null || timeTableCreateRequest.getRouteId() == null
					|| timeTableCreateRequest.getStartTime() == null) {
				throw new Exception();
			}
			Bus bus = busRepository.findById(timeTableCreateRequest.getBusId()).get();
			if (bus == null) {
				throw new Exception();
			}
			Route route = routeRepository.findById(timeTableCreateRequest.getRouteId()).get();
			if (route == null) {
				throw new Exception();
			}
			if (!timeTableCreateRequest.getStartTime().isAfter(currDate)) {
				dataResponse.setResponseMsg("The departure time must be after the current date. !!!");
				dataResponse.setRespType(Constant.TIME_START_INVALID);
				return dataResponse;
			}
			List<TimeTable> checkExist = timeTableRepository.findByRouteIdAndBusIdAndStatus(route.getId(), bus.getId(),
					1);
			Boolean isError = false;
			for (TimeTable tmp : checkExist) {
				long hours = tmp.getRoute().getTransferTime().getHour();
				long minutes = tmp.getRoute().getTransferTime().getMinute();
				long seconds = tmp.getRoute().getTransferTime().getSecond();
				LocalDateTime timeFinish = tmp.getStartedTime();
				timeFinish = timeFinish.plusHours(hours);
				timeFinish = timeFinish.plusMinutes(minutes);
				timeFinish = timeFinish.plusSeconds(seconds);
				if (timeTableCreateRequest.getStartTime().isBefore(timeFinish)
						&& !timeTableCreateRequest.getStartTime().isBefore(tmp.getStartedTime())) {
					isError = true;
				}
			}
			if (isError) {
				dataResponse.setResponseMsg("The schedule you created is duplicated !!!");
				dataResponse.setRespType(Constant.TIME_START_INVALID);
				return dataResponse;
			}
			TimeTable timeTable = new TimeTable();
			timeTable.setBus(bus);
			timeTable.setRoute(route);
			Long count = timeTableRepository.count();
			timeTable.setCode("TB"+(count+1));
			timeTable.setStartedTime(timeTableCreateRequest.getStartTime());
			timeTable.setStatus(1);
			timeTableRepository.save(timeTable);
			
			List<Seat> seats = seatRepository.findByBusIdAndStatus(bus.getId(),1);
			//create seat order
			for (Seat tmp : seats) {
				SeatOrder seatOrder = new SeatOrder(tmp.getSeatType(),tmp.getSeatName(),timeTable,null,true);
				seatOrderRepository.save(seatOrder);
			}
			dataResponse.setResponseMsg("Create success!!!");
			dataResponse.setRespType(Constant.HTTP_SUCCESS);
			Map<String, Object> resp = new HashMap<String, Object>();
			resp.put("data", timeTable);
			dataResponse.setValueReponse(resp);
			return dataResponse;
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error!!!");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@Override
	public DataResponse findTimeTableByFilter(TimeTableFilter filter) {
		DataResponse response = new DataResponse();
		try {
			Sort sort;
			Pageable page;
			if(filter.getSortBy()!= null && filter.getOrderBy()!= null) {
				if (filter.getSortBy().toUpperCase().equals("ASC")) {
					sort = Sort.by(filter.getOrderBy()).ascending();
				} else {
					sort = Sort.by(filter.getOrderBy()).descending();
				}
				page = PageRequest.of(filter.getPage(), filter.getItemPerPage(), sort);
			}else {
				page = PageRequest.of(filter.getPage(),filter.getItemPerPage());
			}
			Page<TimeTableDTO> data = timeTableRepository.findByFilter(filter.getFromId(),filter.getToId(),filter.getStatus(),filter.getDepartureDate(),page);
			response.setResponseMsg("Get schedule success!!!");
			response.setRespType(Constant.HTTP_SUCCESS);
//			List<Price> prices = priceRepository.findAll();
			Map<String, Object> respValue = new HashMap<>();
			respValue.put("data", data);
			response.setValueReponse(respValue);
			return response;
		} catch (Exception e) {
			response.setResponseMsg("System error");
			response.setRespType(Constant.SYSTEM_ERROR_CODE);
			return response;
		}
	}

	@Override
	public DataResponse deleteTimeTable(Long id) {
		DataResponse response = new DataResponse();
		try {
			TimeTable timeTable = timeTableRepository.findById(id).get();
			if (timeTable == null) {
				response.setResponseMsg("Schedule is not exist");
				response.setRespType(Constant.USER_NOT_EXIST);
				return response;
			} else {
				timeTable.setStatus(0);
				timeTableRepository.save(timeTable);
				response.setResponseMsg("Delete success!!!");
				response.setRespType(Constant.HTTP_SUCCESS);
				return response;
			}
		} catch (Exception e) {
			response.setResponseMsg("System error");
			response.setRespType(Constant.SYSTEM_ERROR_CODE);
			return response;
		}
	}

	@Override
	public DataResponse getBustripByFilter(BustripFilter filter) {
		DataResponse response = new DataResponse();
		try {
			LocalDate currDate = LocalDate.now();
			List<BustripDTO> lstData = timeTableRepository.findBusTripByFilter(filter.getFromId(),filter.getToId(),filter.getTimeStarted(),currDate);
			for(BustripDTO data : lstData) {
				List<SeatOrder> seatOrders = seatOrderRepository.findByTimeTable(data.getTimeTableId());
				Long count = seatOrderRepository.countByIsAvailable(false);
				if(seatOrders != null && (seatOrders.size() > 0 || count > 0)) {
					HashMap<String, Object> seatOrderMap = new HashMap<>();
					seatOrderMap.put("seatOrders", seatOrders);
					data.setSeatOrder(seatOrderMap);
					data.setCountEmptySeat(count);
				}else {
					lstData.remove(data);
				}
			}
			response.setResponseMsg("Get bustrip success!!!");
			response.setRespType(Constant.HTTP_SUCCESS);
//			List<Price> prices = priceRepository.findAll();
			Map<String, Object> respValue = new HashMap<>();
			respValue.put("data", lstData);
			response.setValueReponse(respValue);
			return response;
		} catch (Exception e) {
			response.setResponseMsg("System error");
			response.setRespType(Constant.SYSTEM_ERROR_CODE);
			return response;
		}
	}

}
