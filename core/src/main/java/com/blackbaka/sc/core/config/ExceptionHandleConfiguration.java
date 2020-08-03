package com.blackbaka.sc.core.config;

import com.blackbaka.sc.core.exception.CommonExceptionEnum;
import com.blackbaka.sc.core.exception.RemoteServiceException;
import com.blackbaka.sc.core.exception.ServiceErrorTrace;
import com.blackbaka.sc.core.exception.ServiceException;
import com.blackbaka.sc.core.result.Result;
import feign.FeignException;
import feign.codec.DecodeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @Author Kaiyi Zhang
 * @Date 2019/06/26
 * @Description 全局异常捕获处理
 */

@Slf4j
@ControllerAdvice
public class ExceptionHandleConfiguration {

    @Value("${spring.application.name}")
    private String applicationName;


    /**
     * 处理系统业务错误异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = ServiceException.class)
    @ResponseBody
    public Result handleServiceException(ServiceException e) {
        Result result = new Result(false);

        int code = e.getExceptionEnum().getCode();
        String message = e.getExceptionEnum().getMessage();

        result.setCode(code);
        result.setMessage(message);
        log.error("[{}]_{}", result.getCode(), result.getMessage(), e);

        ServiceErrorTrace trace = initServiceErrorTrace(result.getCode(), result.getMessage());
        fillErrorTrace(result, trace);
        return result;
    }

    /**
     * 处理远程接口调用错误异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = RemoteServiceException.class)
    @ResponseBody
    public Result handleRemoteServiceException(RemoteServiceException e) {
        Result result = new Result(false);
        ServiceErrorTrace trace = e.getErrorTrace();
        if (trace == null) {
            trace = initServiceErrorTrace(result.getCode(), result.getMessage());
        } else {
            appendServiceErrorTrace(trace);
            result.setErrorTraceMessage(getRemoteErrorMessage(trace));
        }
        // code和message复制源服务返回的内容，不做额外修改
        result.setCode(trace.getCode());
        result.setMessage(trace.getMessage());
        log.error("[{}]_{}", result.getCode(), result.getMessage());

        fillErrorTrace(result, trace);
        return result;
    }


    /**
     * 处理远程接口调用错误异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = FeignException.class)
    @ResponseBody
    public Result handleFeignException(FeignException e) {
        if (e.getCause() instanceof RemoteServiceException) {
            return handleRemoteServiceException((RemoteServiceException) e.getCause());
        }
        return handleException(e);
    }


    /**
     * 处理远程接口调用错误异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = DecodeException.class)
    @ResponseBody
    public Result handleDecodeException(DecodeException e) {
        if (e.getCause() instanceof RemoteServiceException) {
            return handleRemoteServiceException((RemoteServiceException) e.getCause());
        }
        return handleException(e);
    }


    /**
     * 处理request method 错误异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public Result handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        Result result = new Result(false);

        result.setCode(CommonExceptionEnum.REQUEST_METHOD_NOT_SUPPORT.getCode());
        result.setMessage(e.getMessage());

        ServiceErrorTrace trace = initServiceErrorTrace(result.getCode(), result.getMessage());
        fillErrorTrace(result, trace);
        return result;
    }


    /**
     * 处理请求参数验证失败异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Result result = new Result(false);

        StringBuilder sb = new StringBuilder("参数错误: ");
        List<ObjectError> obErrors = e.getBindingResult().getAllErrors();
        for (ObjectError oe : obErrors) {
            if (oe instanceof FieldError) {
                FieldError foe = (FieldError) oe;
                sb.append("参数名: ").append(foe.getField()).append(", 错误原因: ").append(foe.getDefaultMessage()).append(";");
            } else {
                sb.append(oe.getObjectName()).append(" : ").append(oe.getDefaultMessage()).append(";");
            }
        }

        //参数错误异常
        result.setCode(CommonExceptionEnum.BAD_REQUEST_PARAM_ERROR.getCode());
        result.setMessage(sb.toString());

        ServiceErrorTrace trace = initServiceErrorTrace(result.getCode(), result.getMessage());
        fillErrorTrace(result, trace);
        return result;
    }


    @ExceptionHandler(value = ValidationException.class)
    @ResponseBody
    public Result handleAddressException(ValidationException e) {
        if (e instanceof ConstraintViolationException) {
            return handleConstraintViolationException((ConstraintViolationException) e);
        }

        Result result = new Result(false);
        result.setCode(CommonExceptionEnum.BAD_REQUEST_PARAM_ERROR.getCode());
        result.setMessage(e.getMessage());
        ServiceErrorTrace trace = initServiceErrorTrace(result.getCode(), result.getMessage());
        fillErrorTrace(result, trace);
        return result;
    }

    /**
     * 处理请求参数校验失败异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseBody
    public Result handleConstraintViolationException(ConstraintViolationException e) {
        Result result = new Result(false);

        Set<ConstraintViolation<?>> set = e.getConstraintViolations();
        if (set != null && !set.isEmpty()) {
            Iterator<ConstraintViolation<?>> iterator = set.iterator();
            List<String> errMsgs = new ArrayList<>(set.size());
            while (iterator.hasNext()) {
                ConstraintViolation errPtr = iterator.next();
                errMsgs.add(String.format("参数名:%s  输入值:%s  错误原因:%s", errPtr.getPropertyPath(), errPtr.getInvalidValue(), errPtr.getMessage()));
            }
            result.setCode(CommonExceptionEnum.BAD_REQUEST_PARAM_ERROR.getCode());
            result.setMessage(CommonExceptionEnum.BAD_REQUEST_PARAM_ERROR.getMessage() + " -> " + errMsgs.toString());
        }

        ServiceErrorTrace trace = initServiceErrorTrace(result.getCode(), result.getMessage());
        fillErrorTrace(result, trace);
        return result;
    }


    /**
     * 处理请求参数绑定错误异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = ServletRequestBindingException.class)
    @ResponseBody
    public Result handleServletRequestBindingException(ServletRequestBindingException e) {
        Result result = new Result(false);

        result.setCode(CommonExceptionEnum.BAD_REQUEST_PARAM_ERROR.getCode());
        result.setMessage(e.getMessage());

        ServiceErrorTrace trace = initServiceErrorTrace(result.getCode(), result.getMessage());
        fillErrorTrace(result, trace);
        return result;
    }

    /**
     * 处理请求参数缺失异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public Result handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        Result result = new Result(false);

        result.setCode(CommonExceptionEnum.BAD_REQUEST_PARAM_MISS.getCode());
        result.setMessage(CommonExceptionEnum.BAD_REQUEST_PARAM_MISS.getMessage());

        ServiceErrorTrace trace = initServiceErrorTrace(result.getCode(), result.getMessage());
        fillErrorTrace(result, trace);
        return result;
    }


    /**
     * 处理请求参数绑定错误异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public Result handleBindException(BindException e) {
        Result result = new Result(false);

        if (e.hasErrors()) {
            StringBuilder errors = new StringBuilder();
            for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
                errors.append(fieldError.getDefaultMessage()).append("，");
            }
            result.setCode(CommonExceptionEnum.BAD_REQUEST_PARAM_ERROR.getCode());
            result.setMessage(CommonExceptionEnum.BAD_REQUEST_PARAM_ERROR.getMessage() + " -> " + errors.substring(0, errors.lastIndexOf("，")));
        }

        ServiceErrorTrace trace = initServiceErrorTrace(result.getCode(), result.getMessage());
        fillErrorTrace(result, trace);
        return result;
    }


    /**
     * 处理未知系统错误
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result handleException(Exception e) {
        Result result = new Result(false);

        //未知错误，打印日志
        log.error("[{}]_{}", CommonExceptionEnum.SYSTEM_ERROR.getCode(), e.getMessage(), e);
        result.setCode(CommonExceptionEnum.SYSTEM_ERROR.getCode());
        result.setMessage(CommonExceptionEnum.SYSTEM_ERROR.getMessage());

        ServiceErrorTrace trace = initServiceErrorTrace(result.getCode(), result.getMessage());
        fillErrorTrace(result, trace);
        return result;
    }


    private void fillErrorTrace(Result result, ServiceErrorTrace trace) {
        if (result == null) {
            return;
        }
        result.setWochanyeErrorTrace(trace);
    }


    /**
     * 获取到远程接口调用失败返回的错误信息
     *
     * @param errorTrace
     * @return
     */
    private String getRemoteErrorMessage(ServiceErrorTrace errorTrace) {
        StringBuilder messageBuilder = new StringBuilder("远程服务调用错误; 服务调用轨迹：");
        List<String> serviceIds = errorTrace.getServiceIdTraces();
        String sourceServiceId = serviceIds.isEmpty() ? null : serviceIds.get(0);

        if (!serviceIds.isEmpty()) {
            for (int i = serviceIds.size() - 1; i >= 0; i--) {
                messageBuilder.append(serviceIds.get(i)).append(" --> ");
            }
            messageBuilder.delete(messageBuilder.lastIndexOf(" --> "), messageBuilder.length());
        }

        messageBuilder.append("; 抛出错误异常的源服务：").append(sourceServiceId);
        messageBuilder.append("; 源服务错误异常码：").append(errorTrace.getCode());
        messageBuilder.append("; 源服务错误异常描述：").append(errorTrace.getMessage());

        return messageBuilder.toString();
    }


    /**
     * 初始化服务链路调用轨迹
     *
     * @param code
     * @param message
     * @return
     */
    private ServiceErrorTrace initServiceErrorTrace(int code, String message) {
        ServiceErrorTrace errorTrace = new ServiceErrorTrace();
        errorTrace.setCode(code);
        errorTrace.setMessage(message);
        errorTrace.addServiceId(applicationName);
        return errorTrace;
    }


    /**
     * 追加服务链路调用轨迹
     *
     * @param errorTrace
     */
    private void appendServiceErrorTrace(ServiceErrorTrace errorTrace) {
        if (errorTrace == null) {
            return;
        }
        errorTrace.addServiceId(applicationName);
    }

}
