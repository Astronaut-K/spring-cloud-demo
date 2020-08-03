package com.blackbaka.sc.core.enums;


import com.blackbaka.sc.core.util.EnumUtil;

/**
 * 服务枚举接口
 *
 * @author wuss45
 * @date 2018年10月19日-11时22分
 * @see {@link EnumUtil}
 */
public interface ServiceEnum<T> {


    /**
     * 获取编码值
     * <p>
     *
     * @return
     */
    T getValue();

    /**
     * 获取文本描述
     * <p>
     *
     * @return
     */
    String getLabel();


}
