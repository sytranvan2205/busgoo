package com.iuh.busgoo.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Entity
@Table(name = "seat_order")
@EqualsAndHashCode(callSuper=false)
public class SeatOrder extends AbstractEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column(name="seat_name")
	private String seatName;
	
    @Column(name = "seat_type")
    private String seatType;
    
    @ManyToOne
    @JoinColumn(name = "time_table_id")
    private TimeTable timeTable;
    
    @ManyToOne
    @JoinColumn(name = "order_detail_id")
    private OrderDetail orderDetail;
    
    @Column(name="is_available")
    private Boolean isAvailable;
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSeatName() {
		return seatName;
	}

	public String getSeatType() {
		return seatType;
	}

	public void setSeatType(String seatType) {
		this.seatType = seatType;
	}


	public TimeTable getTimeTable() {
		return timeTable;
	}

	public void setTimeTable(TimeTable timeTable) {
		this.timeTable = timeTable;
	}

	public OrderDetail getOrderDetail() {
		return orderDetail;
	}

	public void setOrderDetail(OrderDetail orderDetail) {
		this.orderDetail = orderDetail;
	}

	public Boolean getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
	
	public SeatOrder(String seatType, String seatName, TimeTable timeTable,
			OrderDetail orderDetail, Boolean isAvailable) {
		super();
		this.seatType = seatType;
		this.timeTable = timeTable;
		this.orderDetail = orderDetail;
		this.isAvailable = isAvailable;
		this.seatName = seatName ;
	}

	public SeatOrder() {
	}




    
}
