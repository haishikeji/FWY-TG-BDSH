package com.px.huishenghuo.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.px.huishenghuo.constant.DeletedEnum;
import com.px.huishenghuo.constant.DistributionLevelEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @@author 品讯科技
 * @Description: entity
 * @Date: 2020-01-14
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="CouponHandlingFee", description="优惠券手续费费率")
@TableName("hccd_coupon_handling_fee")
public class CouponHandlingFee extends BaseEntity {

    @ApiModelProperty(value = "逻辑删除字段")
    @TableField("deleted")
    private DeletedEnum deleted;

    @ApiModelProperty(value = "券手续费")
    @TableField("handling_fee")
    private BigDecimal handlingFee;

    @ApiModelProperty(value = "分销级别")
    @TableField("level")
    private DistributionLevelEnum level;


}