package com.iuh.busgoo.service.impl;

import java.lang.constant.Constable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iuh.busgoo.config.VNPayConfig;
import com.iuh.busgoo.constant.Constant;
import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.entity.Order;
import com.iuh.busgoo.repository.OrderRepository;
import com.iuh.busgoo.requestType.PaymentReturn;
import com.iuh.busgoo.service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private OrderRepository orderRepository;

	@Override
	public DataResponse verifyPayment(PaymentReturn paymentReturn) {
		DataResponse dataResponse = new DataResponse();
		
		Map<String, String> field = new HashMap<String, String>();
		field.put("vnp_Amount", paymentReturn.getVnp_Amount().toString());
		field.put("vnp_BankCode", paymentReturn.getVnp_BankCode());
		field.put("vnp_BankTranNo", paymentReturn.getVnp_Amount().toString());
		field.put("vnp_CardType", paymentReturn.getVnp_OrderInfo());
		field.put("vnp_OrderInfo", paymentReturn.getVnp_OrderInfo());
		field.put("vnp_PayDate", paymentReturn.getVnp_PayDate().toString());
		field.put("vnp_ResponseCode", paymentReturn.getVnp_ResponseCode());
		field.put("vnp_TmnCode", paymentReturn.getVnp_TmnCode());
		field.put("vnp_TransactionNo", paymentReturn.getVnp_TransactionNo());
		field.put("vnp_TransactionStatus", paymentReturn.getVnp_TransactionStatus());
		field.put("vnp_TxnRef", paymentReturn.getVnp_TxnRef());
//		field.put("vnp_SecureHash", paymentReturn.getVnp_SecureHash());
		String signValue = VNPayConfig.hashAllFields(field);
		if(signValue.equals(paymentReturn.getVnp_SecureHash())) {
			 boolean checkOrderId = false; // vnp_TxnRef exists in your database
	         boolean checkAmount = false; // vnp_Amount is valid (Check vnp_Amount VNPAY returns compared to the amount of the code (vnp_TxnRef) in the Your database).
	         boolean checkOrderStatus = false; // PaymnentStatus = 0 (pending)
			
			Long orderId = Long.parseLong(paymentReturn.getVnp_TxnRef().substring(7));
			Order order = orderRepository.getById(orderId);
			if(order != null) {
				checkOrderId = true;
				Long amount =order.getTotal().longValue();
				if(amount.equals(paymentReturn.getVnp_Amount())) {
					checkAmount = true;
				}
				if(order.getIsPay().equals(0)) {
					checkOrderStatus = true;
				}
			}
//			if(checkOrderId) {
				if(checkAmount) {
					if(checkOrderStatus) {
						if ("00".equals(paymentReturn.getVnp_ResponseCode()))
                        {
                            order.setIsPay(1);
                            orderRepository.save(order);
                            //create history payment
                        }
                        else
                        {
                        	dataResponse.setResponseMsg("Payment failed");
    						dataResponse.setRespType(Constant.PAYMENT_FAILED);
    						return dataResponse;
                        }
					}else {
						dataResponse.setResponseMsg("Order already confirmed");
						dataResponse.setRespType(Constant.PAYMENT_FAILED);
						return dataResponse;
					}
				}else {
					dataResponse.setResponseMsg("Invalid Amount");
					dataResponse.setRespType(Constant.PAYMENT_FAILED);
					return dataResponse;
				}
			}else {
				dataResponse.setResponseMsg("Order not exist");
				dataResponse.setRespType(Constant.PAYMENT_FAILED);
				return dataResponse;
			}
			/*
			 * }else { dataResponse.setResponseMsg("Invalid Checksum");
			 * dataResponse.setRespType(Constant.PAYMENT_FAILED); return dataResponse; }
			 */
		return null;
	}

}
