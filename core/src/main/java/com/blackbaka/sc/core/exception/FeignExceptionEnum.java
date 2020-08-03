package com.blackbaka.sc.core.exception;

/**
 * @Author Kaiyi Zhang
 * @Date 2019/06/26
 * @Description
 */

public enum  FeignExceptionEnum implements ExceptionEnum {

	REQUEST_URI_NOT_FOUNR(500, "远程服务接口无法访问: 404 not found"),

	REMOTE_UNREACHABLE(500, "远程服务接口无法访问: http status < 200 or >= 300"),

	REMOTE_SERVICE_THROW_EXCEPTION(500,"调用远程服务接口，返回系统错误"),

	REQUEST_PARAM_BUILD_ERROR(500,"请求参数构建错误")
	;

	public int code;
	public String message;

	FeignExceptionEnum(int code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public int getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}

}
