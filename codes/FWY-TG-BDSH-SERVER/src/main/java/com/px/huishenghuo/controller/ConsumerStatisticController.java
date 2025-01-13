package com.px.huishenghuo.controller;

import com.px.huishenghuo.result.Result;
import com.px.huishenghuo.result.ResultType;
import com.px.huishenghuo.vo.ConsumerStatisticVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import com.px.huishenghuo.service.IConsumerStatisticService;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @@author 品讯科技
 * @Description: 用户统计 前端控制器
 * @Date: 2021-07-20
 **/
@RestController
@RequestMapping(value = "/consumerStatistic", name = "用户统计")
@Api(value = "/consumerStatistic", tags = "用户统计")
public class ConsumerStatisticController {
    @Autowired
    private IConsumerStatisticService consumerStatisticService;


    @GetMapping("/admin/conDialect")
    @ApiOperation(value = "用户看板", nickname = "用户看板", httpMethod = "GET", response = Result.class)
    public Result<ConsumerStatisticVO> conDialect() {
        ConsumerStatisticVO consumerStatisticVO = consumerStatisticService.consumerDialect();
        return ResultType.SUCCESS.build().bindData(consumerStatisticVO);
    }


    @GetMapping("/admin/consumerDialect")
    @ApiOperation(value = "用户统计图 type : 1:天 ，2：月；time 月/年", nickname = "用户统计图", httpMethod = "GET", response = Result.class)
    public Result<List<ConsumerStatisticVO>> consumerDialect(@RequestParam int type,
                                                             @RequestParam int time,
                                                             @RequestParam(required = false)@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")LocalDateTime begin,
                                                             @RequestParam(required = false)@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")LocalDateTime end) {
        List<ConsumerStatisticVO> conStatisticList = consumerStatisticService.getConStatisticList(type, time,begin,end);
        return ResultType.SUCCESS.build().bindData(conStatisticList);
    }

    @PostMapping("/admin/exportConsumerExcel")
    @ApiOperation(value = "用户列表导出Excel", nickname = "用户列表导出Excel", httpMethod = "POST", response = Result.class)
    public void exportConsumerExcel(@RequestParam(required = false) String nickName,
                                    @RequestParam(required = false) String phone,
                                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime registerBegin,
                                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime registerEnd,
                                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime loginBegin,
                                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime loginEnd,
                                    HttpServletRequest request, HttpServletResponse response) {
        consumerStatisticService.exportConsumerExcel(nickName, phone, registerBegin, registerEnd, loginBegin, loginEnd, request, response);
    }
}
