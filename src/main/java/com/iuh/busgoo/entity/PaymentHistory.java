package com.iuh.busgoo.entity;

import java.io.Serializable;

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
@Table(name = "payment_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class PaymentHistory extends AbstractEntity implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", length = 255)
    private String code;
    
    @Column(name = "order_id")
    private Long orderId;
    
    @Column(name = "payment_type")
    private String paymentType;
    
    @Column(name = "amount")
    private Long amount;
    
    @Column(name = "bank_code")
    private String bankCode;
    
    @Column(name = "bank_tran_no")
    private String bankTranNo;

    @Column(name ="transaction_status")
    private Integer transactionStatus;
}
