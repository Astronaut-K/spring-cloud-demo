package com.blackbaka.sc.core.exception;



/**
 * @Author Kaiyi Zhang
 * @Date 2019/06/26
 * @Description 远程服务调用异常
 */

public class RemoteServiceException extends RuntimeException {

    // http response status code <a>https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html</a>
    private int status;

    private String requestServiceId;

    private ExceptionEnum error;

    private ServiceErrorTrace errorTrace;

    public RemoteServiceException(int status, String requestServiceId, ExceptionEnum error, ServiceErrorTrace errorTrace) {
        super(error.getMessage());
        this.status = status;
        this.requestServiceId = requestServiceId;
        this.error = error;
        this.errorTrace = errorTrace;
    }

    public int getStatus() {
        return status;
    }

    public ExceptionEnum getError() {
        return error;
    }

    public ServiceErrorTrace getErrorTrace() {
        return errorTrace;
    }

    public String getRequestServiceId() {
        return requestServiceId;
    }
}
