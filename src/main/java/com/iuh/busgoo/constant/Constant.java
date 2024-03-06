package com.iuh.busgoo.constant;

public class Constant {

	public static final String SYSTEM = "SYSTEM";
	public static final String USER = "USER";
	
	//Cote system
	public static final Integer HTTP_SUCCESS = 200;
	public static final Integer SYSTEM_ERROR_CODE = 500;
	public static final Integer ACCOUNT_VERIFY_FAILD = 501;
	
	//Code validate
	public static final Integer USER_NOT_NULL = 110;// code when username null
	public static final Integer EMAIL_NOT_NULL = 111;// code when email null
	public static final Integer PASSWORD_NOT_NULL = 112;// code when password null
	public static final Integer MAIL_TOKEN_NOT_EXIST = 113;// mail token no exist
	public static final Integer MAIL_TOKEN_EXPIRE = 114;// mail token expire
	public static final Integer SUMMARY_NOT_NULL = 115;// summary is not null
	public static final Integer SUBJEB_NOT_NULL = 116;// subject is not null
	public static final Integer USER_NOT_EXIST = 117;// user is not exist
	public static final Integer PHONE_IS_NULL = 118;// phone is null
	public static final Integer EMAIL_IS_EXIST = 119;
	public static final Integer PHONE_IS_EXIST = 120;
	public static final Integer ACCOUNT_HAS_BEEN_VERIFIED = 121;
	public static final Integer ACCOUNT_NOT_EXIST = 122;
	public static final Integer TYPE_BUS_CODE_NOT_NULL = 123;
	public static final Integer ROUTE_CODE_NOT_NULL = 124;
	public static final Integer TO_DATE_NOT_NULL = 125;
	public static final Integer FROM_DATE_NOT_NULL = 126;
	public static final Integer ROUTE_NOT_EXIST = 127;
	public static final Integer TYPE_BUS_NOT_EXIST = 128;
	public static final Integer PRICE_IS_EXIST = 129;
	public static final Integer FROM_ADDRESS_IS_NOT_NULL = 130;
	public static final Integer TO_ADDRESS_IS_NOT_NULL = 131;
	public static final Integer TRANSFER_TIME_IS_NOT_NULL = 132;
	public static final Integer ROUTE_HAS_EXIST = 133;
	public static final Integer TIME_STATED_IS_NOT_NULL = 134;
	public static final Integer PRICE_IS_NOT_EXIST = 135;
	public static final Integer PRICE_DETAIL_IS_EXIST = 136;


}
