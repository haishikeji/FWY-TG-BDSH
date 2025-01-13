package com.px.huishenghuo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @@author 品讯科技
 * @Description: AdminDTO
 * @Date: 2020-04-11
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="UserPassDto", description="用户密码请求")
public class UserPassDto implements Serializable{

    @ApiModelProperty(value = "密码")
    @NotBlank
    private String pwd;

    @ApiModelProperty(value = "验证码")
    @NotBlank
    private String code;
}