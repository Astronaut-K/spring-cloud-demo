package com.blackbaka.sc.core.exception;

import lombok.Data;

/**
 * @Author Kai Yi
 * @Date 2019/12/19
 * @Description
 */

@Data
public class ExceptionObject implements ExceptionEnum {

    private int code;

    private String message;

    public ExceptionObject() {
    }

    public ExceptionObject(int code, String message) {
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
