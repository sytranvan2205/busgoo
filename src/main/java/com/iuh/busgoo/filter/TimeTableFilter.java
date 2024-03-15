package com.iuh.busgoo.filter;

import java.time.LocalDateTime;

import com.iuh.busgoo.service.impl.AbstractListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class TimeTableFilter extends AbstractListRequest{
	private Integer status;
	private LocalDateTime departureDate;
	private Long fromId;
	private Long toId;
}
