package com.iuh.busgoo.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;

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
    
    @Column(name = "seat_colunm")
    private String seatColunm;
    
    @Column(name ="seat_row")
    private Long seatRow;
    
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

	public String getSeatColunm() {
		return seatColunm;
	}

	public void setSeatColunm(String seatColunm) {
		this.seatColunm = seatColunm;
	}

	public Long getSeatRow() {
		return seatRow;
	}

	public void setSeatRow(Long seatRow) {
		this.seatRow = seatRow;
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
	
	public SeatOrder(String seatType, String seatColunm, Long seatRow, TimeTable timeTable,
			OrderDetail orderDetail, Boolean isAvailable) {
		super();
		this.seatType = seatType;
		this.seatColunm = seatColunm;
		this.seatRow = seatRow;
		this.timeTable = timeTable;
		this.orderDetail = orderDetail;
		this.isAvailable = isAvailable;
		this.seatName = seatColunm + seatType + seatRow ;
	}

	public SeatOrder() {
	}




    
}
