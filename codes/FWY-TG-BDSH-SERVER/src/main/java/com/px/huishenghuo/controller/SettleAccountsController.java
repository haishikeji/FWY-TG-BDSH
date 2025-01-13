package com.px.huishenghuo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.huishenghuo.entity.SettleAccounts;
import com.px.huishenghuo.result.Result;
import com.px.huishenghuo.result.ResultType;
import com.px.huishenghuo.service.ISettleAccountsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @@author 品讯科技
 * @Description: 结算记录表 前端控制器
 * @Date: 2020-01-10
 **/
@RestController
@RequestMapping("/settleAccounts")
@Api(value = "/settleAccounts", tags = "结算记录表")
public class SettleAccountsController {
    @Autowired
    private ISettleAccountsService settleAccountsService;

    @GetMapping("/admin/page")
    @ApiOperation(value = "获取区域信息（分页）", httpMethod = "GET", response = Result.class, notes = "平台管理员登录")
    public Result getPage(@RequestParam(value = "name", required = false) String name,
                          @RequestParam(value = "current", required = false) Long current,
                          @RequestParam(value = "size", required = false) Long pageSize) {
        Page<SettleAccounts> page = new Page<>();
        IPage<SettleAccounts> data = settleAccountsService.page(page);
        return ResultType.SUCCESS.build().bindData(data);
    }

}
