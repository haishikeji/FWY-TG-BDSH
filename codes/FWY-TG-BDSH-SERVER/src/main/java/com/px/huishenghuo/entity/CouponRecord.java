package com.px.huishenghuo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.px.huishenghuo.constant.DeletedEnum;
import com.px.huishenghuo.constant.DisableEnum;
import com.px.huishenghuo.constant.RecordTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @@author 品讯科技
 * @Description: entity
 * @Date: 2020-01-14
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="CouponRecord", description="优惠券创建记录表")
@TableName("hccd_coupon_record")
public class CouponRecord extends BaseEntity {

    @ApiModelProperty(value = "逻辑删除字段")
    @TableField("deleted")
    private DeletedEnum deleted;

    @ApiModelProperty(value = "锁版本")
    @Version
    private Integer version;

    @ApiModelProperty(value = "优惠券主键id")
    @TableField("coupon_id")
    private String couponId;

    @ApiModelProperty(value = "发放时间")
    @TableField("grant_time")
    private String grantTime;

    @ApiModelProperty(value = "领取数量")
    @TableField("receive")
    private Integer receive;

    @ApiModelProperty(value = "剩余数量")
    @TableField("remainder")
    private Integer remainder;

    @ApiModelProperty(value = "发放总量")
    @TableField("total_num")
    private Integer totalNum;

    @ApiModelProperty(value = "记录是否有效")
    @TableField("invalid")
    private DisableEnum invalid;

    @ApiModelProperty(value = "上架记录时间")
    @TableField("putaway_time")
    private LocalDateTime putawayTime;

    @ApiModelProperty(value = "冻结金额")
    @TableField("freeze")
    private BigDecimal freeze;

    @ApiModelProperty(value = "手续费")
    @TableField("fee")
    private BigDecimal fee;

    @ApiModelProperty(value = "结算金额")
    @TableField("settlement")
    private BigDecimal settlement;

    @ApiModelProperty(value = "结算金额")
    @TableField("record_type")
    private RecordTypeEnum recordType;

    @ApiModelProperty(value = "领取用户id")
    @TableField("consumer_id")
    private String consumerId;

    public CouponRecord() {
    }

    public CouponRecord(String couponId,
                        String grantTime,
                        Integer receive,
                        Integer remainder,
                        Integer totalNum,
                        DisableEnum invalid,
                        LocalDateTime putawayTime) {
        this.couponId = couponId;
        this.grantTime = grantTime;
        this.receive = receive;
        this.remainder = remainder;
        this.totalNum = totalNum;
        this.invalid = invalid;
        this.putawayTime = putawayTime;
    }
}