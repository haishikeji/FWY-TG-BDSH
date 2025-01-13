package com.px.huishenghuo.constant;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.enums.IEnum;

/**
 * 〈一句话功能简述〉<br>
 * 〈支付记录枚举〉
 *
 * @author 品讯科技
 * @create 2020/1/6 15:56
 * @since 1.0.0
 */
public enum PayRecordEnum implements IEnum<Integer>,BaseEnum<String>{

    WX_PAY(10001, "微信支付"),
    WALLET_PAY(10002,"钱包支付"),
    ALP_PAY(10003, "支付宝支付"),
    WX_ENT_PAY(10004,"微信企业支付"),

    PAY_REQUEST_SUCCESS(4001,"发起支付请求成功"),
    //支付失败
    PAY_REQUEST_FAIL(4002,"发起支付请求失败"),

    AUTHORIZE_CODE_NULL(5001,"微信授权code为空"),
    AUTHORIZE_ACCESS_TOKEN_NULL(5002,"微信授权accessToken为空"),

    WX_PAID(20001,"微信已支付"),
    WX_PAY_FAIL(20002,"微信支付失败"),
    WX_REFUNDED(20003,"微信已退款"),
    WX_REFUNDED_FAIL(20004,"微信退款失败"),
    ;
    private Integer code;
    private String msg;

    PayRecordEnum(Integer code, String msg){
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

    private static PayRecordEnum query(String msg) {
        if (NumberUtil.isNumber(msg)) {
            PayRecordEnum[] values = PayRecordEnum.values();
            int intKey = Integer.parseInt(msg);
            for (PayRecordEnum result : values) {
                if (result.getValue() == intKey) {
                    return result;
                }
            }
        }
        return null;
    }

    public static PayRecordEnum queryByKey(String msg) {
        PayRecordEnum result = query(msg);
        return result;
    }
}