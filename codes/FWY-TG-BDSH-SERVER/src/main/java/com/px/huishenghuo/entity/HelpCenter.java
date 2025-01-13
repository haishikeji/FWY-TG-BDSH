package com.px.huishenghuo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.px.huishenghuo.constant.DeletedEnum;
import com.px.huishenghuo.constant.HelpCenterEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @@author 品讯科技
 * @Description: entity
 * @Date: 2020-01-02
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="HelpCenter", description="常见问题表")
@TableName("hccd_help_center")
public class HelpCenter extends BaseEntity {

    @ApiModelProperty(value = "逻辑删除字段")
    @TableField("deleted")
    private DeletedEnum deleted;

    @ApiModelProperty(value = "问题描述")
    @TableField("question")
    private String question;

    @ApiModelProperty(value = "解决方案")
    @TableField("answer")
    private String answer;

    @ApiModelProperty(value = "类别")
    @TableField("type")
    private HelpCenterEnum type;
}