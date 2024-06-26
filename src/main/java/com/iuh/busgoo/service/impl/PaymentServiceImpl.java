package com.iuh.busgoo.service.impl;

import java.lang.constant.Constable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iuh.busgoo.config.VNPayConfig;
import com.iuh.busgoo.constant.Constant;
import com.iuh.busgoo.controller.PaymentController;
import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.entity.Invoice;
import com.iuh.busgoo.entity.Order;
import com.iuh.busgoo.entity.OrderDetail;
import com.iuh.busgoo.entity.PaymentHistory;
import com.iuh.busgoo.entity.SeatOrder;
import com.iuh.busgoo.entity.TimeTable;
import com.iuh.busgoo.repository.InvoiceRepository;
import com.iuh.busgoo.repository.OrderDetailRepository;
import com.iuh.busgoo.repository.OrderRepository;
import com.iuh.busgoo.repository.PaymentHistoryRepository;
import com.iuh.busgoo.requestType.PaymentReturn;
import com.iuh.busgoo.service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private InvoiceRepository invoiceRepository;

	@Autowired
	private PaymentHistoryRepository paymentHistoryRepository;
	
	@Autowired
	private OrderDetailRepository orderDetailRepository;

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
//		if(signValue.equals(paymentReturn.getVnp_SecureHash())) {
		boolean checkOrderId = false; // vnp_TxnRef exists in your database
		boolean checkAmount = false; // vnp_Amount is valid (Check vnp_Amount VNPAY returns compared to the amount of
										// the code (vnp_TxnRef) in the Your database).
		boolean checkOrderStatus = false; // PaymnentStatus = 0 (pending)

		Long orderId = Long.parseLong(paymentReturn.getVnp_TxnRef().substring(8));
		Order order = orderRepository.getById(orderId);
		if (order != null && order.getId()!=null) {
			checkOrderId = true;
			Long amount = order.getTotal().longValue();
			if (amount.equals(paymentReturn.getVnp_Amount()/100)) {
				checkAmount = true;
			}
			if (order.getIsPay().equals(0)) {
				checkOrderStatus = true;
			}
		}else {
			dataResponse.setResponseMsg("Order not exist");
			dataResponse.setRespType(Constant.PAYMENT_FAILED);
			return dataResponse;
		}
//			if(checkOrderId) {
		if (checkAmount) {
			if (checkOrderStatus) {
				if ("00".equals(paymentReturn.getVnp_ResponseCode())) {
					order.setIsPay(1);
					orderRepository.save(order);
					//create invoice
					Invoice invoice = new Invoice();
					Long countInv = invoiceRepository.count();
					invoice.setCode("INVC"+(countInv +1));
					invoice.setOrderId(order.getId());
					invoice.setUserId(order.getUser().getUserId());
					invoice.setTimeBooking(order.getCreatedDate());
					invoice.setTotal(order.getTotal());
					invoice.setTotalDiscount(order.getTotalDiscount());
					invoice.setTotalTiketPrice(order.getTotalTiketPrice());
					invoice.setStatus(1);
					List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(order.getId());
					if(orderDetails!= null && orderDetails.size()>0) {
						OrderDetail orderDetail = orderDetails.get(0);
						SeatOrder seatOrder = orderDetail.getSeat();
						TimeTable timeTable = seatOrder.getTimeTable();
						invoice.setTimeStarted(timeTable.getStartedTime());
					}
					invoiceRepository.save(invoice);
					// create history payment
					Long count = paymentHistoryRepository.count();
					PaymentHistory paymentHistory = new PaymentHistory();
					paymentHistory.setAmount(order.getTotal().longValue());
					paymentHistory.setCode("PMC" + (count + 1));
					paymentHistory.setOrderId(order.getId());
					paymentHistory.setPaymentType(PaymentController.payVNP);
					paymentHistory.setTransactionStatus(1);
					paymentHistory.setBankCode(paymentReturn.getVnp_BankCode());
					paymentHistory.setBankTranNo(paymentReturn.getVnp_BankTranNo());
					paymentHistoryRepository.save(paymentHistory);
					dataResponse.setResponseMsg("Payment success !!!");
					dataResponse.setRespType(Constant.HTTP_SUCCESS);
					return dataResponse;

				} else {
					// create history payment
					// thanh toán thất bại
					Long count = paymentHistoryRepository.count();
					PaymentHistory paymentHistory = new PaymentHistory();
					paymentHistory.setAmount(order.getTotal().longValue());
					paymentHistory.setCode("PMC" + (count + 1));
					paymentHistory.setOrderId(order.getId());
					paymentHistory.setPaymentType(PaymentController.payVNP);
					paymentHistory.setTransactionStatus(0);
					paymentHistory.setBankCode(paymentReturn.getVnp_BankCode());
					paymentHistory.setBankTranNo(paymentReturn.getVnp_BankTranNo());
					paymentHistoryRepository.save(paymentHistory);
					dataResponse.setResponseMsg("Payment failed");
					dataResponse.setRespType(Constant.PAYMENT_FAILED);
					return dataResponse;
				}
			} else {
				dataResponse.setResponseMsg("Order already confirmed");
				dataResponse.setRespType(Constant.PAYMENT_FAILED);
				return dataResponse;
			}
		} else {
			dataResponse.setResponseMsg("Invalid Amount");
			dataResponse.setRespType(Constant.PAYMENT_FAILED);
			return dataResponse;
		}
	}
