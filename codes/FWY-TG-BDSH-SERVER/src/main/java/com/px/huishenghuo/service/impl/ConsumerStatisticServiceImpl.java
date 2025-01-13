package com.px.huishenghuo.service.impl;

import com.google.common.collect.Lists;
import com.px.huishenghuo.constant.ConsumerTypeEnum;
import com.px.huishenghuo.dto.ConsumerDto;
import com.px.huishenghuo.dto.ConsumerStatisticDTO;
import com.px.huishenghuo.exceptionHandller.BizException;
import com.px.huishenghuo.exceptionHandller.FrozenException;
import com.px.huishenghuo.mapper.curd.ConsumerStatisticCrud;
import com.px.huishenghuo.result.ResultType;
import com.px.huishenghuo.service.IConsumerService;
import com.px.huishenghuo.service.IConsumerStatisticService;
import com.px.huishenghuo.util.ExcelSheetModel;
import com.px.huishenghuo.util.LocalDateTimeUtil;
import com.px.huishenghuo.util.MyExcelUtils;
import com.px.huishenghuo.util.ZipUtils;
import com.px.huishenghuo.vo.ConsumerPageVo;
import com.px.huishenghuo.vo.ConsumerStatisticVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipOutputStream;

/**
 * @@author 品讯科技
 * @Description: 用户统计 服务实现类
 * @Date: 2021-07-20
 **/

@Service
@Slf4j
public class ConsumerStatisticServiceImpl implements IConsumerStatisticService {
    @Autowired
    private ConsumerStatisticCrud crud;

    @Autowired
    private IConsumerService consumerService;

    @Override
    public ConsumerStatisticVO consumerDialect() {
        ConsumerStatisticVO conStatistic = consumerService.getConStatistic();
        return conStatistic;
    }

    @Override
    public List<ConsumerStatisticVO> getConStatisticList(int type, int time,LocalDateTime beginTime,LocalDateTime endTime) {
        if(type == 1){

            LocalDateTime begin = LocalDateTime.of(beginTime.toLocalDate(),LocalTime.MIN);
            LocalDateTime end = LocalDateTime.of(endTime.toLocalDate(),LocalTime.MIN);
            List<ConsumerStatisticDTO> conStaticsticList = crud.getConStaticsticList(begin, end);
            Long days = LocalDateTimeUtil.betweenTwoTime(begin, end, ChronoUnit.DAYS) + 1L;
            List<ConsumerStatisticVO> list = new ArrayList<>(days.intValue());
            for(int i = 1;i<=days;i++){
                ConsumerStatisticVO vo = new ConsumerStatisticVO();
                vo.setTime(i);
                for(ConsumerStatisticDTO dto : conStaticsticList){
                    LocalDateTime createTime = dto.getCreateTime();
                    if(i == createTime.getDayOfMonth()){
                        vo.setTotalNum(dto.getTotalNum());
                        vo.setDistributionNum(dto.getDistributionNum());
                        vo.setMemberNum(dto.getMemberNum());
                        vo.setIncreaseNum(dto.getIncreaseNum());
                    }
                }
                list.add(vo);
            }
            return list;
        }
        if(type == 2){
            //查询月统计
            LocalDateTime of = LocalDateTimeUtil.of(time, 1, 1, 1, 1, 1, 1);
            LocalDateTime yeaOfStart = LocalDateTimeUtil.getYeaOfStart(of);
            LocalDateTime yeaOfLast = LocalDateTimeUtil.getYeaOfLast(of);
            List<ConsumerStatisticDTO> conStaticsticList = crud.getConStaticsticList(yeaOfStart, yeaOfLast);
            List<ConsumerStatisticVO> list = new ArrayList<>(12);
            for(int i = 1;i<=12;i++){
                ConsumerStatisticVO vo = new ConsumerStatisticVO();
                vo.setTime(i);
                int totalNum = 0;

                int increaseNum = 0;

                int memberNum = 0;

                int distributionNum = 0;
                for(ConsumerStatisticDTO dto : conStaticsticList){
                    LocalDateTime createTime = dto.getCreateTime();
                    if (i == createTime.getMonthValue()) {
                        increaseNum = increaseNum + dto.getIncreaseNum();
                        LocalDateTime monthOfLast = LocalDateTimeUtil.getMonthOfLast(createTime);
                        if(createTime.getDayOfMonth() == monthOfLast.getDayOfMonth()){
                            totalNum = dto.getTotalNum();
                            memberNum = dto.getMemberNum();
                            distributionNum = dto.getDistributionNum();
                        }
                    }
                }
                vo.setTotalNum(totalNum);
                vo.setMemberNum(memberNum);
                vo.setDistributionNum(distributionNum);
                list.add(vo);
            }
            return list;
        }
        return new ArrayList<>(0);
    }

    @Override
    public void exportConsumerExcel(String nickName,
                                    String phone,
                                    LocalDateTime registerBegin,
                                    LocalDateTime registerEnd,
                                    LocalDateTime loginBegin,
                                    LocalDateTime loginEnd,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        /*1.获取用户数据*/
        List<ConsumerPageVo> pageByConditionList = consumerService.getPageByConditionList(registerBegin, registerEnd, loginBegin, loginEnd, null, nickName, phone, null, null);
        /*2.列表拆分*/
        List<List<ConsumerPageVo>> lists = splitList(pageByConditionList, 1000);
        /*3.excel封装*/
        try {
            downloadExcel(lists,request,response);
        } catch (IOException e) {
            log.error("导出表格失败：{}",e.getMessage());
            throw new BizException("导出表格失败");
        }

    }

