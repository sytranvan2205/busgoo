package com.iuh.busgoo.service;

import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.requestType.PaymentReturn;

public interface PaymentService {

	DataResponse verifyPayment(PaymentReturn paymentReturn);

}
