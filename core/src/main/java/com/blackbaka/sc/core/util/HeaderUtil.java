package com.blackbaka.sc.core.util;

import com.blackbaka.sc.core.exception.ServiceErrorTrace;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @Author Kaiyi Zhang
 * @Date 2019/06/26
 * @Description
 */

public class HeaderUtil {

    private final static Logger log = LoggerFactory.getLogger(HeaderUtil.class);

    private final static ObjectMapper objectMapper = new ObjectMapper();


    /**
     * URL encode
     *
     * @param str
     * @return
     */
    public static String urlEncode(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        String encodeStr = null;
        try {
            encodeStr = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return encodeStr;
    }


    /**
     * URL decode
     *
     * @param str
     * @return
     */
    public static String urlDecode(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        String decodeStr = null;
        try {
            decodeStr = URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return decodeStr;
    }


    /**
     * 将str经过URL decode之后反序列化成ServiceErrorTrace
     *
     * @param str
     * @return
     */
    public static ServiceErrorTrace decodeErrorTrace(String str) {
        ServiceErrorTrace errorTrace = null;
        try {
            String json = urlDecode(str);
            if (StringUtils.isBlank(json)) {
                return null;
            }
            errorTrace = objectMapper.readValue(json, ServiceErrorTrace.class);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return errorTrace;
    }


    /**
     * 将ServiceErrorTrace序列化为json后进行URL encode
     *
     * @param errorTrace
     * @return
     */
    public static String encodeErrorTrace(ServiceErrorTrace errorTrace) {
        if (errorTrace == null) {
            return null;
        }
        String str = null;
        try {
            String json = objectMapper.writeValueAsString(errorTrace);
            str = urlEncode(json);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return str;
    }

}
