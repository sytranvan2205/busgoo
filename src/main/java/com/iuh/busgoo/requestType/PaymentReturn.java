package com.iuh.busgoo.requestType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentReturn {
	private Long vnp_Amount; 
	private String vnp_BankCode;
	private String vnp_BankTranNo;
	private String vnp_CardType;
	private String vnp_OrderInfo;
	private Long vnp_PayDate;
	private String vnp_ResponseCode;
	private String vnp_TmnCode;
	private String vnp_TransactionNo;
	private String vnp_TransactionStatus;
	private String vnp_TxnRef;
	private String vnp_SecureHash;
	
	
}
