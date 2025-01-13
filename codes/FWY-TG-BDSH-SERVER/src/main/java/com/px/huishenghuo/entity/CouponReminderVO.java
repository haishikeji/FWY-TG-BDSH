package com.px.huishenghuo.entity;

import com.px.huishenghuo.wx.entity.BaseVoEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * CouponReminderVO
 *
 * @author 品讯科技
 * @date 2024-08
 **/
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="CouponReminderVO", description="定时提醒领券")
public class CouponReminderVO extends BaseVoEntity {

    @ApiModelProperty(value = "是否提醒")
    private Boolean isReminder;

    @ApiModelProperty(value = "每天提醒时间")
    private LocalDateTime time;

    @ApiModelProperty(value = "数据创建账户")
    private String createUser;

    @ApiModelProperty(value = "数据最后更新账户")
    private String updateUser;

    @ApiModelProperty(value = "逻辑删除字段(0正常1删除)")
    private Integer deleted;

    @Override
    public String toString() {
        return super.toString();
    }
}