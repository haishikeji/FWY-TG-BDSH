package com.px.huishenghuo.controller.request;

import com.px.huishenghuo.constant.DeletedEnum;
import com.px.huishenghuo.wx.entity.BaseRequestEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

/**
 * @@author 品讯科技
 * @Description: ConsumerStatisticDTO
 * @Date: 2021-07-20
 **/
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ConsumerStatisticRequest数据传输对象", description="用户统计")
public class ConsumerStatisticRequest extends BaseRequestEntity {

    @ApiModelProperty(value = "逻辑删除字段(0正常1删除)",required = false)
    private DeletedEnum deleted;

    @ApiModelProperty(value = "所有用户注册数量",required = false)
    private Integer totalNum;

    @ApiModelProperty(value = "新增用户数量",required = false)
    private Integer increaseNum;

    @ApiModelProperty(value = "会员用户数量",required = false)
    private Integer memberNum;

    @ApiModelProperty(value = "分销员用户数量",required = false)
    private Integer distributionNum;


}