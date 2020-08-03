package com.blackbaka.sc.core.result;

import com.blackbaka.sc.core.exception.ServiceErrorTrace;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @Author Kai Yi
 * @Date 2020/01/10
 * @Description
 */

public abstract class AbstractResult {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ServiceErrorTrace wochanyeErrorTrace;

    private boolean success;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int code;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String errorTraceMessage;


    public ServiceErrorTrace getWochanyeErrorTrace() {
        return wochanyeErrorTrace;
    }

    public void setWochanyeErrorTrace(ServiceErrorTrace wochanyeErrorTrace) {
        this.wochanyeErrorTrace = wochanyeErrorTrace;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorTraceMessage() {
        return errorTraceMessage;
    }

    public void setErrorTraceMessage(String errorTraceMessage) {
        this.errorTraceMessage = errorTraceMessage;
    }
}
