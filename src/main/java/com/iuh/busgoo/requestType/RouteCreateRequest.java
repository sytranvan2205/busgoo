package com.iuh.busgoo.requestType;

import java.time.LocalTime;

public class RouteCreateRequest {
	private String fromCode;
	private String toCode;
	private String timeTransfer;
	private LocalTime transferTime;

	public RouteCreateRequest() {
	}

	public String getFromCode() {
		return fromCode;
	}

	public void setFromCode(String fromCode) {
		this.fromCode = fromCode;
	}

	public String getToCode() {
		return toCode;
	}

	public void setToCode(String toCode) {
		this.toCode = toCode;
	}

	public String getTimeTransfer() {
		return timeTransfer;
	}

	public void setTimeTransfer(String timeTransfer) {
		this.timeTransfer = timeTransfer;
	}

	public LocalTime getTransferTime() {
		return transferTime;
	}

	@SuppressWarnings("unused")
	private void setTransferTime(String timeTransfer) {
		this.transferTime = LocalTime.parse(timeTransfer);
		;
	}

}
