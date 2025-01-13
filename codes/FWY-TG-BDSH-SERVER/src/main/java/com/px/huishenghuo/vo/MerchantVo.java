package com.px.huishenghuo.vo;

import com.px.huishenghuo.constant.GenderEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @@author 品讯科技
 * @Description: vo
 * @Date: 2020-03-13
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Merchant数据传输对象", description = "商家账户信息")
public class MerchantVo implements Serializable {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "商家电话")
    private String merchantPhone;

    @ApiModelProperty(value = "微信用户openid")
    private String openId;

    @ApiModelProperty(value = "是否冻结")
    private Integer isFreeze;

    @ApiModelProperty(value = "省份")
    private String province;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "国籍")
    private String country;

    @ApiModelProperty(value = "账户昵称，如果微信授权，可以直接用微信昵称")
    private String nickName;

    @ApiModelProperty(value = "头像")
    private String icon;

    @ApiModelProperty(value = "生日")
    private String birthday;

    @ApiModelProperty(value = "用户性别")
    private GenderEnum gender;

    @ApiModelProperty(value = "商家账号")
    private String account;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "商家联系电话")
    private String tel;

    @ApiModelProperty(value = "商户类型 1：商家  2：代理商")
    private Integer merchantType;

    @ApiModelProperty(value = "关联商家数量")
    private Integer merchantNum;

    @ApiModelProperty(value = "提成标准")
    private String commission;

    @ApiModelProperty(value = "商家关联代理商id")
    private String associationId;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "角色绑定")
    private String roleId;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "店铺对象")
    private ShopVo vo;
}