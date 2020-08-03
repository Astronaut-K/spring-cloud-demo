package com.blackbaka.sc.core.exception;

/**
 * 系统常见异常枚举 未知错误-1，其余10000开始
 *
 * @author cuijie
 * @since 2018-07-03
 */
public enum SysExceptionEnum implements ExceptionEnum {

    UNKNOWN_ERROR(-1, "未知错误"),
    BAD_REQUEST_PARAM_MISS(400, "非法请求，参数缺失"),
    BAD_REQUEST_PARAM_ERROR(400, "非法请求，参数错误"),
    SYSTEM_ERROR(500, "系统错误");

    public int code;
    public String message;

    SysExceptionEnum(int code, String message) {
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
