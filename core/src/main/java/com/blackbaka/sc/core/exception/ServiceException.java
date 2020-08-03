package com.blackbaka.sc.core.exception;

/**
 * @Author Kaiyi Zhang
 * @Date 2019/06/13
 * @Description
 */

public class ServiceException extends RuntimeException {

	private ExceptionEnum exceptionEnum;

	public ServiceException(ExceptionEnum exceptionEnum) {
		super(exceptionEnum.getMessage());
		this.exceptionEnum = exceptionEnum;
	}

	public ServiceException(ExceptionEnum exceptionEnum, Throwable cause) {
		super(exceptionEnum.getMessage(),cause);
		this.exceptionEnum = exceptionEnum;
	}

	public ExceptionEnum getExceptionEnum() {
		return exceptionEnum;
	}

}
