package com.px.huishenghuo.wx.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈订阅消息dto〉
 *
 * @author 品讯科技
 * @create 2020/7/8 20:23
 * @since 1.0.0
 */
@Getter
@Setter
public class SubscribeMsgDto {


    /*接收方openid*/
    private String userOpenId;

    /*发送消息内容*/
    private Map<String, Map<String, String>> jsonStr;

    /*模板类型*/
    private String tempType;
}

