package com.iuh.busgoo.mail;

import java.util.Map;

import lombok.Data;

@Data
public class Mail {
	private String to;
	private String from;
	private String subject;
	private Map<String, Object> model;
}
