package com.iuh.busgoo.service.impl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iuh.busgoo.constant.Constant;
import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.entity.Price;
import com.iuh.busgoo.entity.PriceDetail;
import com.iuh.busgoo.entity.Route;
import com.iuh.busgoo.entity.TypeBus;
import com.iuh.busgoo.repository.PriceDetailRepository;
import com.iuh.busgoo.repository.PriceRepository;
import com.iuh.busgoo.repository.RouteRepository;
import com.iuh.busgoo.repository.TypeBusRepository;
import com.iuh.busgoo.requestType.PriceCreateRequest;
import com.iuh.busgoo.requestType.PriceDetailRequest;
import com.iuh.busgoo.service.PriceService;

import jakarta.transaction.Transactional;

@Service
public class PriceServiceImpl implements PriceService {

	@Autowired
	private RouteRepository routeRepository;

	@Autowired
	private TypeBusRepository typeBusRepository;

	@Autowired
	private PriceRepository priceRepository;
	
	@Autowired
	private PriceDetailRepository priceDetailRepository;

	@Override
	@Transactional
	public DataResponse createPrice(PriceCreateRequest priceCreateRequest) {
		DataResponse response = new DataResponse();
		LocalDate curDate = LocalDate.now();
		try {
//			if (priceCreateRequest.getTypeBusCode() == null || priceCreateRequest.getTypeBusCode().length() == 0) {
//				response.setResponseMsg("Type bus is not null");
//				response.setRespType(Constant.TYPE_BUS_CODE_NOT_NULL);
//				return response;
//			}
//			if (priceCreateRequest.getRouteCode() == null || priceCreateRequest.getRouteCode().length() == 0) {
//				response.setResponseMsg("Route code is not null");
//				response.setRespType(Constant.ROUTE_CODE_NOT_NULL);
//				return response;
//			}
			if (priceCreateRequest.getFromDate() == null) {
				response.setResponseMsg("From date is not null");
				response.setRespType(Constant.FROM_DATE_NOT_NULL);
				return response;
			}
			if(priceCreateRequest.getToDate()!=null && !priceCreateRequest.getToDate().isAfter(curDate)) {
				throw new Exception();
			}
			if(priceCreateRequest.getFromDate().isAfter(priceCreateRequest.getToDate())) {
				throw new Exception();
			}
//			Route route = routeRepository.findByCode(priceCreateRequest.getRouteCode());
//			if (route == null) {
//				response.setResponseMsg("Route is not exist");
//				response.setRespType(Constant.ROUTE_NOT_EXIST);
//				return response;
//			}
//			TypeBus typeBus = typeBusRepository.findByCode(priceCreateRequest.getTypeBusCode());
//			if (typeBus == null) {
//				response.setResponseMsg("Type bus is not exist");
//				response.setRespType(Constant.TYPE_BUS_NOT_EXIST);
//				return response;
//			}
			List<Price> prices = priceRepository.getLstByFromDateAndToDate(priceCreateRequest.getFromDate(),priceCreateRequest.getToDate());
			if (prices == null) {
				//create price
				Price newPrice = new Price();
				Long countPrice = priceRepository.count();
				newPrice.setCode("P" + countPrice);
				newPrice.setStatus(1);
				newPrice.setDescription((priceCreateRequest.getPriceDescription() == null) ? null
						: priceCreateRequest.getPriceDescription());
				newPrice.setToDate(priceCreateRequest.getToDate());
				newPrice.setFromDate(priceCreateRequest.getFromDate());
//				newPrice.setRoute(route);
//				newPrice.setTypeBus(typeBus);
				Price priceSave = priceRepository.save(newPrice);
				
				//create price detail
//				PriceDetail priceDetail = new PriceDetail();
//				Long countDetail = priceDetailRepository.count();
//				priceDetail.setDetailCode("PD"+ countDetail);
//				priceDetail.setValue(priceCreateRequest.getPriceValue());
//				priceDetail.setStatus(1);
//				priceDetail.setPrice(priceSave);
//				priceDetailRepository.save(priceDetail);
				
				//trả về data
				response.setResponseMsg("Price create success !!!");
				response.setRespType(Constant.HTTP_SUCCESS);
				Map<String, Object> reponseValue = new HashMap<>();
				reponseValue.put("price", priceSave);
				response.setValueReponse(reponseValue);
				return response;
			}else {
				//check validate
				Integer checkErrorInsert = 0;
				for(Price price: prices) {
					if(price.getToDate().isAfter(priceCreateRequest.getFromDate())) {
						checkErrorInsert = 1;
					}
				}
				if(checkErrorInsert == 1) {
					response.setResponseMsg("Price is exist");
					response.setRespType(Constant.PRICE_IS_EXIST);
					return response;
				}else {
					//create price
					Price newPrice = new Price();
					Long countPrice = priceRepository.count();
					newPrice.setCode("P" + countPrice);
					newPrice.setStatus(1);
					newPrice.setDescription((priceCreateRequest.getPriceDescription() == null) ? null
							: priceCreateRequest.getPriceDescription());
					newPrice.setToDate(priceCreateRequest.getToDate());
					newPrice.setFromDate(priceCreateRequest.getFromDate());
//					newPrice.setRoute(route);
//					newPrice.setTypeBus(typeBus);
					Price priceSave = priceRepository.save(newPrice);
					
					//create price detail
//					PriceDetail priceDetail = new PriceDetail();
//					Long countDetail = priceDetailRepository.count();
//					priceDetail.setDetailCode("PD"+ countDetail);
//					priceDetail.setValue(priceCreateRequest.getPriceValue());
//					priceDetail.setStatus(1);
//					priceDetail.setPrice(priceSave);
//					priceDetailRepository.save(priceDetail);
					
					//trả về data
					response.setResponseMsg("Price create success !!!");
					response.setRespType(Constant.HTTP_SUCCESS);
					Map<String, Object> reponseValue = new HashMap<>();
					reponseValue.put("price", priceSave);
					response.setValueReponse(reponseValue);
					return response;
				}
			}
		} catch (Exception e) {
			response.setResponseMsg("System error");
			response.setRespType(Constant.SYSTEM_ERROR_CODE);
			return response;
		}
	}

