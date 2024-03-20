package com.iuh.busgoo.filter;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BustripFilter {
	Long fromId;
	Long toId;
	LocalDateTime timeStarted;
}
