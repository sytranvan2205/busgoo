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
import com.iuh.busgoo.entity.Promotion;
import com.iuh.busgoo.entity.PromotionLine;
import com.iuh.busgoo.filter.PromotionFilter;
import com.iuh.busgoo.repository.PromotionLineRepository;
import com.iuh.busgoo.repository.PromotionRepository;
import com.iuh.busgoo.requestType.PromotionCreateRequest;
import com.iuh.busgoo.service.PromotionService;

@Service
public class PromotionServiceImpl implements PromotionService {

	@Autowired
	private PromotionRepository promotionRepo;
	
	@Autowired
	private PromotionLineRepository lineRepo;

	@Override
	public DataResponse getAllPromotionByFilter(PromotionFilter filter) {
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
			if(promotionCreateRequest.getFromDate().isBefore(currentDate)) {
				dataResponse.setResponseMsg("From date must be greater than or equal to the current date");
				dataResponse.setRespType(Constant.FROM_DATE_BEFORE_CURR_DATE);
				return dataResponse;
			}
			if(promotionCreateRequest.getFromDate().isAfter(promotionCreateRequest.getToDate())) {
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
			if(promotionId == null) {
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
			if(promotionId == null) {
				throw new Exception();
			}
			Promotion checkExist = promotionRepo.findByIdAndStatus(promotionId, 1);
			if(checkExist != null) {
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

}
