package com.iuh.busgoo.service;

import com.iuh.busgoo.dto.DataResponse;

public interface RegionDetailService {
	DataResponse getRegionDetailByStructureAndParentId(Long parentID,Long regionStructureId);
}
