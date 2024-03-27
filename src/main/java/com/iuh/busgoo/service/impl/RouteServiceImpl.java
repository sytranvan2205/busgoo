package com.iuh.busgoo.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.iuh.busgoo.constant.Constant;
import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.dto.RouteDTO;
import com.iuh.busgoo.entity.RegionDetail;
import com.iuh.busgoo.entity.Route;
import com.iuh.busgoo.filter.RouteFilter;
import com.iuh.busgoo.repository.RegionDetailRepository;
import com.iuh.busgoo.repository.RouteRepository;
import com.iuh.busgoo.requestType.RouteCreateRequest;
import com.iuh.busgoo.service.RouteService;

@Service
public class RouteServiceImpl implements RouteService{
	
	@Autowired
	RouteRepository routeRepository;
	
	@Autowired
	private RegionDetailRepository regionDetailRepository;

	@Override
	public DataResponse getAllRoute() {
		DataResponse dataResponse = new DataResponse();
		try {
			dataResponse.setResponseMsg("get routes success !!!");
			dataResponse.setRespType(Constant.HTTP_SUCCESS);
			List<Route> routes = routeRepository.findByStatus(1);
			Map<String, Object> respValue = new HashMap<>();
			respValue.put("data", routes);
			dataResponse.setValueReponse(respValue);
			return dataResponse;
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@Override
	public DataResponse getRouteByAddress(String fromCode, String toCode) {
		DataResponse dataResponse = new DataResponse();
		try {
			dataResponse.setResponseMsg("get routes success !!!");
			dataResponse.setRespType(Constant.HTTP_SUCCESS);
			List<Route> routes = routeRepository.findByFromDetailCodeAndToDetailCodeAndStatus(fromCode, toCode,1);
			Map<String, Object> respValue = new HashMap<>();
			respValue.put("data", routes);
			dataResponse.setValueReponse(respValue);
			return dataResponse;
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@Override
	public DataResponse createRoute(RouteCreateRequest routeCreateRequest) {
		DataResponse dataResponse = new DataResponse();
		try {
			if(routeCreateRequest.getFromId() == null) {
				dataResponse.setResponseMsg("Departure point must not be empty.");
				dataResponse.setRespType(Constant.FROM_ADDRESS_IS_NOT_NULL);
				return dataResponse;
			}
			if(routeCreateRequest.getFromId() == null) {
				dataResponse.setResponseMsg("Destination must not be empty.");
				dataResponse.setRespType(Constant.TO_ADDRESS_IS_NOT_NULL);
				return dataResponse;
			}
			if(routeCreateRequest.getTransferTime() == null) {
				dataResponse.setResponseMsg("Destination must not be empty.");
				dataResponse.setRespType(Constant.TRANSFER_TIME_IS_NOT_NULL);
				return dataResponse;
			}
			RegionDetail from = regionDetailRepository.findById(routeCreateRequest.getFromId()).get();
			RegionDetail to = regionDetailRepository.findById(routeCreateRequest.getToId()).get();
			if(from == null || to == null) {
				throw new Exception();
			}
			//create new route
			List<Route> list = routeRepository.findByFromDetailCodeAndToDetailCodeAndStatus(from.getDetailCode(), to.getDetailCode(), 1);
			if(list != null && list.size()>0) {
				dataResponse.setResponseMsg("The route already exists.");
				dataResponse.setRespType(Constant.ROUTE_HAS_EXIST);
				return dataResponse;
			}else {
				Route route = new Route();
				Long countRoute = routeRepository.count();
				route.setCode("R"+(countRoute+1));
				route.setFrom(from);
				route.setTo(to);
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
				LocalTime transferTime = LocalTime.parse(routeCreateRequest.getTransferTime(), formatter);
				route.setTransferTime(transferTime);
				route.setStatus(1);
				routeRepository.save(route);
				dataResponse.setResponseMsg("Create success !!!");
				dataResponse.setRespType(Constant.HTTP_SUCCESS);
				Map<String, Object> respValue = new HashMap<>();
				respValue.put("data", route);
				dataResponse.setValueReponse(respValue);
				return dataResponse;
			}
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@Override
	public DataResponse deleteRoute(Long routeId) {
		DataResponse dataResponse = new DataResponse();
		try {
			Optional<Route> checkExist = routeRepository.findById(routeId);
			Route route = checkExist.get();
			if(route != null) {
				route.setStatus(0);
				routeRepository.save(route);
			}
			dataResponse.setResponseMsg("Delete route success !!!");
			dataResponse.setRespType(Constant.HTTP_SUCCESS);
			return dataResponse;	
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@Override
	public DataResponse getBusTripByAddressAndTime(String fromCode, String toCode, LocalDate timeStated) {
		DataResponse dataResponse = new DataResponse();
		try {
			if(fromCode != null && fromCode.length() == 0) {
				dataResponse.setResponseMsg("Departure point must not be empty.");
				dataResponse.setRespType(Constant.FROM_ADDRESS_IS_NOT_NULL);
				return dataResponse;
			}
			if(toCode!= null && toCode.length() == 0) {
				dataResponse.setResponseMsg("Destination must not be empty.");
				dataResponse.setRespType(Constant.TO_ADDRESS_IS_NOT_NULL);
				return dataResponse;
			}
			if(timeStated == null) {
				dataResponse.setResponseMsg("Time stated is not Null");
				dataResponse.setRespType(Constant.TIME_STATED_IS_NOT_NULL);
				return dataResponse;
			}
			List<RouteDTO> dtos = routeRepository.getRouteByAddressAndTime(fromCode, toCode, timeStated);
			Map<String, Object> respValue = new HashMap<>();
			respValue.put("data", dtos);
			dataResponse.setValueReponse(respValue);
			dataResponse.setResponseMsg("Get bustrip success!!!");
			dataResponse.setRespType(Constant.HTTP_SUCCESS);
			return dataResponse;
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@Override
	public DataResponse findAll() {
		DataResponse dataResponse = new DataResponse();
		try {
			List<Route> routes = routeRepository.findByStatus(1);
			Map<String, Object> respValue = new HashMap<>();
			respValue.put("data", routes);
			dataResponse.setValueReponse(respValue);
			dataResponse.setResponseMsg("Get bustrip success!!!");
			dataResponse.setRespType(Constant.HTTP_SUCCESS);
			return dataResponse;
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@Override
	public DataResponse getRouteById(Long routeId) {
		DataResponse dataResponse = new DataResponse();
		try {
			Route route = routeRepository.findById(routeId).get();
			Map<String, Object> respValue = new HashMap<>();
			respValue.put("data", route);
			dataResponse.setValueReponse(respValue);
			dataResponse.setResponseMsg("Get bustrip success!!!");
			dataResponse.setRespType(Constant.HTTP_SUCCESS);
			return dataResponse;
		} catch (Exception e) {
			e.printStackTrace();
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@Override
	public DataResponse findRouteByFilter(RouteFilter filter) {
		DataResponse dataResponse = new DataResponse();
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
			Page<Route> pageRoute;
			pageRoute = routeRepository.findRouteByFilter(filter.getStatus(),filter.getFromId(),filter.getToId(),page);
			dataResponse.setResponseMsg("Get routes success!!!");
			dataResponse.setRespType(Constant.HTTP_SUCCESS);
//			List<Price> prices = priceRepository.findAll();
			Map<String, Object> respValue = new HashMap<>();
			respValue.put("data", pageRoute);
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
