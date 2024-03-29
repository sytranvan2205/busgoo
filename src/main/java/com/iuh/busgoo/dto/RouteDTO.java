package com.iuh.busgoo.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import com.iuh.busgoo.entity.Seat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String routeCode;
	private String fromAddress;
	private String toAddress;
	private LocalDate toDate;
	private LocalDate transferTime;
	private Long busId;
	private String nameBus;
	private String typeBusName;
	private String typeBusDescription;
	private Double price;
	private List<Seat> lstSeat;
}
