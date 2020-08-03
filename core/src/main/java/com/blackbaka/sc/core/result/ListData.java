package com.blackbaka.sc.core.result;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @Author Kai Yi
 * @Date 2020/01/10
 * @Description
 */

public class ListData<T extends List<?>>  {

    @JsonProperty("list")
    private T list;

    public ListData() {
    }

    public ListData(T list) {
        this.list = list;
    }

    public T getList() {
        return list;
    }

    public void setList(T list) {
        this.list = list;
    }
}
