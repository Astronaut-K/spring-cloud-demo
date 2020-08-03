package com.blackbaka.sc.core.config.feign;

import com.blackbaka.sc.core.exception.FeignExceptionEnum;
import com.blackbaka.sc.core.exception.RemoteServiceException;
import com.blackbaka.sc.core.util.FeignUtil;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author Kai Yi
 * @Date 2019/12/25
 * @Description 远程服务接口无法访问时进行异常抛出
 */

@Slf4j
public class DiyErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        log.error("Http status code: " + response.status() + ", reason: " + response.reason()
                + ". Url: " + response.request().url() + ". Request: " + response.request().toString());
        throw new RemoteServiceException(response.status(), FeignUtil.extractServiceId(response.request()), FeignExceptionEnum.REMOTE_UNREACHABLE, null);
    }

}
