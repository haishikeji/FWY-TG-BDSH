package com.px.huishenghuo.dto;

import java.math.BigDecimal;

import com.px.huishenghuo.constant.DeletedEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;

/**
 * @@author 品讯科技
 * @Description: ResellDTO
 * @Date: 2019-12-26
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Resell数据传输对象", description="分销类")
public class ResellDto implements Serializable{

    @ApiModelProperty(value = "逻辑删除字段")
    private DeletedEnum deleted;

    @ApiModelProperty(value = "一级分销者id")
    private String userOneId;

    @ApiModelProperty(value = "一级分销者奖金")
    private BigDecimal userOneBonus;

    @ApiModelProperty(value = "二级分销则者id")
    private String userTwoId;

    @ApiModelProperty(value = "二级分销者奖金")
    private BigDecimal userTwoBonus;

    @ApiModelProperty(value = "分销的优惠券id")
    private String couponId;


}