	@Override
	public DataResponse getAllPrice() {
		DataResponse response = new DataResponse();
		try {
			response.setResponseMsg("Get prices success!!!");
			response.setRespType(Constant.HTTP_SUCCESS);
			List<Price> prices = priceRepository.findAll();
			Map<String, Object> respValue = new HashMap<>();
			respValue.put("prices", prices);
			return response;
		} catch (Exception e) {
			response.setResponseMsg("System error");
			response.setRespType(Constant.SYSTEM_ERROR_CODE);
			return response;
		}
	}

	@Override
	public DataResponse getPriceById(Long id) {
		DataResponse response = new DataResponse();
		try {
			Price price = priceRepository.getById(id);
			if(price == null) {
				response.setResponseMsg("Price is not exist");
				response.setRespType(Constant.PRICE_IS_NOT_EXIST);
				return response;
			}else {
				response.setResponseMsg("Get price success!!!");
				response.setRespType(Constant.HTTP_SUCCESS);
				Map<String, Object> respValue = new HashMap<>();
				respValue.put("price", price);
				response.setValueReponse(respValue);
				return response;
			}
		} catch (Exception e) {
			response.setResponseMsg("System error");
			response.setRespType(Constant.SYSTEM_ERROR_CODE);
			return response;
		}
	}

	@Override
	public DataResponse deletePrice(Long id) {
		DataResponse response = new DataResponse();
		try {
			Price price = priceRepository.getById(id);
			if(price == null) {
				response.setResponseMsg("Price is not exist");
				response.setRespType(Constant.PRICE_IS_NOT_EXIST);
				return response;
			}else {
				price.setStatus(0);
				priceRepository.save(price);
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
	public DataResponse createPriceDetail(PriceDetailRequest request) {
		DataResponse response = new DataResponse();
		try {
			if(request.getPriceId() == null) {
				throw new Exception();
			}
			if (request.getTypeBusCode() == null || request.getTypeBusCode().length() == 0) {
				response.setResponseMsg("Type bus is not null");
				response.setRespType(Constant.TYPE_BUS_CODE_NOT_NULL);
				return response;
			}
			if (request.getRouteCode() == null || request.getRouteCode().length() == 0) {
				response.setResponseMsg("Route code is not null");
				response.setRespType(Constant.ROUTE_CODE_NOT_NULL);
				return response;
			}
			Route route = routeRepository.findByCode(request.getRouteCode());
			if (route == null) {
				response.setResponseMsg("Route is not exist");
				response.setRespType(Constant.ROUTE_NOT_EXIST);
				return response;
			}
			TypeBus typeBus = typeBusRepository.findByCode(request.getTypeBusCode());
			if (typeBus == null) {
				response.setResponseMsg("Type bus is not exist");
				response.setRespType(Constant.TYPE_BUS_NOT_EXIST);
				return response;
			}
			Price price = priceRepository.getById(request.getPriceId());
			if(price == null) {
				response.setResponseMsg("Price is not exist");
				response.setRespType(Constant.PRICE_IS_NOT_EXIST);
				return response;
			}else {
				List<PriceDetail> checkExsit = priceDetailRepository.findByRouteCodeAndTypeBusCode(request.getRouteCode(), request.getTypeBusCode());
				if(checkExsit != null) {
					response.setResponseMsg("PriceDetail is exist");
					response.setRespType(Constant.PRICE_DETAIL_IS_EXIST);
					return response;
				}
				PriceDetail priceDetail = new PriceDetail();
				Long countDetail = priceDetailRepository.count();
				priceDetail.setDetailCode("PD"+ countDetail);
				priceDetail.setPrice(price);
				priceDetail.setRoute(route);
				priceDetail.setTypeBus(typeBus);
				priceDetail.setValue(request.getPriceValue());
				priceDetailRepository.save(priceDetail);
				//trả về data
				response.setResponseMsg("PriceDetail create success!!!");
				response.setRespType(Constant.HTTP_SUCCESS);
				Map<String, Object> reponseValue = new HashMap<>();
				reponseValue.put("priceDetail", priceDetail);
				response.setValueReponse(reponseValue);
				return response;
			}
		} catch (Exception e) {
			response.setResponseMsg("System error");
			response.setRespType(Constant.SYSTEM_ERROR_CODE);
			return response;
		}
	}

	@Override
	public DataResponse getPriceDetailByPriceId(Long priceId) {
		DataResponse response = new DataResponse();
		try {
			if(priceId == null) {
				throw new Exception();
			}else {
				List<PriceDetail> lstPriceDetail = priceDetailRepository.findByPriceId(priceId);
				//trả về data
				response.setResponseMsg("Get list PriceDetail success!!!");
				response.setRespType(Constant.HTTP_SUCCESS);
				Map<String, Object> reponseValue = new HashMap<>();
				reponseValue.put("lstPriceDetail", lstPriceDetail);
				response.setValueReponse(reponseValue);
				return response;
			}
		} catch (Exception e) {
			response.setResponseMsg("System error");
			response.setRespType(Constant.SYSTEM_ERROR_CODE);
			return response;
		}
	}
	
	

}