package com.iuh.busgoo.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iuh.busgoo.config.VNPayConfig;
import com.iuh.busgoo.constant.Constant;
import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.entity.Order;
import com.iuh.busgoo.requestType.PaymentReturn;
import com.iuh.busgoo.service.OrderService;
import com.iuh.busgoo.service.PaymentService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController("/api")
public class PaymentController {
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private OrderService orderService;
	
	public static String payCash = "CASH";
	public static String payVNP = "VNPAY";
	
	@GetMapping("/payment")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse getPay(@RequestParam Long orderId, @RequestParam String paymentType) throws UnsupportedEncodingException{
		DataResponse dataResponse = new DataResponse();
		try {
			Order order = orderService.findOrderById(orderId);
			if(order == null) {
				dataResponse.setResponseMsg("Order does not exist");
	            dataResponse.setRespType(Constant.PAYMENT_FAILED);
	            return dataResponse;
			}else {
				if(order.getIsPay().equals(1)) {
					dataResponse.setResponseMsg("Order has been paid for");
		            dataResponse.setRespType(Constant.PAYMENT_FAILED);
		            return dataResponse;
				}
				if(paymentType.toUpperCase().equals(payCash)){
					return paymentService.createPaymentByCash(order.getId());
				}else {
					String vnp_Version = "2.1.0";
			        String vnp_Command = "pay";
			        String orderType = "other";
//			        long amount = 10000*100;
			        String bankCode = "NCB";
			        
			        String vnp_TxnRef = VNPayConfig.getRandomNumber(8)+order.getId();
			        String vnp_IpAddr = "127.0.0.1";

			        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;
			        
			        Map<String, String> vnp_Params = new HashMap<>();
			        vnp_Params.put("vnp_Version", vnp_Version);
			        vnp_Params.put("vnp_Command", vnp_Command);
			        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
			        Long amount = order.getTotal().longValue();
			        vnp_Params.put("vnp_Amount", String.valueOf(amount*100));
			        vnp_Params.put("vnp_CurrCode", "VND");
			        
			        vnp_Params.put("vnp_BankCode", bankCode);
			        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
			        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef+orderId);
			        vnp_Params.put("vnp_OrderType", orderType);

			        vnp_Params.put("vnp_Locale", "vn");
			        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
			        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

			        LocalDateTime createdTime = LocalDateTime.now();
			        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
			        System.out.println(createdTime);
			        String vnp_CreateDate = createdTime.format(formatter);
			        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
			        
			        LocalDateTime expireTime = createdTime.plusMinutes(30);
			        System.out.println(expireTime);
			        String vnp_ExpireDate = expireTime.format(formatter);
			        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
			        
			        List fieldNames = new ArrayList(vnp_Params.keySet());
			        Collections.sort(fieldNames);
			        StringBuilder hashData = new StringBuilder();
			        StringBuilder query = new StringBuilder();
			        Iterator itr = fieldNames.iterator();
			        while (itr.hasNext()) {
			            String fieldName = (String) itr.next();
			            String fieldValue = (String) vnp_Params.get(fieldName);
			            if ((fieldValue != null) && (fieldValue.length() > 0)) {
			                //Build hash data
			                hashData.append(fieldName);
			                hashData.append('=');
			                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
			                //Build query
			                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
			                query.append('=');
			                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
			                if (itr.hasNext()) {
			                    query.append('&');
			                    hashData.append('&');
			                }
			            }
			        }
			        String test = VNPayConfig.hashAllFields(vnp_Params);
			        String queryUrl = query.toString();
			        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
			        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
			        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;
					
			        dataResponse.setResponseMsg("Http success !!!");
		            dataResponse.setRespType(Constant.HTTP_SUCCESS);
		            Map<String, Object> respValue = new HashMap<String, Object>();
		            respValue.put("paymentUrl", paymentUrl);
		            dataResponse.setValueReponse(respValue);
		            return dataResponse;
				}
			}
		} catch (Exception e) {
            dataResponse.setResponseMsg("System error");
            dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
            return dataResponse;
		}
		
	}
	
	@PostMapping("/verify-payment")
	@SecurityRequirement(name = "bearerAuth")
	public DataResponse verifyPayment(@RequestBody PaymentReturn paymentReturn) {
		return paymentService.verifyPayment(paymentReturn);
	}
}
