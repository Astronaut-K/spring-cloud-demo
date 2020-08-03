package com.blackbaka.sc.core.config;

import com.blackbaka.sc.core.exception.RemoteServiceException;
import com.blackbaka.sc.core.config.feign.DiyErrorDecoder;
import com.blackbaka.sc.core.config.feign.DiySpringDecoder;
import com.blackbaka.sc.core.config.feign.FeignRequestInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import feign.optionals.OptionalDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * @Author Kaiyi Zhang
 * @Date 2019/07/09
 * @Description feign配置
 */

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnClass(Feign.class)
@AutoConfigureBefore(value = {FeignClientsConfiguration.class})
@EnableFeignClients(basePackages = {"com.blackbaka.sc"})
@Slf4j
public class FeignConfiguration {

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    /**
     * 远程服务调用decode阶段，检测请求结果是否发生错误，并抛出{@link RemoteServiceException}异常
     *
     * @return
     */
    @Bean
    @Primary
    public Decoder feignDecoder() {

        Decoder decoder= new OptionalDecoder(
                new ResponseEntityDecoder(new DiySpringDecoder(this.messageConverters)));
        log.info("Feign Decoder加载完毕.");
        return decoder;
    }

    /**
     * 将ErrorDecoder包装为{@link RemoteServiceException}并抛出
     *
     * @return
     */
    @Bean
    @Primary
    public ErrorDecoder feignErrorDecoder() {
        ErrorDecoder errorDecoder=new DiyErrorDecoder();
        log.info("Feign ErrorDecoder加载完毕.");
        return errorDecoder;
    }



    @Bean
    @Primary
    public FeignRequestInterceptor feignRequestInterceptor(ObjectMapper objectMapper) {
        FeignRequestInterceptor feignRequestInterceptor = new FeignRequestInterceptor(objectMapper);
        log.info("Feign FeignRequestInterceptor加载完毕.");
        return feignRequestInterceptor;
    }

}