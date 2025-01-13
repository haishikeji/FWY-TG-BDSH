package com.px.huishenghuo.vo;

import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;

/**
 * @@author 品讯科技
 * @Description: vo
 * @Date: 2020-01-14
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CouponHandlingFeeVo implements Serializable{

    private BigDecimal handlingFee;

    private Integer level;


}