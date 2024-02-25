package com.iuh.busgoo.dto;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class DataResponse {
	private String responseMsg;
	private Integer respType;
	private Map<String, Object> valueReponse = new HashMap<>();
}
