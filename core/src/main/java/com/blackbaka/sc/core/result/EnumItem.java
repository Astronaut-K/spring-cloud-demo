package com.blackbaka.sc.core.result;

import com.blackbaka.sc.core.util.EnumUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/***
 *对前端的Select组件 中的备选 Option数据进行了封装。可以使用 com.wochanye.api.util.EnumUtil.getOptions(BaseEnum)
 * 方法，传入枚举类，获得List<EnumItem> 对象。获得List<EnumItem> 对象，方便前端获取。
 *
 * 例如，在Controller获取Options的方法中，可以使用以下代码。：
 *
 *List<EnumItemDto> options = EnumUtil.getOptions(SubEnum.class);
 *return ResponseUtil.getInfoListSuccess(options);
 *
 *@author Thinker Wu
 *@date 2019/4/23 16:10
 * @see EnumUtil
 */
public class EnumItem {

    /**
     * 值属性值
     */
    private Object value;

    /**
     * 标签-前端显示
     */
    private String label;

    /**
     * 父属性值，0为顶层。分级Select的时候使用
     */
    @JsonIgnore
    private Object parentValue;

    /**
     * 下级option.分级的时候使用
     */
    private List<EnumItem> children;

    public EnumItem() {
    }

    public EnumItem(Object value, String label) {
        this.value = value;
        this.label = label;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Object getParentValue() {
        return parentValue;
    }

    public void setParentValue(Object parentValue) {
        this.parentValue = parentValue;
    }

    public List<EnumItem> getChildren() {
        return children;
    }

    public void setChildren(List<EnumItem> children) {
        this.children = children;
    }
}