package com.blackbaka.sc.core.util;

import com.blackbaka.sc.core.constant.ResponseHeaderConsts;
import com.blackbaka.sc.core.exception.ExceptionObject;
import com.blackbaka.sc.core.exception.FeignExceptionEnum;
import com.blackbaka.sc.core.exception.RemoteServiceException;
import com.blackbaka.sc.core.exception.ServiceErrorTrace;
import feign.Request;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.util.List;
import java.util.Objects;

/**
 * @Author Kai Yi
 * @Date 2019/12/20
 * @Description
 */

@Slf4j
public class FeignUtil {

    // 保存feign线程上下文的request请求的远程服务id（remote service id）
    private static final ThreadLocal<String> REMOTE_SERVICE_ID_TL = new ThreadLocal<>();


    public static String extractServiceId(Request request) {
        Objects.requireNonNull(request);
        URI asUri = URI.create(request.url());
        return asUri.getHost();
    }

    /**
     * Client.execute()之后，从response header中判断远程服务调用是否有业务异常。
     *
     * @param response
     * @throws RemoteServiceException 当远程服务接口返回的header中含有相关信息时抛出
     */
    @Deprecated
    public static void afterClientExecute(Request request, Response response) throws RemoteServiceException {
        String serviceId = extractServiceId(request);
        setRemoteServiceId(serviceId);
        if (response == null) {
            return;
        }
        // 返回的http status not in range [200,299) --> 抛出 RemoteServiceException
        if (response.status() < 200 || response.status() >= 300) {
            throw new RemoteServiceException(response.status(), getRemoteServiceId(),
                    new ExceptionObject(500, "远程服务接口调用失败, 调用service id: " + serviceId + ", response http status code: " + response.status()),
                    null);
        }
        //异常返回处理 --> 抛出 RemoteServiceException
        if (response.headers() != null && response.headers().containsKey(ResponseHeaderConsts.SERVICE_ERROR)) {
            throw new RemoteServiceException(response.status(),getRemoteServiceId(), FeignExceptionEnum.REMOTE_SERVICE_THROW_EXCEPTION,
                    HeaderUtil.decodeErrorTrace(
                            ((List<String>) response.headers().get(ResponseHeaderConsts.SERVICE_ERROR)).get(0)
                    ));
        }
    }

    /**
     * 设置feign线程上下文的request请求的远程服务id（remote service id）
     *
     * @param serviceId
     */
    public static void setRemoteServiceId(String serviceId) {
        if (StringUtils.isNotBlank(serviceId)) {
            REMOTE_SERVICE_ID_TL.set(serviceId);
        }

    }

    /**
     * 获取feign线程上下文的request请求的远程服务id（remote service id）
     *
     * @return
     */
    public static String getRemoteServiceId() {
        return REMOTE_SERVICE_ID_TL.get();
    }


    /**
     * 抛出远程服务接口调用错误异常
     *
     * @param response
     * @param resultCode
     * @param resultMessage
     * @param trace
     * @throws RemoteServiceException
     */
    public static void throwRemoteServiceException(Response response, int resultCode, String resultMessage, ServiceErrorTrace trace) throws RemoteServiceException {
        throw getRemoteServiceException(response, resultCode, resultMessage, trace);
    }

    /**
     * 抛出远程服务接口调用错误异常
     *
     * @param response
     * @param resultCode
     * @param resultMessage
     * @param trace
     * @return
     * @throws RemoteServiceException
     */
    public static RemoteServiceException getRemoteServiceException(Response response, int resultCode, String resultMessage, ServiceErrorTrace trace) throws RemoteServiceException {
        int status = response.status();
        String serviceId = extractServiceId(response.request());
        if (status == 404) {
            return new RemoteServiceException(status, serviceId, FeignExceptionEnum.REQUEST_URI_NOT_FOUNR, null);
        }
        if (status < 200 || status >= 300) {
            return new RemoteServiceException(status, serviceId, FeignExceptionEnum.REMOTE_UNREACHABLE, null);
        }
        if (trace == null) {
            trace = new ServiceErrorTrace(resultCode, resultMessage, serviceId);
        }

        return new RemoteServiceException(status, serviceId, FeignExceptionEnum.REMOTE_SERVICE_THROW_EXCEPTION, trace);
    }

}
