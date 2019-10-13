package com.xxddww.spring.cloud.gateway.base;


import java.io.Serializable;

/**
 *  控制层返回对象
 */
public class ResponseData implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 成功标记
	 */
	boolean success;
	/**
	 * 错误编码
	 */
	private String errorCode;
	/**
	 * 返回描述
	 */
	private String message;
	/**
	 * 业务数据
	 */
	private Object dataObj;

	public ResponseData() { }

	public ResponseData(boolean success, String errorCode, String message, Object dataObj) {
		this.success = success;
		this.errorCode = errorCode;
		this.message = message;
		this.dataObj = dataObj;
	}
	public static ResponseData success(){
		return new ResponseData(Boolean.TRUE,"","",null);
	}
	public static ResponseData success(Object dataObj){
		return new ResponseData(Boolean.TRUE,"","", dataObj);
	}
	public static ResponseData fail(String errorCode){
		return new ResponseData(Boolean.FALSE, errorCode,"",null);
	}
	public static ResponseData fail(String errorCode, String message){
		return new ResponseData(Boolean.FALSE,  errorCode, message,null);
	}
	public static ResponseData fail(String errorCode, Object dataObj){
		return new ResponseData(Boolean.FALSE, errorCode,"", dataObj);
	}
	public static ResponseData fail(String errorCode, String message, Object dataObj){
		return new ResponseData(Boolean.FALSE, errorCode, message, dataObj);
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getDataObj() {
		return dataObj;
	}

	public void setDataObj(Object dataObj) {
		this.dataObj = dataObj;
	}
}
