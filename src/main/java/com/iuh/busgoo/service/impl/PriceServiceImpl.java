package com.iuh.busgoo.service.impl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.iuh.busgoo.constant.Constant;
import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.entity.Price;
import com.iuh.busgoo.entity.PriceDetail;
import com.iuh.busgoo.entity.Route;
import com.iuh.busgoo.entity.TypeBus;
import com.iuh.busgoo.filter.PriceFilter;
import com.iuh.busgoo.repository.PriceDetailRepository;
import com.iuh.busgoo.repository.PriceRepository;
import com.iuh.busgoo.repository.RouteRepository;
import com.iuh.busgoo.repository.TypeBusRepository;
import com.iuh.busgoo.requestType.PriceCreateRequest;
import com.iuh.busgoo.requestType.PriceDetailRequest;
import com.iuh.busgoo.requestType.PriceUpdateRequest;
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
			if (priceCreateRequest.getFromDate() != null && !priceCreateRequest.getFromDate().isAfter(curDate)) {
				response.setResponseMsg("From date must be greater than the current date");
				response.setRespType(Constant.FROM_DATE_BEFORE_CURR_DATE);
				return response;
			}
			if (priceCreateRequest.getFromDate().isAfter(priceCreateRequest.getToDate())) {
				response.setResponseMsg("To date must be greater than or equal to from date.");
				response.setRespType(Constant.FROM_DATE_AFTER__TO_DATE);
				return response;
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
			List<Price> prices = priceRepository.getLstByFromDateAndToDate(priceCreateRequest.getFromDate(),
					priceCreateRequest.getToDate());
			if (prices == null || prices.size() == 0) {
				// create price
				Price newPrice = new Price();
				Long countPrice = priceRepository.count();
				newPrice.setCode("P" + (countPrice+1));
				newPrice.setStatus(1);
				newPrice.setDescription((priceCreateRequest.getPriceDescription() == null) ? null
						: priceCreateRequest.getPriceDescription());
				newPrice.setToDate(priceCreateRequest.getToDate());
				newPrice.setFromDate(priceCreateRequest.getFromDate());
//				newPrice.setRoute(route);
//				newPrice.setTypeBus(typeBus);
				Price priceSave = priceRepository.save(newPrice);

				// create price detail
//				PriceDetail priceDetail = new PriceDetail();
//				Long countDetail = priceDetailRepository.count();
//				priceDetail.setDetailCode("PD"+ countDetail);
//				priceDetail.setValue(priceCreateRequest.getPriceValue());
//				priceDetail.setStatus(1);
//				priceDetail.setPrice(priceSave);
//				priceDetailRepository.save(priceDetail);

				// trả về data
				response.setResponseMsg("Price create success !!!");
				response.setRespType(Constant.HTTP_SUCCESS);
				Map<String, Object> reponseValue = new HashMap<>();
				reponseValue.put("data", priceSave);
				response.setValueReponse(reponseValue);
				return response;
			} else {
				// check validate
				Integer checkErrorInsert = 0;
				for (Price price : prices) {
					if (price.getToDate().isAfter(priceCreateRequest.getFromDate())) {
						checkErrorInsert = 1;
					}
				}
				if (checkErrorInsert == 1) {
					response.setResponseMsg("Price is exist");
					response.setRespType(Constant.PRICE_IS_EXIST);
					return response;
				} else {
					// create price
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

					// create price detail
//					PriceDetail priceDetail = new PriceDetail();
//					Long countDetail = priceDetailRepository.count();
//					priceDetail.setDetailCode("PD"+ countDetail);
//					priceDetail.setValue(priceCreateRequest.getPriceValue());
//					priceDetail.setStatus(1);
//					priceDetail.setPrice(priceSave);
//					priceDetailRepository.save(priceDetail);

					// trả về data
					response.setResponseMsg("Price create success !!!");
					response.setRespType(Constant.HTTP_SUCCESS);
					Map<String, Object> reponseValue = new HashMap<>();
					reponseValue.put("data", priceSave);
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
	public DataResponse getAllPriceByFilter(PriceFilter filter) {
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
			
			Page<Price> pagePrice;
			pagePrice = priceRepository.findByStatusAndFromDateGreaterThanEqualAndToDateLessThanEqual(
					filter.getStatus(), filter.getFromDate(), filter.getToDate(), page);
			response.setResponseMsg("Get prices success!!!");
			response.setRespType(Constant.HTTP_SUCCESS);
//			List<Price> prices = priceRepository.findAll();
			Map<String, Object> respValue = new HashMap<>();
			respValue.put("data", pagePrice);
			response.setValueReponse(respValue);
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
			if (price == null) {
				response.setResponseMsg("Price is not exist");
				response.setRespType(Constant.PRICE_IS_NOT_EXIST);
				return response;
			} else {
				response.setResponseMsg("Get price success!!!");
				response.setRespType(Constant.HTTP_SUCCESS);
				Map<String, Object> respValue = new HashMap<>();
				respValue.put("data", price);
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
			if (price == null) {
				response.setResponseMsg("Price is not exist");
				response.setRespType(Constant.PRICE_IS_NOT_EXIST);
				return response;
			} else {
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
			if (request.getPriceId() == null) {
				throw new Exception();
			}
			if (request.getTypeBusId() == null) {
				response.setResponseMsg("Type bus is not null");
				response.setRespType(Constant.TYPE_BUS_CODE_NOT_NULL);
				return response;
			}
			if (request.getRouteId() == null) {
				response.setResponseMsg("Route code is not null");
				response.setRespType(Constant.ROUTE_CODE_NOT_NULL);
				return response;
			}
			Route route = routeRepository.findById(request.getRouteId()).get();
			if (route == null) {
				response.setResponseMsg("Route is not exist");
				response.setRespType(Constant.ROUTE_NOT_EXIST);
				return response;
			}
			TypeBus typeBus = typeBusRepository.findById(request.getTypeBusId()).get();
			if (typeBus == null) {
				response.setResponseMsg("Type bus is not exist");
				response.setRespType(Constant.TYPE_BUS_NOT_EXIST);
				return response;
			}
			Price price = priceRepository.getById(request.getPriceId());
			if (price == null) {
				response.setResponseMsg("Price is not exist");
				response.setRespType(Constant.PRICE_IS_NOT_EXIST);
				return response;
			} else {
				List<PriceDetail> checkExsit = priceDetailRepository
						.findByRouteIdAndTypeBusIdAndStatusAndPriceId(request.getRouteId(), request.getTypeBusId(),1,price.getId());
				if (checkExsit != null && checkExsit.size()>0) {
					response.setResponseMsg("PriceDetail is exist");
					response.setRespType(Constant.PRICE_DETAIL_IS_EXIST);
					return response;
				}
				PriceDetail priceDetail = new PriceDetail();
				Long countDetail = priceDetailRepository.count();
				priceDetail.setDetailCode("PD" + (countDetail+1));
				priceDetail.setPrice(price);
				priceDetail.setRoute(route);
				priceDetail.setTypeBus(typeBus);
				priceDetail.setStatus(1);
				priceDetail.setValue(request.getPriceValue());
				PriceDetail newPrice = priceDetailRepository.save(priceDetail);
				// trả về data
				response.setResponseMsg("PriceDetail create success!!!");
				response.setRespType(Constant.HTTP_SUCCESS);
				Map<String, Object> reponseValue = new HashMap<>();
//				reponseValue.put("data", newPrice);
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
			if (priceId == null) {
				throw new Exception();
			} else {
				List<PriceDetail> lstPriceDetail = priceDetailRepository.findByPriceIdAndStatus(priceId,1);
				// trả về data
				response.setResponseMsg("Get list PriceDetail success!!!");
				response.setRespType(Constant.HTTP_SUCCESS);
				Map<String, Object> reponseValue = new HashMap<>();
				reponseValue.put("data", lstPriceDetail);
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
	public DataResponse findPriceById(Long priceId) {
		DataResponse dataResponse = new DataResponse();
		try {
			Price price = priceRepository.findById(priceId).get();
			dataResponse.setResponseMsg("Get price success!!!");
			dataResponse.setRespType(Constant.HTTP_SUCCESS);
			Map<String, Object> reponseValue = new HashMap<>();
			reponseValue.put("data", price);
			dataResponse.setValueReponse(reponseValue);
			return dataResponse;
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@Override
	public DataResponse findPriceDetailById(Long priceDetailId) {
		DataResponse dataResponse = new DataResponse();
		try {
			PriceDetail priceDetail = priceDetailRepository.findById(priceDetailId).get();
			dataResponse.setResponseMsg("Get price success!!!");
			dataResponse.setRespType(Constant.HTTP_SUCCESS);
			Map<String, Object> reponseValue = new HashMap<>();
			reponseValue.put("data", priceDetail);
			dataResponse.setValueReponse(reponseValue);
			return dataResponse;
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@Override
	public DataResponse deletePriceDetailById(Long priceDetailId) {
		DataResponse dataResponse = new DataResponse();
		try {
			PriceDetail priceDetail = priceDetailRepository.findById(priceDetailId).get();
			if(priceDetail != null) {
				priceDetail.setStatus(0);
				priceDetailRepository.save(priceDetail);
				dataResponse.setResponseMsg("Delete success!!!");
				dataResponse.setRespType(Constant.HTTP_SUCCESS);
				return dataResponse;
			}else {
				throw new Exception();
			}
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@Override
	public DataResponse updatePrice(PriceUpdateRequest priceUpdateRequest) {
		DataResponse dataResponse = new DataResponse();
		try {
			LocalDate currDate = LocalDate.now();
			Price priceUpdate = priceRepository.findById(priceUpdateRequest.getPriceId()).get();
			if(priceUpdate == null) {
				throw new Exception();
			}else {
				if(priceUpdateRequest.getFromDate().isAfter(priceUpdateRequest.getToDate())) {
					dataResponse.setResponseMsg("From date must be less than or equal to to date");
					dataResponse.setRespType(Constant.FROM_DATE_AFTER__TO_DATE);
					return dataResponse;
				}
				if(!priceUpdate.getFromDate().isBefore(currDate)) {
					priceUpdate.setFromDate(priceUpdateRequest.getFromDate());
				}
				if(!priceUpdate.getToDate().isBefore(currDate)) {
					priceUpdate.setToDate(priceUpdateRequest.getToDate());
				}
				if(priceUpdateRequest.getPriceDescription().trim().length()==0) {
					priceUpdate.setDescription(null);
				}else {
					priceUpdate.setDescription(priceUpdateRequest.getPriceDescription());
				}
				List<Price> prices = priceRepository.getLstByFromDateAndToDate(priceUpdate.getFromDate(),
						priceUpdate.getToDate());
				
				Integer checkErrorUpdate = 0;
				for (Price price : prices) {
					if (price.getToDate().isAfter(priceUpdate.getFromDate())) {
						checkErrorUpdate = 1;
					}
				}
				if (checkErrorUpdate == 1) {
					dataResponse.setResponseMsg("The time range you're updating already has another price");
					dataResponse.setRespType(Constant.PRICE_IS_EXIST);
					return dataResponse;
				}else {
					priceRepository.save(priceUpdate);
					dataResponse.setResponseMsg("Update data success!!!");
					dataResponse.setRespType(Constant.HTTP_SUCCESS);
					Map<String, Object> reponseValue = new HashMap<>();
					reponseValue.put("data", priceUpdate);
					dataResponse.setValueReponse(reponseValue);
					return dataResponse;
				}
			}
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

}
