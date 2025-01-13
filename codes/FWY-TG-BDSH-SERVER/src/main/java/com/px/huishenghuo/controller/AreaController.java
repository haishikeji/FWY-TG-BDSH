package com.px.huishenghuo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.huishenghuo.entity.Area;
import com.px.huishenghuo.result.Result;
import com.px.huishenghuo.result.ResultType;
import com.px.huishenghuo.service.IAreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @@author 品讯科技
 * @Description: 前端控制器
 * @Date: 2019-12-26
 **/
@RestController
@RequestMapping("/area")
@Api(value = "/area", tags = "区域类")
public class AreaController {

    @Resource
    private IAreaService areaService;

    @GetMapping("/admin/page")
    @ApiOperation(value = "获取区域信息（分页）",httpMethod = "GET", response = Result.class, notes = "平台管理员登录")
    public Result<IPage<Area>> getPage(@RequestParam(value = "name",required = false)String name,
                          @RequestParam(value = "current",required = false)Long current,
                          @RequestParam(value = "size",required = false)Long pageSize){
        Page<Area> page = new Page<>();
        IPage<Area> data = areaService.page(page);
        return ResultType.SUCCESS.build().bindData(data);
    }

}
