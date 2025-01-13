package com.px.huishenghuo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author 品讯科技
 * @description 店铺经纬度信息
 * @datetime 2020-04-10 16:42
 */
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "Shop", description = "商家外卖配送地址")
public class ShopCoordinatesDto {

    @ApiModelProperty(value = "经度")
    private BigDecimal lng;

    @ApiModelProperty(value = "纬度")
    private BigDecimal lat;
}

