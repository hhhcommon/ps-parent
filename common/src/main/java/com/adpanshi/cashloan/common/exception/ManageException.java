package com.adpanshi.cashloan.common.exception;


import com.adpanshi.cashloan.common.enums.ManageExceptionEnum;

/**
 * @auther huangqin
 * @data 2017-12-18
 * @dsscription Manage用户登录异常
 * @since 2017-12-18 22:10:23
 * @version 1.0.0
 * */
public class ManageException extends Exception{

	private static final long serialVersionUID = 9051275901320933959L;
	/**
	 * 错误码
	 */
	private int errorCode;

	@Deprecated
	public ManageException() {
		super();
	}

	public ManageException(ManageExceptionEnum en) {
		super(en.Msg());
		this.errorCode = en.Code();
	}

	/**
	 * 获取错误码
	 * */
	public int getErrorCode() {
		return errorCode;
	}
	/**
	 * 设置错误码
	 * */
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	/**
	 * 获取错误信息
	 * */
	public String getErrorMsg() {
		return getMessage();
	}
}
