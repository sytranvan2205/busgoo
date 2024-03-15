package com.iuh.busgoo.service.impl;

import com.iuh.busgoo.constant.Constant;
import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.entity.Bus;
import com.iuh.busgoo.entity.Route;
import com.iuh.busgoo.repository.BusRepository;
import com.iuh.busgoo.repository.OrderRepository;
import com.iuh.busgoo.service.BusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BusServiceImpl implements BusService {
    @Autowired
    private BusRepository busRepository;

    @Override
    public DataResponse findAll() {
        DataResponse dataResponse = new DataResponse();
        try {
            List<Bus> buses = busRepository.findByStatus(1);
            Map<String, Object> respValue = new HashMap<>();
            respValue.put("data", buses);
            dataResponse.setValueReponse(respValue);
            dataResponse.setResponseMsg("Get list bus success!!!");
            dataResponse.setRespType(Constant.HTTP_SUCCESS);
            return dataResponse;
        } catch (Exception e) {
            dataResponse.setResponseMsg("System error");
            dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
            return dataResponse;
        }
    }
}
