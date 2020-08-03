package com.blackbaka.sc.core.constant;

/**
 * @Author Kai Yi
 * @Date 2020/03/21
 * @Description feign相关配置及常量
 */

public class FeignConsts {


    /**
     * 在Header中标明该feign请求为request body方式，避免被RequestInterceptor转译为form/data
     */
    public final static String HEADER_REQUEST_BODY_KEY = "Request-Body";

    public final static String HEADER_REQUEST_BODY_VALUE = "true";

    public final static String HEADER_REQUEST_BODY = HEADER_REQUEST_BODY_KEY + ": " + HEADER_REQUEST_BODY_VALUE;



}
