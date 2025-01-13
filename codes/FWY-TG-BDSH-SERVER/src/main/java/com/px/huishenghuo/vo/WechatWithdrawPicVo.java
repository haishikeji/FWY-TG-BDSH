package com.px.huishenghuo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @@author 品讯科技
 * @Description: vo
 * @Date: 2020-04-11
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "WechatWithdrawPic数据传输对象", description = "")
public class WechatWithdrawPicVo implements Serializable {

    @ApiModelProperty(value = "账户id")
    private String account;

    @ApiModelProperty(value = "账户类型0用户，1商家")
    private Integer accountType;

    @ApiModelProperty(value = "收款二维码")
    private String url;


}