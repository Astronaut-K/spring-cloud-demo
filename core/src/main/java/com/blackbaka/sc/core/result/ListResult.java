package com.blackbaka.sc.core.result;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @Author Kai Yi
 * @Date 2020/01/11
 * @Description
 */

public class ListResult<T extends List<?>> extends AbstractResult implements Serializable {

    @JsonIgnore
    private static final long serialVersionUID = -1306050490867288725L;

    @JsonProperty("data")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ListData<T> data;

    public ListResult() {
    }

    public ListResult(boolean success) {
        setSuccess(success);
    }

    @JsonIgnore
    public T getData() {
        if (data != null) {
            return data.getList();
        } else {
            return null;
        }
    }

    @JsonIgnore
    public T getDataOrDefault(T t) {
        if (data == null) {
            data = new ListData<>(t);
        }
        if (data.getList() == null) {
            data.setList(t);
        }
        return data.getList();
    }


    @JsonIgnore
    public void setData(T data) {
        if (this.data == null) {
            this.data = new ListData<>(data);
        } else {
            this.data.setList(data);
        }
    }

}
