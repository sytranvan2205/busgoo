package com.iuh.busgoo.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
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
import com.iuh.busgoo.dto.PromotionDTO;
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
	
	@Autowired
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
				LocalDate currDate = LocalDate.now();
				Promotion checkExist = promotionRepo.findById(promotionLineRq.getPromotionId()).get();
				if(checkExist == null) {
					dataResponse.setResponseMsg("Promotion not exist");
					dataResponse.setRespType(Constant.PROMOTION_IS_NOT_EXIST);
					return dataResponse;
				}else {
					if(checkExist.getFromDate().isBefore(currDate)) {
						dataResponse.setResponseMsg("Can't update promotion is active");
						dataResponse.setRespType(Constant.PROMOTION_UPDATE_FAILED);
						return dataResponse;
					}
					PromotionLine line = null;
					if(promotionLineRq.getPromotionLineId() != null) {
						Optional<PromotionLine> lineTmp = lineRepo.findById(promotionLineRq.getPromotionLineId());
						if(lineTmp != null)
							line = lineTmp.get();
					}
					if(line != null) {
						if(!line.getPromotion().equals(checkExist)) {
							throw new Exception();
						}
					}else {
						line = new PromotionLine();
					}
					line.setLineName(promotionLineRq.getLineName());
					if(line.getCode() == null) {
						line.setCode(promotionLineRq.getLineCode());
					}
					line.setPromotionType(promotionLineRq.getPromotionType());
					if(promotionLineRq.getFromDate().isBefore(checkExist.getFromDate())) {
						dataResponse.setResponseMsg("From date of the promotion line must be greater than the from date of the promotion !!!");
						dataResponse.setRespType(Constant.PROMOTION_LINE_FROM_DATE_INVALID);
						return dataResponse;
					}else {
						line.setFromDate(promotionLineRq.getFromDate());
					}
					if(promotionLineRq.getToDate().isAfter(checkExist.getToDate())) {
						dataResponse.setResponseMsg("TO date of the promotion line must be less than the to date of the promotion !!!");
						dataResponse.setRespType(Constant.PROMOTION_LINE_TO_DATE_INVALID);
						return dataResponse;
					}else if(promotionLineRq.getFromDate().isAfter(promotionLineRq.getToDate())) {
						dataResponse.setResponseMsg("From date must be less than or equal to to date");
						dataResponse.setRespType(Constant.FROM_DATE_AFTER__TO_DATE);
						return dataResponse;
					}else {
						line.setToDate(promotionLineRq.getToDate());
					}
					line.setPromotion(checkExist);
					line.setStatus(1);
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
			PromotionLine checkExist = lineRepo.findByIdAndStatus(promotionLineId, 1);
			if (checkExist != null) {
				checkExist.setStatus(0);
				lineRepo.save(checkExist);
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
				LocalDate currDate = LocalDate.now();
				PromotionLine line = lineRepo.findById(promotionDetailRequest.getPromotionLineId()).get();
				if(line == null) {
					throw new Exception();
				}else {
					Boolean isCreate = false;
					if(!line.getPromotion().getFromDate().isAfter(currDate)) {
						dataResponse.setResponseMsg("Can't update promotion is active");
						dataResponse.setRespType(Constant.PROMOTION_UPDATE_FAILED);
						return dataResponse;
					}
					PromotionDetail checkExist = null;
					if(promotionDetailRequest.getPromotionLineId() != null) {
						checkExist = promoDetailRepository.findByPromotionLineIdAndStatus(promotionDetailRequest.getPromotionLineId(),1);
					}
					if(checkExist != null) {
						dataResponse.setResponseMsg("You cannot have two promotional structures at the same time");
						dataResponse.setRespType(Constant.PROMOTION_DETAIL_ALREADY_EXIST);
						return dataResponse;
					}
					PromotionDetail promotionDetail = promoDetailRepository.findByPromotionLineIdAndStatus(line.getId(), 1);
					if(promotionDetail == null) {
						promotionDetail = new PromotionDetail();
						isCreate = true;
					}
					promotionDetail.setPromotionLine(line);
					if(isCreate) {
						promotionDetail.setCode(promotionDetailRequest.getDetailCode());
					}
					promotionDetail.setConditionApply(new BigDecimal(promotionDetailRequest.getConditionApply()));
					if(line.getPromotionType().equals(2)) {
						promotionDetail.setMaxDiscount(new BigDecimal(promotionDetailRequest.getMaxDiscount()));
						if(promotionDetailRequest.getDiscount()<0 && promotionDetailRequest.getDiscount()>100) {
							dataResponse.setResponseMsg("The discount must be >0 and <100");
							dataResponse.setRespType(Constant.DISCOUNT_INVALID);
							return dataResponse;
						}
					}
					promotionDetail.setDiscount(new BigDecimal(promotionDetailRequest.getDiscount()));
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

	@Override
	public DataResponse findDetailByLineId(Long promotionLineId) {
		DataResponse dataResponse = new DataResponse();
		try {
			if(promotionLineId == null) {
				throw new Exception();
			}else {
				PromotionDetail promotionDetail = promoDetailRepository.findByPromotionLineIdAndStatus(promotionLineId,1);
				dataResponse.setResponseMsg("Get data success!!!");
				dataResponse.setRespType(Constant.HTTP_SUCCESS);
				Map<String, Object> respValue = new HashMap<>();
				respValue.put("data", promotionDetail);
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
	public DataResponse findById(Long promotionId) {
		DataResponse dataResponse = new DataResponse();
		try {
			if(promotionId == null) {
				throw new Exception();
			}else {
				Promotion promotion = promotionRepo.findById(promotionId).get();
				dataResponse.setResponseMsg("Get data success!!!");
				dataResponse.setRespType(Constant.HTTP_SUCCESS);
				Map<String, Object> respValue = new HashMap<>();
				respValue.put("data", promotion);
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
	public DataResponse getCurrentPromotion(LocalDate currentDate) {
		DataResponse dataResponse = new DataResponse();
		try {
			List<PromotionDTO> list = promoDetailRepository.findByCurrentDate(currentDate);
			dataResponse.setResponseMsg("Get data success!!!");
			dataResponse.setRespType(Constant.HTTP_SUCCESS);
			Map<String, Object> respValue = new HashMap<>();
			respValue.put("data", list);
			dataResponse.setValueReponse(respValue);
			return dataResponse;
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@Override
	public DataResponse getPromotionByPrice(Double priceValue) {
		DataResponse dataResponse = new DataResponse();
		LocalDate currDate = LocalDate.now();
		try {
			//apply promotion
			Double discountPrice = Double.valueOf(0);
			//Tìm các promotion lines với các detail thoải điều kiện
			List<PromotionLine> lines = lineRepo.findPromotionLineByCondition(currDate,new BigDecimal(priceValue));
			List<PromotionDTO> promotionDTOs = null ;
			for(PromotionLine line: lines) {
				PromotionDTO promotionDTO = new PromotionDTO();
				PromotionDetail detail = promoDetailRepository.findByPromotionLineIdAndStatus(line.getId(), 1);
				Double discountValue = Double.valueOf(0);
				if (line.getPromotionType().equals(1)) {
					discountValue = detail.getDiscount().doubleValue();
				}else if(line.getPromotionType().equals(2)) {
					Double discountValueTmp = detail.getDiscount().doubleValue()*priceValue;
					discountValue = (discountValueTmp<= detail.getMaxDiscount().doubleValue())? discountValue: detail.getMaxDiscount().doubleValue();
				}
				if(discountPrice < discountValue) {
					promotionDTOs = new ArrayList<PromotionDTO>();
					discountPrice = discountValue;
					promotionDTO.setPromotionCode(line.getPromotion().getCode());
					promotionDTO.setPromotionLineName(line.getLineName());
					promotionDTO.setPromotionType(line.getPromotionType());
					promotionDTO.setDiscount(new BigDecimal(discountValue));
					promotionDTO.setMaxDiscount(detail.getMaxDiscount());;
					promotionDTO.setConditionApply(detail.getConditionApply());;
					promotionDTOs.add(promotionDTO);
				}
			}
			dataResponse.setResponseMsg("Get data success!!!");
			dataResponse.setRespType(Constant.HTTP_SUCCESS);
			Map<String, Object> respValue = new HashMap<>();
			if (promotionDTOs!= null && promotionDTOs.size()>0) {
				respValue.put("data", promotionDTOs.get(0));
			}
//			respValue.put("data", list);
			dataResponse.setValueReponse(respValue);
			return dataResponse;
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

}
