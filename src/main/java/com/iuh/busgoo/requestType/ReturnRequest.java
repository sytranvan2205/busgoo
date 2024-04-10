package com.iuh.busgoo.requestType;

import lombok.Data;

@Data
public class ReturnRequest {
	private Long invoiceId;
	private String reason;
}
