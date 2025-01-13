package com.px.huishenghuo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.px.huishenghuo.constant.DeletedEnum;
import com.px.huishenghuo.dto.CouponTimingRecordsDTO;
import com.px.huishenghuo.entity.Coupon;
import com.px.huishenghuo.entity.CouponRecord;
import com.px.huishenghuo.entity.CouponTimingRecords;
import com.px.huishenghuo.mapper.curd.CouponTimingRecordsCrud;
import com.px.huishenghuo.service.CouponTimingRecordsService;
import com.px.huishenghuo.service.ICouponRecordService;
import com.px.huishenghuo.service.ICouponService;
import com.px.huishenghuo.util.BeanUtil;
import com.px.huishenghuo.wx.exception.BizException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 定时发放优惠券配置 服务实现类
 *
 * @author 品讯科技
 * @date 2024-08
 **/
@Service
public class CouponTimingRecordsServiceImpl implements CouponTimingRecordsService {

    @Resource
    private CouponTimingRecordsCrud crud;

    @Resource
    private ICouponService couponService;

    @Resource
    private ICouponRecordService recordService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean timimgRecord(CouponTimingRecordsDTO dto) {
        /*1.优惠券开启定时发放*/
        couponService.timeGrant(dto.getCouponId(),true);
        /*2.添加优惠券定时发放记录*/
        CouponTimingRecords records = BeanUtil.convert(dto, CouponTimingRecords.class);
        crud.addOrUpdateCouponTimingRecord(records);
        /*3.更新发放记录 无效之前的发放记录， */
        List<String> ids = new ArrayList<>(1);
        ids.add(dto.getCouponId());
        recordService.updateOldCouponRecord(ids);
        /*4.添加本周发放记录*/
        //查询优惠券
        Coupon couponInfo = couponService.getOne(new LambdaQueryWrapper<Coupon>().
                eq(Coupon::getId,dto.getCouponId()).
                eq(Coupon::getDeleted, DeletedEnum.NORMAL.getCode()));
        if(Objects.isNull(couponInfo)){
            throw new BizException("该优惠券不存在");
        }
        List<Coupon> coupons = new ArrayList<>(1);
        coupons.add(couponInfo);
        CouponTimingRecords timingRecords = new CouponTimingRecords();
        BeanUtils.copyProperties(dto, timingRecords);
        List<CouponTimingRecords> list = new ArrayList<>(1);
        list.add(timingRecords);
        List<CouponRecord> newCouponRecords = recordService.createNewCouponRecords(coupons, list);
        /*5.创建发放记录*/
        return recordService.addCouponRecords(newCouponRecords);
    }

    @Override
    public List<CouponTimingRecords> getCouponTimingRecords() {
        return crud.getCouponTimingRecords();
    }
}