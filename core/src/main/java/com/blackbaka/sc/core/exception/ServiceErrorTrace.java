package com.blackbaka.sc.core.exception;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Kaiyi Zhang
 * @Date 2019/06/26
 * @Description 服务异常错误堆栈信息
 */

public class ServiceErrorTrace implements Serializable {

    private static final long serialVersionUID = -5369062502329559694L;

    private int code;

    private String message;

    private List<String> serviceIdTraces = new ArrayList<>();

    public ServiceErrorTrace() {
    }

    public ServiceErrorTrace(int code, String message, String serviceId) {
        this.code = code;
        this.message = message;
        this.addServiceId(serviceId);
    }

    public void addServiceId(String serviceId) {
        if (serviceId == null) {
            serviceId = "Unknown";
        }
        synchronized (this) {
            serviceIdTraces.add(serviceId);
        }
    }

    public List<String> getServiceIdTraces() {
        return serviceIdTraces;
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

}
