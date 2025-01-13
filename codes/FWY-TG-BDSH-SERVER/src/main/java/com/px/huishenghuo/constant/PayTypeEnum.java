package com.px.huishenghuo.constant;

import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 〈一句话功能简述〉<br>
 * 〈支付类型〉
 *
 * @author 品讯科技
 * @create 2020/1/2 18:38
 * @since 1.0.0
 */
public enum PayTypeEnum implements IEnum<Integer>,BaseEnum<String> {

    WECHAT_PAY(0,"微信支付"),
    WALLET_PAY(1,"钱包支付"),
    ;
    @EnumValue
    private Integer code;
    @JSONField(serialzeFeatures = SerializerFeature.WriteEnumUsingToString)
    @JsonValue
    private String msg;

    PayTypeEnum(Integer code, String msg) {
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

    private static PayTypeEnum query(String key) {
        if (NumberUtil.isNumber(key)) {
            PayTypeEnum[] values = PayTypeEnum.values();
            int intKey = Integer.parseInt(key);
            for (PayTypeEnum result : values) {
                if (result.getValue() == intKey) {
                    return result;
                }
            }
        }
        return null;
    }

    public static PayTypeEnum queryByKey(String key) {
        PayTypeEnum result = query(key);
        return result;
    }


}