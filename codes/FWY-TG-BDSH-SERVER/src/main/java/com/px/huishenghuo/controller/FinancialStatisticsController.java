package com.px.huishenghuo.controller;

import com.px.huishenghuo.result.Result;
import com.px.huishenghuo.service.IFinancialStatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author ：HQ
 * @date ：Created in 2020/4/9 15:40
 * @description：平台端财务统计
 */
@RestController
@RequestMapping("/financialStatistics")
@Api(value = "/financialStatistics", tags = "财务统计")
public class FinancialStatisticsController {
    @Resource
    private IFinancialStatisticsService iFinancialStatisticsService;

    @GetMapping("/admin/financial")
    @ApiOperation(value = "财务统计", nickname = "财务统计", httpMethod = "GET", response = Result.class)
    public Result investMoney(Integer year, Integer month) {
        return iFinancialStatisticsService.financial(year, month);
    }

    @GetMapping("/admin/recharge")
    @ApiOperation(value = "充值统计", nickname = "充值统计", httpMethod = "GET", response = Result.class)
    public Result recharge(Integer year, Integer month) {
        return iFinancialStatisticsService.charge(year, month);
    }

    @GetMapping("/admin/cashOut")
    @ApiOperation(value = "提现统计", nickname = "提现统计", httpMethod = "GET", response = Result.class)
    public Result cashOut(Integer year, Integer month) {
        return iFinancialStatisticsService.withdraw(year, month);
    }

    @GetMapping("/admin/collectMoney")
    @ApiOperation(value = "应收款统计", nickname = "应收款统计", httpMethod = "GET", response = Result.class)
    public Result collectMoney(Integer year, Integer month) {
        return iFinancialStatisticsService.entrustCharge(year, month);
    }
}
