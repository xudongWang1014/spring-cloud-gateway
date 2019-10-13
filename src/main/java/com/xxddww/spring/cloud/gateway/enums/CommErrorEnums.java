package com.xxddww.spring.cloud.gateway.enums;


/**
 * 通用错误码
 * <pre>
 *     1.key规范：CODE_XXXX
 *     2.value规范：common.error.code.xxxx
 * </pre>
 */
public enum CommErrorEnums {

	/**
	 * "系统繁忙"
	 */
	CODE_0001("common.error.code.0001", "系统繁忙"),
	/**
	 * 请求超时
	 */
	CODE_0002("common.error.code.0002", "请求超时"),


	/**
	 * "系统异常"
	 */
	CODE_9999("common.error.code.9999", "系统异常");

	private final String code;

	private final String message;

	CommErrorEnums(final String code, final String message){
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

}
