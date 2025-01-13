package com.px.huishenghuo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.px.huishenghuo.constant.CouponTypeEnum;
import com.px.huishenghuo.constant.DeletedEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @@author 品讯科技
 * @Description: entity
 * @Date: 2020-02-13
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Category", description="")
@TableName("hccd_category")
public class Category extends BaseEntity {

    @ApiModelProperty(value = "逻辑删除字段")
    @TableField("deleted")
    private DeletedEnum deleted;

    @ApiModelProperty(value = "品类名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "品类图标")
    @TableField("img_url")
    private String imgUrl;

    @ApiModelProperty(value = "品类类型  0：团购，  1：外卖")
    @TableField("type")
    private CouponTypeEnum type;

    @ApiModelProperty(value = "品类父id")
    @TableField("parentId")
    private String parentId;


}