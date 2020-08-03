package com.blackbaka.sc.core.result;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;

/**
 * @Author Kai Yi
 * @Date 2020/01/10
 * @Description
 */

public class PageResult<T extends List<?>> extends ListResult<T> implements Serializable {

    @JsonIgnore
    private static final long serialVersionUID = 7169952469149313279L;

    private int total;

    public PageResult() {
    }

    public PageResult(boolean success) {
        setSuccess(success);
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }


}
