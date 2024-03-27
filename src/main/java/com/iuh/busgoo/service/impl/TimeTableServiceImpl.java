package com.iuh.busgoo.service.impl;

import com.iuh.busgoo.constant.Constant;
import com.iuh.busgoo.dto.BustripDTO;
import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.dto.SeatOrderDTO;
import com.iuh.busgoo.dto.TimeTableDTO;
import com.iuh.busgoo.entity.*;
import com.iuh.busgoo.filter.BustripFilter;
import com.iuh.busgoo.filter.TimeTableFilter;
import com.iuh.busgoo.mapper.SeatOrderMapper;
import com.iuh.busgoo.repository.*;
import com.iuh.busgoo.requestType.TimeTableCreateRequest;
import com.iuh.busgoo.service.TimeTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	@Autowired
	private SeatOrderMapper seatOrderMapper;
	
//	@Autowired
//	private BusTripDTORepository busTripDTORepository;

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
						&& !(timeTableCreateRequest.getStartTime().isBefore(tmp.getStartedTime()))) {
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
			List<Object[]> resultList  = timeTableRepository.findBusTripByFilter(filter.getFromId(),filter.getToId(),filter.getTimeStarted(),currDate);
			List<BustripDTO> lstData = new ArrayList<>();
			 for (Object[] result : resultList) {
		            Long timeTableId = (Long) result[0];
		            Timestamp timestamp = (Timestamp) result[1];
		            LocalDateTime timeStated = timestamp.toLocalDateTime();
//		            LocalDateTime timeStated = LocalDateTime.parse(strTimeStated, null);
		            Long priceDetailId = (Long) result[2];
		            Double priceValue = (Double) result[3];
		            Long routeId = (Long) result[4];
				 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
//		            LocalTime transferTime = (LocalTime) result[5];
		            Time tpTransferTime =  (Time) result[5];
		            LocalTime transferTime = tpTransferTime.toLocalTime();
		            
		            String fromName = (String) result[6];
		            String toName = (String) result[7];
		            String typeBusName = (String) result[8];

					 String[] parts = transferTime.toString().split(":");
					 int hours = Integer.parseInt(parts[0]);
					 int minutes = Integer.parseInt(parts[1]);


					 // Tính thời gian kết thúc bằng cách cộng thời gian chạy vào thời gian bắt đầu
				 	LocalDateTime endTime = timeStated.plusHours(hours).plusMinutes(minutes);
		            BustripDTO busTripDTO = new BustripDTO();
		            busTripDTO.setTimeTableId(timeTableId);
		            busTripDTO.setTimeStated(timeStated);
		            busTripDTO.setPriceDetailId(priceDetailId);
		            busTripDTO.setPriceValue(priceValue);
		            busTripDTO.setRouteId(routeId);
		            busTripDTO.setTransferTime(transferTime);
		            busTripDTO.setFromName(fromName);
				 	busTripDTO.setEndTime(endTime);
		            busTripDTO.setToName(toName);
		            busTripDTO.setTypeBusName(typeBusName);
		            lstData.add(busTripDTO);
		        }
			
			for(BustripDTO data : lstData) {
				List<SeatOrder> seatOrders = seatOrderRepository.findByTimeTableId(data.getTimeTableId());
				Long count = seatOrderRepository.countByIsAvailableAndTimeTableId(false,data.getTimeTableId());
				if(seatOrders != null && (seatOrders.size() > 0 || count > 0)) {
					List<SeatOrderDTO> dtos = seatOrderMapper.toDto(seatOrders);
					data.setSeatOrder(dtos);
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
