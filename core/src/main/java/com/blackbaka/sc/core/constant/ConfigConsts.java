package com.blackbaka.sc.core.constant;

/**
 * 系统配置常量定义
 *
 * @author cuijie
 * @since 2018-06-27
 */
public class ConfigConsts {

    /**
     * 默认字符分割
     */
    public static final String DEFAULT_SPLIT_SYMBOL = ",";

    /**
     * / 分割符
     */
    public static final String DIVIDE_SHOW_SPLIT_SYMBOL = "/";

    /**
     * ; 分割符
     */
    public static final String SEMICOLON_SHOW_SPLIT_SYMBOL = ";";

    /**
     * html换行符
     */
    public static final String HTML_CR = "<br/>";

    public interface Auth {

        // request header 中用户鉴权参数key
        String USER_TOKEN_KEY = "token";

        String USER_TOKEN_EXPIRE_KEY = "user_token_expire_at";

        // 用户token中用户鉴权参数key
        String USER_TOKEN_CALIM_ID_KEY = "id";

        String USER_TOKEN_CLAIM_EXPIRE_KEY = "expire_at";

        String USER_INFO_KEY = "user_info";

        // request header中服务鉴权参数key
        String SERVICE_TOKEN_KEY = "service_token";

        // request header中服务鉴权默认value
        String SERVICE_TOKEN_DEFAULT_VALUE = "default_wochanye_service_token";

    }

    /**
     * 图表统一输出
     */
    public interface Chart {
        /**
         * 柱状图、折线图的横轴、饼图的label等
         */
        String X_AXIS_NAME = "label";

        /**
         * 柱状图、折线图、饼图等的数据
         */
        String Y_AXIS_DATA = "data";

        /**
         * 图上的第二个数据 - 折线
         */
        String Y_AXIS_LINE_DATA = "lineData";

        /**
         * 图上的第二个数据 - 子（从）数据
         */
        String Y_AXIS_CHILD_DATA = "childData";

        /**
         * 数据点的名字
         */
        String POINT_NAME = "name";

        /**
         * 数据点的数值
         */
        String POINT_VALUE = "value";
    }

}
