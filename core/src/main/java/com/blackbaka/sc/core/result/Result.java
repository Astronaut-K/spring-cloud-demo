package com.blackbaka.sc.core.result;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @Author Kai Yi
 * @Date 2020/01/10
 * @Description
 */

public class Result<T> extends AbstractResult implements Serializable {

    private static final long serialVersionUID = -8101047701068092725L;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public Result() {
    }

    public Result(boolean success) {
        setSuccess(success);
    }


    public T getData() {
        return data;
    }

    @JsonIgnore
    public T getDataOrDefault(T t) {
        if (data == null) {
            data = t;
        }
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
