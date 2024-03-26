package com.iuh.busgoo.dto;

import lombok.Data;

@Data
public class SeatOrderDTO {

    private Long id;

	private String seatName;
	
    private String seatType;
    
    private Long timeTableId;
    
    private Long orderDetailId;
    
    private Boolean isAvailable;
}
