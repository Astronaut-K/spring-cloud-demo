package com.blackbaka.sc.core.util;

import com.blackbaka.sc.core.result.ListResult;
import com.blackbaka.sc.core.result.PageResult;
import com.blackbaka.sc.core.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 通用结果返回工具类
 *
 * @author cuijie
 * @since 2018-07-03
 */
public class ResponseUtil {

    private static final Logger logger = LoggerFactory.getLogger(ResponseUtil.class);


    /**
     * 操作成功默认返回
     *
     * @return
     */
    public static Result operateSuccess() {
        Result result = new Result(true);
        result.setCode(200);
        result.setMessage("操作成功");
        return result;
    }

    /**
     * 操作成功返回具体数据
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> operateSuccess(T data) {
        Result<T> result = new Result<>(true);
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    /**
     * 操作失败
     *
     * @return
     */
    public static Result operateFail() {
        Result result = new Result(false);
        result.setCode(400);
        result.setMessage("操作失败");
        return result;
    }

    /**
     * 操作失败 - 有数据返回
     *
     * @return
     */
    public static <T> Result<T> operateFail(T data) {
        Result<T> result = new Result<>(false);
        result.setCode(400);
        result.setMessage("操作失败");
        result.setData(data);
        return result;
    }

    /**
     * 操作失败详细提醒 - 需要处理自定义message的情况
     *
     * @param code
     * @param message
     * @return
     */
    public static Result operateFail(int code, String message) {
        Result result = new Result(false);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    /**
     * 获取成功返回具体数据
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> getSuccess(T data) {
        Result<T> result = new Result<>(true);
        result.setCode(200);
        result.setMessage("获取成功");
        result.setData(data);
        return result;
    }

    /**
     * 获取失败
     *
     * @param <T>
     * @return
     */
    public static <T> Result<T> getFail() {
        Result<T> result = new Result<>(false);
        result.setCode(400);
        result.setMessage("获取失败");
        return result;
    }


    /**
     * 获取成功返回分页结果
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T extends List<?>> ListResult<T> getListSuccess(T data) {
        ListResult<T> result = new ListResult<>(true);
        result.setCode(200);
        result.setMessage("获取成功");
        result.setData(data);
        return result;
    }


    /**
     * 获取成功返回分页结果
     *
     * @param total
     * @param data
     * @param <T>
     * @return
     */
    public static <T extends List<?>> PageResult<T> getPageSuccess(int total, T data) {
        PageResult<T> result = new PageResult<>(true);
        result.setCode(200);
        result.setMessage("获取成功");
        result.setTotal(total);
        result.setData(data);
        return result;
    }


}
