package com.iuh.busgoo.controller;

import com.iuh.busgoo.constant.Constant;
import com.iuh.busgoo.dto.DataResponse;
import com.iuh.busgoo.service.BusService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/bus")
public class BusController {
    @Autowired
    private BusService busService;

    @GetMapping("/find-all")
    @SecurityRequirement(name = "bearerAuth")
    public DataResponse findAllRoute() {
        try {
            return busService.findAll();
        } catch (Exception e) {
            DataResponse dataResponse = new DataResponse();
            dataResponse.setResponseMsg("System error");
            dataResponse.setRespType(Constant.SYSTEM_ERROR_CODE);
            return dataResponse;
        }
    }

}
