package com.iuh.busgoo.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeTableDTO {
	private String busName;
	private String busType;
	private String routeDescription;
	private LocalTime timeTransfer;
	private LocalDateTime timeStarted;
}
