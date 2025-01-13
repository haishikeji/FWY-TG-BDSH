package com.px.huishenghuo.wx.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * 〈一句话功能简述〉<br>
 * 〈生成支付订单请求〉
 *
 * @author 品讯科技
 * @create 2021/4/1 11:26
 * @since 1.0.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
//@Accessors(chain = true)
@ApiModel(value = "CommodityOrderRequest数据传输对象", description = "生成支付订单")
public class CommodityOrderRequest {


    @ApiModelProperty(value = "商品id 必传")
    @NotBlank
    private String commodityId;

    @ApiModelProperty(value = "商品规格id 必传")
    @NotBlank
    private String specificationId;

    @ApiModelProperty(value = "商品数量 最小1")
    @Min(1)
    private Integer goodNum;

    @ApiModelProperty(value = "使用开始时间")
    private String useStartTime;

    @ApiModelProperty(value = "使用结束时间")
    private String useEndTime;

    @ApiModelProperty(value = "住几晚")
    private Integer days;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "联系人电话")
    private String contactPhone;

    @ApiModelProperty(value = "联系人")
    private String contactName;

}

