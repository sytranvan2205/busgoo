package com.iuh.busgoo.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iuh.busgoo.constant.Constant;
import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.entity.RegionDetail;
import com.iuh.busgoo.repository.RegionDetailRepository;
import com.iuh.busgoo.service.RegionDetailService;

@Service
public class RegionDetailServiceImpl implements RegionDetailService{

	@Autowired
	RegionDetailRepository regionDetailRepository;
	
	@Override
	public DataResponse getRegionDetailByStructureAndParentId(Long parentID, Long regionStructureId) {
		DataResponse dataResponse = new DataResponse();
		try {
			List<RegionDetail> lstData;
			if(regionStructureId == null) {
				dataResponse.setResponseMsg("RegeionStructureId is not null !!!");
				dataResponse.setRespType(Constant.REGEION_STRUCTURE_ID_IS_NOT_NULL);
				return dataResponse;
			}else if(parentID == null){
				lstData = regionDetailRepository.findByStatusAndRegionStructureId(1,regionStructureId);
			}else {
				lstData = regionDetailRepository.findByStatusAndRegionParentIdAndRegionStructureId(1, parentID, regionStructureId);
			}
			dataResponse.setResponseMsg("Get region success !!!");
			dataResponse.setRespType(Constant.HTTP_SUCCESS);
			Map<String, Object> respValue = new HashMap<>();
			respValue.put("data", lstData);
			dataResponse.setValueReponse(respValue);
			return dataResponse;

		} catch (Exception e) {
			dataResponse.setResponseMsg("System error !!!");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

}