//	else{
//		dataResponse.setResponseMsg("Order not exist");
//		dataResponse.setRespType(Constant.PAYMENT_FAILED);
//		return dataResponse;
//	}
	/*
	 * }else { dataResponse.setResponseMsg("Invalid Checksum");
	 * dataResponse.setRespType(Constant.PAYMENT_FAILED); return dataResponse; }
	 */


	@Override
	public DataResponse createPaymentByCash(Long id) {
		DataResponse dataResponse = new DataResponse();
		try {
			Order order = orderRepository.getById(id);
			if (order == null) {
				throw new Exception();
			} else {
				if(order.getIsPay()==1) {
					dataResponse.setResponseMsg("This order has been paid. !!!");
					dataResponse.setRespType(Constant.PAYMENT_FAILED);
					return dataResponse;
				}
				Long count = paymentHistoryRepository.count();
				PaymentHistory paymentHistory = new PaymentHistory();
				paymentHistory.setAmount(order.getTotal().longValue());
				paymentHistory.setCode("PMC" + (count + 1));
				paymentHistory.setOrderId(order.getId());
				paymentHistory.setPaymentType(PaymentController.payCash);
				paymentHistory.setTransactionStatus(1);
				paymentHistoryRepository.save(paymentHistory);
				order.setIsPay(1);
				orderRepository.save(order);
				Invoice invoice = new Invoice();
				Long countInv = invoiceRepository.count();
				invoice.setCode("INVC"+(countInv +1));
				invoice.setOrderId(order.getId());
				invoice.setUserId(order.getUser().getUserId());
				invoice.setTimeBooking(order.getCreatedDate());
				invoice.setTotal(order.getTotal());
				invoice.setTotalDiscount(order.getTotalDiscount());
				invoice.setTotalTiketPrice(order.getTotalTiketPrice());
				invoice.setStatus(1);
				List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(order.getId());
				if(orderDetails!= null && orderDetails.size()>0) {
					OrderDetail orderDetail = orderDetails.get(0);
					SeatOrder seatOrder = orderDetail.getSeat();
					TimeTable timeTable = seatOrder.getTimeTable();
					invoice.setTimeStarted(timeTable.getStartedTime());
				}
				invoiceRepository.save(invoice);
				dataResponse.setResponseMsg("Payment success !!!");
				dataResponse.setRespType(Constant.HTTP_SUCCESS);
				return dataResponse;
			}
		} catch (Exception e) {
			dataResponse.setResponseMsg("System error !!!");
			dataResponse.setRespType(Constant.PAYMENT_FAILED);
			return dataResponse;
		}
	}

}
