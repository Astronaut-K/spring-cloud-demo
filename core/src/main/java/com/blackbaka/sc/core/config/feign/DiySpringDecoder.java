package com.blackbaka.sc.core.config.feign;

import com.blackbaka.sc.core.result.AbstractResult;
import com.blackbaka.sc.core.util.FeignUtil;
import feign.FeignException;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.HttpMessageConverterExtractor;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * @Author Kai Yi
 * @Date 2019/12/22
 * @Description 添加 捕获远程服务接口返回错误并抛出RemoteServiceException 的功能，copy from {@link SpringDecoder}
 */

public class DiySpringDecoder implements Decoder {

    private ObjectFactory<HttpMessageConverters> messageConverters;

    public DiySpringDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        this.messageConverters = messageConverters;
    }

    @Override
    public Object decode(final Response response, Type type)
            throws IOException, FeignException {

        if (type instanceof Class || type instanceof ParameterizedType
                || type instanceof WildcardType) {
            @SuppressWarnings({"unchecked", "rawtypes"})
            HttpMessageConverterExtractor<?> extractor = new HttpMessageConverterExtractor(
                    type, this.messageConverters.getObject().getConverters());

            Object o = extractor.extractData(new FeignResponseAdapter(response));
            // 根据result类型及success字段判断是否需要抛出远程服务调用异常
            if (o instanceof AbstractResult) {
                AbstractResult result = (AbstractResult) o;
                if (!result.isSuccess()) {
                    FeignUtil.throwRemoteServiceException(response, result.getCode(), result.getMessage(), result.getWochanyeErrorTrace());
                }
            }
            return o;
        }

        throw new DecodeException(response.status(),
                "type is not an instance of Class or ParameterizedType: " + type,
                response.request());
    }


    private final class FeignResponseAdapter implements ClientHttpResponse {

        private final Response response;

        private FeignResponseAdapter(Response response) {
            this.response = response;
        }

        @Override
        public HttpStatus getStatusCode() throws IOException {
            return HttpStatus.valueOf(this.response.status());
        }

        @Override
        public int getRawStatusCode() throws IOException {
            return this.response.status();
        }

        @Override
        public String getStatusText() throws IOException {
            return this.response.reason();
        }

        @Override
        public void close() {
            try {
                this.response.body().close();
            } catch (IOException ex) {
                // Ignore exception on close...
            }
        }

        @Override
        public InputStream getBody() throws IOException {
            return this.response.body().asInputStream();
        }

        @Override
        public HttpHeaders getHeaders() {
            return getHttpHeaders(this.response.headers());
        }

        /**
         * copy from {@code org.springframework.cloud.openfeign.support.FeignUtils#getHttpHeaders(java.util.Map)}
         *
         * @param headers
         * @return
         */
        private HttpHeaders getHttpHeaders(Map<String, Collection<String>> headers) {
            HttpHeaders httpHeaders = new HttpHeaders();
            for (Map.Entry<String, Collection<String>> entry : headers.entrySet()) {
                httpHeaders.put(entry.getKey(), new ArrayList<>(entry.getValue()));
            }
            return httpHeaders;
        }
    }


}