    /**
     * @author 品讯科技
     * @Description 导出Excel
     * @date 2024-08
     * @Param [orderListVOS, request, response]
     * @return void
     **/
    private void downloadExcel(List<List<ConsumerPageVo>> lists,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException {
        ZipOutputStream zos = null;
        try {
            //设置响应格式
            String fileName = "用户汇总";
            String agent = request.getHeader("User-Agent").toLowerCase();
            response.setContentType("application/x-msdownload");
            response.setCharacterEncoding("UTF-8");
            String codedFileName;
            if (agent.contains("firefox")) {
                codedFileName = new String(fileName.getBytes(), "ISO8859-1");
            } else {
                codedFileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            }
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".zip");
            // 处理下载数据
            zos = new ZipOutputStream(response.getOutputStream());
            // 制作总表
            List<ExcelSheetModel> sheets = new ArrayList<>();
            for(int i=0;i<lists.size();i++){
                String name = "用户汇总"+(i+1);
                sheets.add(getModel(lists.get(i),name));
                ByteArrayOutputStream totalBos = MyExcelUtils.getExcelStreamV2(sheets);
                ByteArrayInputStream totalBis = new ByteArrayInputStream(totalBos.toByteArray());
                totalBos.flush();
                totalBos.close();
                ZipUtils.doZip(totalBis, zos, name.concat(".xls"));
            }
        }catch (IOException e) {
            throw new FrozenException(ResultType.FILE_HANDLE_ERROR);
        }finally {
            zos.flush();
            zos.close();
        }
    }

    /**
     * @author 品讯科技
     * @Description 封装Excel数据
     * @date 2024-08
     * @Param [orderListVOS, sheetName]
     * @return com.px.huishenghuo.util.ExcelSheetModel
     **/
    private ExcelSheetModel getModel(List<ConsumerPageVo> consumerListVOS,String sheetName){
        ExcelSheetModel model = new ExcelSheetModel();
        model.setName(sheetName);
        List<String> heads = new ArrayList<>();
        heads.add("用户昵称");
        heads.add("用户电话");
        heads.add("用户身份");
        heads.add("注册时间");
        heads.add("最近登录时间");
        heads.add("下单量");
        heads.add("订单取消量");
        heads.add("推荐分享次数");
        heads.add("推荐分享的下单量");
        model.setHeadNames(heads);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<Map<String, Object>> collect = consumerListVOS.stream().map(vo -> {
            HashMap<String, Object> map = new HashMap<>();
            String name = StringUtils.isBlank(vo.getNickName()) ? "" : vo.getNickName();
            map.put("cell0", name);
            map.put("cell1", vo.getPhone());
            if(vo.getDistributionMember()){
                map.put("cell2", ConsumerTypeEnum.DISTRIBUTION.getStringValue());
            }else{
                if(vo.getUserMember()){
                    map.put("cell2", ConsumerTypeEnum.MEMBER.getStringValue());
                }else{
                    map.put("cell2", ConsumerTypeEnum.USER.getStringValue());
                }
            }
            map.put("cell3", vo.getCreateTime()!=null?dtf.format(vo.getCreateTime()):"");
            map.put("cell4", vo.getLastLoginTime()!=null?dtf.format(vo.getLastLoginTime()):"");
            map.put("cell5", vo.getOrderNum());
            map.put("cell6", vo.getCancelNum());
            map.put("cell7", vo.getShareNum());
            map.put("cell8", vo.getUserAmount().toString());
            return map;
        }).collect(Collectors.toList());
        model.setContents(collect);
        return model;
    }

    /**
     * list拆分
     * @param list list
     * @param size size
     * @return list
     */
    private List<List<ConsumerPageVo>> splitList(List<ConsumerPageVo> list, int size) {
        return Lists.partition(list, size);
    }


    @Override
    public void consumerTodayStatistic() {

        Integer totalNum = 0;
        Integer increaseNum = 0;
        Integer memberNum = 0;
        Integer distributionNum = 0;
        //查询总用户数量
        totalNum = consumerService.getTotalConsumer();
        //查询今日用户
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime begin = LocalDateTime.of(now.toLocalDate(), LocalTime.MIN);
        LocalDateTime end = now.with(LocalTime.parse("23:59:59"));
        List<ConsumerDto> todayConsumer = consumerService.getTodayConsumer(begin, end);
        //当日新增用户
        increaseNum = todayConsumer.size();
        //会员数量
        Long memberNums = todayConsumer.stream().filter(e->e.getUserMember() == true).count();
        memberNum = memberNums.intValue();
        Long distributionNums = todayConsumer.stream().filter(e->e.getDistributionMember() == true).count();
        distributionNum = distributionNums.intValue();
        ConsumerStatisticDTO statistic = new ConsumerStatisticDTO();
        statistic.setTotalNum(totalNum);
        statistic.setIncreaseNum(increaseNum);
        statistic.setMemberNum(memberNum);
        statistic.setDistributionNum(distributionNum);
        crud.addConsumerStatic(statistic);
    }
}

