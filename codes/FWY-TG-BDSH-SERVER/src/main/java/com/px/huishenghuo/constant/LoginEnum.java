package com.px.huishenghuo.constant;

import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 〈一句话功能简述〉<br>
 * 〈登录方式枚举〉
 *
 * @author 品讯科技
 * @create 2020/1/7 14:35
 * @since 1.0.0
 */
public enum LoginEnum implements IEnum<Integer>,BaseEnum<String> {
    PHONE_LOGIN(0,"手机登录"),
    PASSWORD_LOGIN(1,"账号密码登录"),
    WX_LOGIN(2,"微信授权登录"),;
    @EnumValue
    private Integer code;
    @JSONField(serialzeFeatures= SerializerFeature.WriteEnumUsingToString)
    @JsonValue
    private String msg;

    LoginEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public Integer getValue() {
        return this.code;
    }


    @Override
    public String toString() {
        return this.msg;
    }
    @Override
    public String getStringValue() {
        return this.msg;
    }

    private static LoginEnum query(String key) {
        if (NumberUtil.isNumber(key)) {
            LoginEnum[] values = LoginEnum.values();
            int intKey = Integer.parseInt(key);
            for (LoginEnum result : values) {
                if (result.getValue() == intKey) {
                    return result;
                }
            }
        }
        return null;
    }

    public static LoginEnum queryByKey(String key) {
        LoginEnum result = query(key);
        return result;
    }

}

