package com.iuh.busgoo.requestType;

import lombok.Data;

@Data
public class RegionDetailRequest {
	private Long parentId;
	private Long regionStructureId;
}
