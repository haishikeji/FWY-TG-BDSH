package com.px.huishenghuo.vo;

import com.px.huishenghuo.constant.DeletedEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author 品讯科技
 * @description 选择条件查询订单的数据封装
 * @datetime 2020-03-26 18:42
 */
@Data
public class OrderConditionVo extends PageVo {

    @ApiModelProperty(value = "订单号")
    private String orderOn;

    @ApiModelProperty(value = "订单状态当order_type为0时： 1：已抢单 2：已拒绝 当order_type为1时： 3：待消费   4：已消费  5：已退款 通用：0：代付款   6：已失效 ")
    private Integer orderCheck;

    @ApiModelProperty(value = "店铺id")
    private String shopId;

    @ApiModelProperty(value = "优惠券主键id")
    private String couponId;

    @ApiModelProperty(value = "消费时间 ")
    private LocalDateTime consumeTime;

    @ApiModelProperty(value = "订单下单平台")
    private Integer orderBuild;

    @ApiModelProperty(value = "下单时间")
    private String consumerOrderTime;

    @ApiModelProperty(value = "下单开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime consumerOrderTimeBegin;

    @ApiModelProperty(value = "下单结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime consumerOrderTimeEnd;

    @ApiModelProperty(value = "消费者id")
    private String consumerId;

    @ApiModelProperty(value = "消费者电话")
    private String phone;

    @ApiModelProperty(value = "审核人员id")
    private String adminId;

    @ApiModelProperty(value = "用户外卖消费订单单号")
    private String consumeOrderNo;

    @ApiModelProperty(value = "逻辑删除字段")
    private DeletedEnum deleted;

}

