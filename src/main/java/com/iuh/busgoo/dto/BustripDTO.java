package com.iuh.busgoo.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BustripDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long timeTableId;
	private	LocalDateTime timeStated;
	private Long priceDetailId;
	private Double priceValue;
	private Long countEmptySeat;
	private Long routeId;
	private LocalTime transferTime;
	private String fromName;
	private String toName;
	private String typeBusName;

	private Boolean expanded;
	private LocalDateTime endTime;
	private List<SeatOrderDTO> seatOrder = new ArrayList<SeatOrderDTO>();
	
	public BustripDTO() {
		super();
	}

	public BustripDTO(Long timeTableId, LocalDateTime timeStated, Long priceDetailId, Double priceValue, Long routeId,
			LocalTime transferTime, String fromName, String toName) {
		super();
		this.timeTableId = timeTableId;
		this.timeStated = timeStated;
		this.priceDetailId = priceDetailId;
		this.priceValue = priceValue;
		this.routeId = routeId;
		this.transferTime = transferTime;
		this.fromName = fromName;
		this.toName = toName;
	}
	
	
	
}
