package com.px.huishenghuo.controller.request;

import com.px.huishenghuo.wx.entity.BaseRequestEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *  ConsumerMemberRecordDTO
 *
 * @author 品讯科技
 * @date 2024-08
 **/
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ConsumerMemberRecordRequest", description="会员充值记录")
public class ConsumerMemberRecordRequest extends BaseRequestEntity {

    @ApiModelProperty(value = "充值类型：1.周卡；2.月卡；3.季卡",required = true)
    private Integer type;

    @ApiModelProperty(value = "支付类型：1.微信支付；2.钱包支付",required = true)
    private Integer payMethod;

    @ApiModelProperty(value = "支付时间",required = true)
    private LocalDateTime payTime;

    @ApiModelProperty(value = "支付金额",required = true)
    private BigDecimal payAmount;

    @ApiModelProperty(value = "过期时间",required = true)
    private LocalDateTime expireDate;

    @ApiModelProperty(value = "数据创建账户",required = true)
    private String createUser;

    @ApiModelProperty(value = "数据最后更新账户",required = true)
    private String updateUser;

    @ApiModelProperty(value = "逻辑删除字段(0正常1删除)",required = true)
    private Integer deleted;

    @Override
    public String toString() {
        return super.toString();
    }
}