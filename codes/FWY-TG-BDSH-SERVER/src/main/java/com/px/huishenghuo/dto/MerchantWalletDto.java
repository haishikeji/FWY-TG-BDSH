package com.px.huishenghuo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @@author 品讯科技
 * @Description: MerchantWalletDTO
 * @Date: 2020-03-26
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "MerchantWallet数据传输对象", description = "商户钱包表")
public class MerchantWalletDto implements Serializable {

    @ApiModelProperty(value = "商家账户余额")
    private BigDecimal amount;

    @ApiModelProperty(value = "商家账户冻结金额")
    private BigDecimal freeze;

    @ApiModelProperty(value = "版本控制")
    private Integer version;

    @ApiModelProperty(value = "商家账户ID")
    private String account;

    @ApiModelProperty(value = "支付密码")
    private String pwd;


}