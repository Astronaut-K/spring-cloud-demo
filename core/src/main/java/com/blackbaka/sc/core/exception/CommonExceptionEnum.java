package com.blackbaka.sc.core.exception;

/**
 * @Author Kaiyi Zhang
 * @Date 2019/06/14
 * @Description
 */

public enum CommonExceptionEnum implements ExceptionEnum {
    //
    REQUEST_METHOD_NOT_SUPPORT(400,"Request method not support"),
    AUTHENTICATE_TOKEN_NOT_FOUND(400, "token缺失"),
    AUTHENTICATE_WRONG_TOKEN(400, "token格式错误"),
    AUTHENTICATE_FAIL(400, "无权调用该接口"),
    BAD_REQUEST_PARAM_MISS(400, "非法请求，参数缺失"),
    BAD_REQUEST_PARAM_ERROR(400, "非法请求，参数错误"),
    SYSTEM_ERROR(500, "系统错误"),
    ALREADY_EXISTS(400, "提交的数据已存在，需二次确认");


    public int code;
    public String message;

    CommonExceptionEnum(int code, String message) {
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
