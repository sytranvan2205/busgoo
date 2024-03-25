package com.iuh.busgoo.filter;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BustripFilter {
	Long fromId;
	Long toId;
	LocalDate timeStarted;
}
