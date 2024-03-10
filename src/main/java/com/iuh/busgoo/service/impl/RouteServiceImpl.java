package com.iuh.busgoo.service.impl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iuh.busgoo.constant.Constant;
import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.dto.RouteDTO;
import com.iuh.busgoo.entity.RegionDetail;
import com.iuh.busgoo.entity.Route;
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
			List<Route> routes = routeRepository.findAll();
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
	public DataResponse createRote(RouteCreateRequest routeCreateRequest) {
		DataResponse dataResponse = new DataResponse();
		try {
			if(routeCreateRequest.getFromCode() == null) {
				dataResponse.setResponseMsg("Departure point must not be empty.");
				dataResponse.setRespType(Constant.FROM_ADDRESS_IS_NOT_NULL);
				return dataResponse;
			}
			if(routeCreateRequest.getFromCode() == null) {
				dataResponse.setResponseMsg("Destination must not be empty.");
				dataResponse.setRespType(Constant.TO_ADDRESS_IS_NOT_NULL);
				return dataResponse;
			}
			if(routeCreateRequest.getTransferTime() == null) {
				dataResponse.setResponseMsg("Destination must not be empty.");
				dataResponse.setRespType(Constant.TRANSFER_TIME_IS_NOT_NULL);
				return dataResponse;
			}
			//create new route
			List<Route> list = routeRepository.findByFromDetailCodeAndToDetailCodeAndStatus(routeCreateRequest.getFromCode(), routeCreateRequest.getFromCode(), 1);
			if(list != null) {
				dataResponse.setResponseMsg("The route already exists.");
				dataResponse.setRespType(Constant.ROUTE_HAS_EXIST);
				return dataResponse;
			}else {
				Route route = new Route();
				Long countRoute = routeRepository.count();
				route.setCode("R"+countRoute);
				RegionDetail from = regionDetailRepository.findRegionDetailByDetailCode(routeCreateRequest.getFromCode());
				route.setFrom(from);
				RegionDetail to = regionDetailRepository.findRegionDetailByDetailCode(routeCreateRequest.getToCode());
				route.setTo(to);
				route.setTransferTime(routeCreateRequest.getTransferTime());
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

}
