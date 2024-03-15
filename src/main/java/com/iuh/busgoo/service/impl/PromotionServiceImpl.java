package com.iuh.busgoo.service.impl;

import java.math.BigDecimal;
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
import com.iuh.busgoo.entity.Promotion;
import com.iuh.busgoo.entity.PromotionDetail;
import com.iuh.busgoo.entity.PromotionLine;
import com.iuh.busgoo.filter.PromotionFilter;
import com.iuh.busgoo.repository.PromotionDetailRepository;
import com.iuh.busgoo.repository.PromotionLineRepository;
import com.iuh.busgoo.repository.PromotionRepository;
import com.iuh.busgoo.requestType.PromotionCreateRequest;
import com.iuh.busgoo.requestType.PromotionDetailRequest;
import com.iuh.busgoo.requestType.PromotionLineRq;
import com.iuh.busgoo.requestType.PromotionUpdateRequest;
import com.iuh.busgoo.service.PromotionService;

@Service
public class PromotionServiceImpl implements PromotionService {

	@Autowired
	private PromotionRepository promotionRepo;

	@Autowired
	private PromotionLineRepository lineRepo;
	
	private PromotionDetailRepository promoDetailRepository;

	@Override
	public DataResponse getAllPromotionByFilter(PromotionFilter filter) {
		DataResponse dataResponse = new DataResponse();
		try {
			Sort sort;
			Pageable page;
			if (filter.getSortBy() != null && filter.getOrderBy() != null) {
				if (filter.getSortBy().toUpperCase().equals("ASC")) {
					sort = Sort.by(filter.getOrderBy()).ascending();
				} else {
					sort = Sort.by(filter.getOrderBy()).descending();
				}
				page = PageRequest.of(filter.getPage(), filter.getItemPerPage(), sort);
			} else {
				page = PageRequest.of(filter.getPage(), filter.getItemPerPage());
			}

			Page<Promotion> pagePromotion = promotionRepo.findByStatusAndCodeAndFromDateAndToDate(filter.getStatus(),
					filter.getQ(), filter.getFromDate(), filter.getToDate(), page);
			dataResponse.setResponseMsg("Get prices success!!!");
			dataResponse.setRespType(Constant.HTTP_SUCCESS);
			Map<String, Object> respValue = new HashMap<>();
			respValue.put("data", pagePromotion);
			dataResponse.setValueReponse(respValue);
			return dataResponse;

		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@Override
	public DataResponse createPromotion(PromotionCreateRequest promotionCreateRequest) {
		DataResponse dataResponse = new DataResponse();
		try {
			if (promotionCreateRequest.getCode() == null || promotionCreateRequest.getFromDate() == null
					|| promotionCreateRequest.getToDate() == null || promotionCreateRequest.getName() == null) {
				throw new Exception();
			}
			LocalDate currentDate = LocalDate.now();
			if (promotionCreateRequest.getFromDate().isBefore(currentDate)) {
				dataResponse.setResponseMsg("From date must be greater than or equal to the current date");
				dataResponse.setRespType(Constant.FROM_DATE_BEFORE_CURR_DATE);
				return dataResponse;
			}
			if (promotionCreateRequest.getFromDate().isAfter(promotionCreateRequest.getToDate())) {
				dataResponse.setResponseMsg("To date must be greater than or equal to the from date");
				dataResponse.setRespType(Constant.FROM_DATE_AFTER__TO_DATE);
				return dataResponse;
			}
			Promotion checkExist = promotionRepo.findByCodeAndStatus(promotionCreateRequest.getCode(), 1);
			if (checkExist != null) {
				dataResponse.setResponseMsg("Promotion already exist");
				dataResponse.setRespType(Constant.PROMOTION_IS_EXIST);
				return dataResponse;
			}
			Promotion promotion = new Promotion();
			promotion.setCode(promotionCreateRequest.getCode());
			promotion.setFromDate(promotionCreateRequest.getFromDate());
			promotion.setToDate(promotionCreateRequest.getToDate());
			promotion.setDescription(promotionCreateRequest.getDescription());
			promotion.setStatus(1);
			promotion.setName(promotionCreateRequest.getName());
			promotionRepo.save(promotion);
			dataResponse.setResponseMsg("Get promotion success!!!");
			dataResponse.setRespType(Constant.HTTP_SUCCESS);
			Map<String, Object> respValue = new HashMap<>();
			respValue.put("data", promotion);
			dataResponse.setValueReponse(respValue);
			return dataResponse;
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@Override
	public DataResponse getPromotionLine(Long promotionId) {
		DataResponse dataResponse = new DataResponse();
		try {
			if (promotionId == null) {
				throw new Exception();
			}
			dataResponse.setResponseMsg("Get promotion line success!!!");
			dataResponse.setRespType(Constant.HTTP_SUCCESS);
			List<PromotionLine> promotionLines = lineRepo.findByPromotionId(promotionId);
			Map<String, Object> respValue = new HashMap<>();
			respValue.put("data", promotionLines);
			dataResponse.setValueReponse(respValue);
			return dataResponse;
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@Override
	public DataResponse deletePromotion(Long promotionId) {
		DataResponse dataResponse = new DataResponse();
		try {
			if (promotionId == null) {
				throw new Exception();
			}
			Promotion checkExist = promotionRepo.findByIdAndStatus(promotionId, 1);
			if (checkExist != null) {
				checkExist.setStatus(0);
			}
			promotionRepo.save(checkExist);
			dataResponse.setResponseMsg("Delete success !!!");
			dataResponse.setRespType(Constant.HTTP_SUCCESS);
			return dataResponse;
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@Override
	public DataResponse createPromotionLine(PromotionLineRq promotionLineRq) {
		DataResponse dataResponse = new DataResponse();
		try {
			if(promotionLineRq.getFromDate() == null || promotionLineRq.getToDate() == null || promotionLineRq.getLineCode() == null || promotionLineRq.getLineName() == null || promotionLineRq.getPromotionType() == null) {
				throw new Exception();
			}
			if(promotionLineRq.getPromotionId() == null) {
				throw new Exception();
			}else {
				Promotion checkExist = promotionRepo.findById(promotionLineRq.getPromotionId()).get();
				if(checkExist == null) {
					dataResponse.setResponseMsg("Promotion not exist");
					dataResponse.setRespType(Constant.PROMOTION_IS_NOT_EXIST);
					return dataResponse;
				}else {
					PromotionLine line = new PromotionLine();
					line.setLineName(promotionLineRq.getLineName());
					line.setCode(promotionLineRq.getLineCode());
					line.setPromotionType(promotionLineRq.getPromotionType());
					line.setFromDate(promotionLineRq.getFromDate());
					line.setToDate(promotionLineRq.getToDate());
					line.setPromotion(checkExist);
					lineRepo.save(line);
					dataResponse.setResponseMsg("Create success !!!");
					dataResponse.setRespType(Constant.HTTP_SUCCESS);
					Map<String, Object> respValue = new HashMap<>();
					respValue.put("data", line);
					dataResponse.setValueReponse(respValue);
					return dataResponse;
				}
			}
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@Override
	public DataResponse deletePromotionLine(Long promotionLineId) {
		DataResponse dataResponse = new DataResponse();
		try {
			if (promotionLineId == null) {
				throw new Exception();
			}
			Promotion checkExist = lineRepo.findByIdAndStatus(promotionLineId, 1);
			if (checkExist != null) {
				checkExist.setStatus(0);
				promotionRepo.save(checkExist);
			}
			dataResponse.setResponseMsg("Delete success !!!");
			dataResponse.setRespType(Constant.HTTP_SUCCESS);
			return dataResponse;
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@Override
	public DataResponse findAllDetail() {
		DataResponse dataResponse = new DataResponse();
		try {
			dataResponse.setResponseMsg("Get promotion detail success!!!");
			dataResponse.setRespType(Constant.HTTP_SUCCESS);
			List<PromotionDetail> promotionDetail = promoDetailRepository.findByStatus(1);
			Map<String, Object> respValue = new HashMap<>();
			respValue.put("data", promotionDetail);
			dataResponse.setValueReponse(respValue);
			return dataResponse;
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@Override
	public DataResponse createPromotionDetail(PromotionDetailRequest promotionDetailRequest) {
		DataResponse dataResponse = new DataResponse();
		try {
			if(promotionDetailRequest.getPromotionLineId()==null) {
				throw new Exception();
			}else {
				PromotionLine line = lineRepo.findById(promotionDetailRequest.getPromotionLineId()).get();
				if(line == null) {
					throw new Exception();
				}else {
					PromotionDetail checkExist = promoDetailRepository.findByPromotionLineId(promotionDetailRequest.getPromotionLineId());
					if(checkExist != null) {
						dataResponse.setResponseMsg("You cannot have two promotional structures at the same time");
						dataResponse.setRespType(Constant.PROMOTION_DETAIL_ALREADY_EXIST);
						return dataResponse;
					}
					PromotionDetail promotionDetail = new PromotionDetail();
					promotionDetail.setPromotionLine(line);
					promotionDetail.setCode(promotionDetailRequest.getDetailCode());
					promotionDetail.setConditionApply(new BigDecimal(promotionDetailRequest.getConditionApply()));
					promotionDetail.setDiscount(new BigDecimal(promotionDetailRequest.getDiscount()));
					if(line.getPromotionType().equals(2)) {
						promotionDetail.setMaxDiscount(new BigDecimal(promotionDetailRequest.getMaxDiscount()));
					}
					promotionDetail.setStatus(1);
					promoDetailRepository.save(promotionDetail);
					dataResponse.setResponseMsg("Create success!!!");
					dataResponse.setRespType(Constant.HTTP_SUCCESS);
					Map<String, Object> respValue = new HashMap<>();
					respValue.put("data", promotionDetail);
					dataResponse.setValueReponse(respValue);
					return dataResponse;
				}
			}
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@Override
	public DataResponse deletePromotionDetail(Long promotionDetailId) {DataResponse dataResponse = new DataResponse();
	try {
		if (promotionDetailId == null) {
			throw new Exception();
		}
		PromotionDetail checkExist = promoDetailRepository.findByIdAndStatus(promotionDetailId, 1);
		if (checkExist != null) {
			checkExist.setStatus(0);
			promoDetailRepository.save(checkExist);
		}
		dataResponse.setResponseMsg("Delete success !!!");
		dataResponse.setRespType(Constant.HTTP_SUCCESS);
		return dataResponse;
	} catch (Exception e) {
		dataResponse.setResponseMsg("System error");
		dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
		return dataResponse;
	}}

	@Override
	public DataResponse updatePromotion(PromotionUpdateRequest promotionUpdateRequest) {
		DataResponse dataResponse = new DataResponse();
		try {
			if(promotionUpdateRequest.getFromDate().isAfter(promotionUpdateRequest.getToDate())) {
				dataResponse.setResponseMsg("From date must be less than or equal to to date");
				dataResponse.setRespType(Constant.FROM_DATE_AFTER__TO_DATE);
				return dataResponse;
			}
			LocalDate currDate = LocalDate.now();
			Promotion promotion = promotionRepo.findById(promotionUpdateRequest.getPromotionId()).get();
			if(promotion == null) {
				throw new Exception();
			}else {
				promotion.setName(promotionUpdateRequest.getName());
				promotion.setDescription(promotionUpdateRequest.getDescription());
				if(promotion.getFromDate().isBefore(currDate)) {
					dataResponse.setResponseMsg("Can't update promotion is active");
					dataResponse.setRespType(Constant.PROMOTION_UPDATE_FAILED);
					return dataResponse;
				}else {
					if(promotionUpdateRequest.getFromDate().isAfter(currDate)) {
						promotionUpdateRequest.setFromDate(promotionUpdateRequest.getFromDate());
						promotionUpdateRequest.setToDate(promotionUpdateRequest.getToDate());
					}else {
						dataResponse.setResponseMsg("From date must be greater than the current date");
						dataResponse.setRespType(Constant.FROM_DATE_BEFORE_CURR_DATE);
						return dataResponse;
					}
					promotionRepo.save(promotion);
				}
			}
			dataResponse.setResponseMsg("Update success!!!");
			dataResponse.setRespType(Constant.HTTP_SUCCESS);
			Map<String, Object> respValue = new HashMap<>();
			respValue.put("data", promotion);
			dataResponse.setValueReponse(respValue);
			return dataResponse;
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

}
