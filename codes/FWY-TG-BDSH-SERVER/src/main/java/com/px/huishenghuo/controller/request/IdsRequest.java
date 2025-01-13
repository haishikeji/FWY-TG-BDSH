package com.px.huishenghuo.controller.request;


import com.google.gson.Gson;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 品讯科技
 * @create 2021/4/28 20:45
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class IdsRequest implements Serializable {

    @ApiModelProperty("提现订单id")
    @NotNull
    private List<String> ids;

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}

