package com.iuh.busgoo.dto;

import lombok.Data;

@Data
public class StationDTO {
    private Long id;

    private String code;

    private String name;

    private Long addressId;

    private Integer status;

    private String description;
    
    private String address;
    
    private String addressParent;
    
    private String addressDescription;
}
