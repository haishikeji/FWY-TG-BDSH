package com.px.huishenghuo.dto;

import com.px.huishenghuo.constant.LogTypeEnum;
import com.px.huishenghuo.constant.ResponseEnum;
import lombok.Data;

/**
 * @author ：HQ
 * @date ：Created in 2020/2/4 17:41
 * @description：日志查询条件
 */
@Data
public class LoginLogDto {
    private String userId;
    private String phone;
    private String userName;
    private String beginTime;
    private String endTime;
//    0:成功   1：失败
    private ResponseEnum responseEnum;
//    0：用户  1：商家
    private LogTypeEnum logTypeEnum;
    private Integer page=1;
    private Integer size=10;
}
