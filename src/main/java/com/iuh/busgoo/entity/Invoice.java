package com.iuh.busgoo.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "invoice")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Invoice extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "code", length = 255)
	private String code;

	@Column(name = "order_id")
	private Long orderId;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "time_booking")
	private LocalDate timeBooking;
	
	@Column(name = "time_started")
	private LocalDateTime timeStarted;

	@Column(name = "total")
	private Double total;

	@Column(name = "total_discount")
	private Double totalDiscount;

	@Column(name = "total_tiket_price")
	private Double totalTiketPrice;

	@Column(name = "status")
	private Integer status;

	@Column(name = "reason",length = 2000)
	private String reason;
}
