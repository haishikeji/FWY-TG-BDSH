package com.px.huishenghuo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.px.huishenghuo.constant.DeletedEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * CouponTimingRecordsentity
 *
 * @author 品讯科技
 * @date 2024-08
 **/
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@TableName("hccd_coupon_timing_records")
public class CouponTimingRecords implements Serializable {

    /**
     * 优惠券id
     */
    private String couponId;

    /**
     * 发放日期 1.周一；2.周二；3.周三；4.周四；5.周五；6.周六；7.周日 逗号隔开
     */
    private String grantTime;

    /**
     * 数据创建账户
     */
    private String createUser;

    /**
     * 数据最后更新账户
     */
    private String updateUser;

    /**
     * 逻辑删除字段(0正常1删除)
     */
    private DeletedEnum deleted;

    /**
     * id 雪花算法生成
     */
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


    @Override
    public String toString() {
        return super.toString();
    }
}