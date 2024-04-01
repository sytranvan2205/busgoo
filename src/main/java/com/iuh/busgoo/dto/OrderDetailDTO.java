package com.iuh.busgoo.dto;

import java.io.Serializable;

import com.iuh.busgoo.entity.Order;
import com.iuh.busgoo.entity.SeatOrder;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class OrderDetailDTO implements Serializable{
	private static final long serialVersionUID = 1L;

    private Long orderDetailId;

    @Column(name = "code", length = 255)
    private String code;

    private Long seatId;
    
    private String seatName;

    private Double price;	

    private Long orderId;
}
