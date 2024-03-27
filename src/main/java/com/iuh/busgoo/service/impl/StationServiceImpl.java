package com.iuh.busgoo.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iuh.busgoo.constant.Constant;
import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.dto.StationDTO;
import com.iuh.busgoo.entity.RegionDetail;
import com.iuh.busgoo.entity.Station;
import com.iuh.busgoo.mapper.StationMapper;
import com.iuh.busgoo.repository.RegionDetailRepository;
import com.iuh.busgoo.repository.StationRepository;
import com.iuh.busgoo.requestType.StationCreateRequest;
import com.iuh.busgoo.service.StationService;

@Service
public class StationServiceImpl implements StationService{
	
	@Autowired
	private RegionDetailRepository regionDetailRepository;
	
	@Autowired
	private StationRepository stationRepository;
	
	@Autowired
	private StationMapper stationMapper;

	@Override
	public DataResponse createStation(StationCreateRequest stationCreateRequest) {
		DataResponse dataResponse = new DataResponse();
		try {
			if(stationCreateRequest.getRegionDetailId() == null || stationCreateRequest.getStationName()==null ) {
				throw new Exception();
			}else {
				RegionDetail regionDetail = regionDetailRepository.findById(stationCreateRequest.getRegionDetailId()).get();
				if (regionDetail == null) {
					throw new Exception();
				}else {
					Long count = stationRepository.count();
					Station station = new Station();
					station.setName(stationCreateRequest.getStationName());
					station.setRegionDetail(regionDetail);
					station.setCode("ST"+(count+1));
					station.setAddressDescription(stationCreateRequest.getAddressDescription());
					stationRepository.save(station);
					
					dataResponse.setResponseMsg("Create station success !!!");
					dataResponse.setRespType(Constant.HTTP_SUCCESS);
					Map<String, Object> respValue = new HashMap<>();
					respValue.put("data",station);
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
	public DataResponse createStation(Long regionDetailId) {
		DataResponse dataResponse = new DataResponse();
		try {
			List<Station> stations = stationRepository.findByRegionDetailId(regionDetailId);
			dataResponse.setResponseMsg("Get lst station success !!!");
			dataResponse.setRespType(Constant.HTTP_SUCCESS);
			Map<String, Object> respValue = new HashMap<>();
			respValue.put("data",stations);
			dataResponse.setValueReponse(respValue);
			return dataResponse;
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}

	@Override
	public DataResponse getStation(Long regionDetailId) {
		DataResponse dataResponse = new DataResponse();
		try {
			RegionDetail regionDetail = regionDetailRepository.getById(regionDetailId);
			if(regionDetail == null) {
				throw new Exception();
			}
			List<RegionDetail> lstChild = regionDetailRepository.findByRegionParentId(regionDetail.getId());
			List<Long> lstIdChild = new ArrayList<Long>();
			for(RegionDetail tmp : lstChild) {
				lstIdChild.add(tmp.getId());
			}
			List<Station> lstData = stationRepository.findByInRegionDetail(lstIdChild);
			List<StationDTO> lstDTO = stationMapper.toDto(lstData);
			dataResponse.setResponseMsg("Get lst station success !!!");
			dataResponse.setRespType(Constant.HTTP_SUCCESS);
			Map<String, Object> respValue = new HashMap<>();
			respValue.put("data",lstDTO);
			dataResponse.setValueReponse(respValue);
			return dataResponse;
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error");
			dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
			return dataResponse;
		}
	}
	
}
