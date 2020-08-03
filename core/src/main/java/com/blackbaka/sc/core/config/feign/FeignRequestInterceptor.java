package com.blackbaka.sc.core.config.feign;

import com.blackbaka.sc.core.constant.ConfigConsts;
import com.blackbaka.sc.core.constant.FeignConsts;
import com.blackbaka.sc.core.context.UserContext;
import com.blackbaka.sc.core.context.UserInfo;
import com.blackbaka.sc.core.exception.FeignExceptionEnum;
import com.blackbaka.sc.core.exception.ServiceException;
import com.blackbaka.sc.core.util.HeaderUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.util.*;

/**
 * @Author Kaiyi Zhang
 * @Date 2019/07/09
 * @Description
 */

@Slf4j
public class FeignRequestInterceptor implements RequestInterceptor, ApplicationListener<EnvironmentChangeEvent> {

    @Value("${" + ConfigConsts.Auth.SERVICE_TOKEN_KEY + ":" + ConfigConsts.Auth.SERVICE_TOKEN_DEFAULT_VALUE + "}")
    private String serviceToken;

    @Autowired
    private ApplicationContext applicationContext;

    private ObjectMapper objectMapper;

    public FeignRequestInterceptor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onApplicationEvent(EnvironmentChangeEvent event) {
        if (event.getKeys() == null || event.getKeys().isEmpty()) {
            return;
        }

        Environment environment = applicationContext.getEnvironment();
        Set<String> envKeys = event.getKeys();

        if (envKeys.contains(ConfigConsts.Auth.SERVICE_TOKEN_KEY)) {
            String oldST = serviceToken;
            synchronized (this) {
                serviceToken = environment.getProperty(ConfigConsts.Auth.SERVICE_TOKEN_KEY, String.class, ConfigConsts.Auth.SERVICE_TOKEN_DEFAULT_VALUE);
            }
            log.info("Service Token已变更");
            log.debug(String.format("旧Service Token: %s,新Service Token: %s ", oldST, serviceToken));
        }

    }

    @Override
    @SuppressWarnings("deprecation")
    public void apply(RequestTemplate template) {
        // 1.服务间调用，传递鉴权用服务token
        template.header(ConfigConsts.Auth.SERVICE_TOKEN_KEY, serviceToken);
        // 2.服务间调用，传递当前登录的用户信息
        UserInfo userInfo = UserContext.get();
        if (userInfo != null && userInfo.getUserId() != null) {
            try {
                template.header(ConfigConsts.Auth.USER_INFO_KEY, HeaderUtil.urlEncode(objectMapper.writeValueAsString(userInfo)));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("UserInfo对象Json序列化错误，" + e.getMessage(), e);
            }
        }
        //
    }

    /**
     * 将request body 中的json 转为 form/data query
     * 目前已经可以在参数上添加注解{@link org.springframework.cloud.openfeign.SpringQueryMap} 代替
     */
    @Deprecated
    private void transferBodyToFormData(RequestTemplate template) {

        // 是否满足request body转form/data的条件
        if (!containsHeader(template.headers(), FeignConsts.HEADER_REQUEST_BODY_KEY, FeignConsts.HEADER_REQUEST_BODY_VALUE)
                && "GET".equals(template.method()) && template.requestBody().asBytes() != null) {
            // feign 不支持 GET 方法传 POJO, 需要将request body 中的json 转为 form/data query
            try {
                JsonNode jsonNode = objectMapper.readTree(template.requestBody().asBytes());
                String nullStr = null;
                template.body(nullStr);

                Map<String, Collection<String>> queries = new HashMap<>();
                buildQuery(jsonNode, "", queries);
                template.queries(queries);
            } catch (IOException e) {
                throw new ServiceException(FeignExceptionEnum.REQUEST_PARAM_BUILD_ERROR, e);
            }
        }
    }

    /**
     * json转为Map
     *
     * @param jsonNode
     * @param path     json的根key
     * @param queries  map
     */
    private void buildQuery(JsonNode jsonNode, String path, Map<String, Collection<String>> queries) {
        if (!jsonNode.isContainerNode()) {   // 叶子节点
            if (jsonNode.isNull()) {
                return;
            }
            Collection<String> values = queries.get(path);
            if (values == null) {
                values = new ArrayList<>();
                queries.put(path, values);
            }
            values.add(jsonNode.asText());
            return;
        }
        if (jsonNode.isArray()) {   // 数组节点
            Iterator<JsonNode> it = jsonNode.elements();
            while (it.hasNext()) {
                buildQuery(it.next(), path, queries);
            }
        } else {
            Iterator<Map.Entry<String, JsonNode>> it = jsonNode.fields();
            while (it.hasNext()) {
                Map.Entry<String, JsonNode> entry = it.next();
                if (StringUtils.isNotBlank(path)) {
                    buildQuery(entry.getValue(), path + "." + entry.getKey(), queries);
                } else {  // 根节点
                    buildQuery(entry.getValue(), entry.getKey(), queries);
                }
            }
        }
    }

    /**
     * header是否存在
     *
     * @param key
     * @param value
     * @return
     */
    private boolean containsHeader(Map<String, Collection<String>> headers, String key, String value) {
        if (headers == null) {
            return false;
        }
        Collection<String> values = headers.get(key);
        if (values == null || values.isEmpty()) {
            return false;
        }
        return values.contains(value);
    }


}