package com.px.huishenghuo.constant;

import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 〈一句话功能简述〉<br>
 * 〈店铺类型〉
 *
 * @author 品讯科技
 * @create 2019/12/27 17:09
 * @since 1.0.0
 */
public enum ShopTypeEnum implements IEnum<Integer>,BaseEnum<String> {

    ALL(0,"全部"),
    DINNER(1, "正餐"),
    DRINK(2, "下午茶"),
    PASTRY(3, "小吃"),
    SUPPER(4, "夜宵"),
    ;
    @EnumValue
    private Integer code;
    @JSONField(serialzeFeatures = SerializerFeature.WriteEnumUsingToString)
    @JsonValue
    private String msg;

    ShopTypeEnum(Integer code, String msg) {
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

    private static ShopTypeEnum query(String msg) {
        if (NumberUtil.isNumber(msg)) {
            ShopTypeEnum[] values = ShopTypeEnum.values();
            int intKey = Integer.parseInt(msg);
            for (ShopTypeEnum result : values) {
                if (result.getValue() == intKey) {
                    return result;
                }
            }
        }
        return null;
    }

    public static ShopTypeEnum queryByKey(String msg) {
        ShopTypeEnum result = query(msg);
        return result;
    }
}