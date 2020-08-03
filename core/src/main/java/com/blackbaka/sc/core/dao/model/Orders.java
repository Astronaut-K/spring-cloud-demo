package com.blackbaka.sc.core.dao.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author Auto Generate
 * @since 2020-07-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("orders")
@ApiModel(value="Orders对象", description="")
public class Orders extends Model<Orders> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("account_id")
    private Integer accountId;

    @TableField("stock_id")
    private Integer stockId;

    @TableField("count")
    private Integer count;

    @TableField("single_money")
    private Integer singleMoney;

    @TableField("all_money")
    private Integer allMoney;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
