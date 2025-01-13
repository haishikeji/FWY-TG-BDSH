package com.px.huishenghuo.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.huishenghuo.constant.*;
import com.px.huishenghuo.dto.*;
import com.px.huishenghuo.entity.*;
import com.px.huishenghuo.exceptionHandller.BizException;
import com.px.huishenghuo.exceptionHandller.FrozenException;
import com.px.huishenghuo.mapper.CouponMapper;
import com.px.huishenghuo.mapper.CouponRecordMapper;
import com.px.huishenghuo.mapper.ShopCoordinateMapper;
import com.px.huishenghuo.mapper.ShopMapper;
import com.px.huishenghuo.result.Result;
import com.px.huishenghuo.result.ResultType;
import com.px.huishenghuo.service.ICouponRecordService;
import com.px.huishenghuo.service.ICouponService;
import com.px.huishenghuo.service.IMerchantService;
import com.px.huishenghuo.util.GeoUtils;
import com.px.huishenghuo.util.LocalDateTimeUtil;
import com.px.huishenghuo.vo.CouponAddVo;
import com.px.huishenghuo.vo.CouponPageVO;
import com.px.huishenghuo.vo.CouponVo;
import com.px.huishenghuo.vo.merchant.CouponStatisticVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @@author 品讯科技
 * @Description: 优惠券表 服务实现类
 * @Date: 2019-12-26
 **/
@Service
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements ICouponService {
    @Resource
    private ShopMapper shopMapper;

    @Resource
    private CouponRecordMapper recordMapper;

    @Resource
    private ShopCoordinateMapper coordinateMapper;

    @Resource
    private ICouponRecordService recordService;

    @Resource
    private IMerchantService merchantService;

    @Override
    public boolean saveBatch(Collection<Coupon> entityList) {
        return false;
    }

    @Override
    @Transactional
    public Result deleteCoupon(String couponId) {
        Coupon coupon = baseMapper.selectById(couponId);
        if (CouponStatusEnum.OFF_SALE.getValue() != coupon.getCouponStatus().getValue()) {
            return ResultType.COUPON_ON_SALE.build();
        }
        if (null != coupon) {
            coupon.setDeleted(DeletedEnum.DELETE);
        }
        baseMapper.updateById(coupon);
        recordMapper.deleteCouponRecordStatus(couponId);
        return ResultType.SUCCESS.build();
    }

    @Override
    public Result notSaleCoupon(String couponId) {
        Coupon coupon = baseMapper.selectById(couponId);
        if (null == coupon) {
            return ResultType.RESULT_IS_NULL.build();
        }
        if (CouponStatusEnum.ON_SALE.getCode() != coupon.getCouponStatus().getCode()) {
            return ResultType.FILE_LOWER_SHELF.build();
        }
        coupon.setCouponStatus(CouponStatusEnum.OFF_SALE);
        baseMapper.updateById(coupon);
        recordMapper.updateCouponRecordStatus(couponId);
        return ResultType.SUCCESS.build();
    }

    @Override
    public Result updateCoupon(CouponUpdateDto couponUpdateDto) {
        Coupon coupon = baseMapper.selectById(couponUpdateDto.getId());
        if (coupon == null) {
            return ResultType.RESULT_IS_NULL.build();
        }
        if (CouponStatusEnum.ON_SALE.getCode() == coupon.getCouponStatus().getCode()) {
            return ResultType.COUPON_ON_SALE.build();
        }
        BeanUtils.copyProperties(couponUpdateDto, coupon);
        baseMapper.updateById(coupon);
        return ResultType.SUCCESS.build();
    }


    @Override
    @Transactional
    public Result updateCouponStatus(String couponId, CouponStatusEnum statusEnum) {
        if (null == statusEnum) {
            return ResultType.PARAM_IS_NULL.build();
        }
        Coupon coupon = baseMapper.selectById(couponId);
        if (null == coupon) {
            return ResultType.PARAM_IS_NULL.build();
        }
        boolean res;
        if (CouponStatusEnum.ON_SALE.getCode().equals(statusEnum.getCode()) &&
                CouponStatusEnum.OFF_SALE.getCode().equals(coupon.getCouponStatus().getCode())) {
            //验证商家状态
            String shopId = coupon.getShopId();
            Shop shop = shopMapper.selectById(shopId);
            if(shop == null){
                return ResultType. COUPON_SHOP_NOT_EXIST.build();
            }
            if(shop.getDeleted() == DeletedEnum.DELETE){
                return ResultType. COUPON_SHOP_NOT_EXIST.build();
            }
            if(shop.getIsFreeze() == DisableEnum.DISABLE){
                return ResultType. SHOP_IS_FREEZE.build();
            }
            if(shop.getCheckStatus() == ShopCheckEnum.UNCHECKED){
                return ResultType. SHOP_IS_CHECKING.build();
            }
            LocalDateTime time = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
            //验证发放记录是否存在
//            String s = time.toLocalDate().toString();
//            CouponRecord record = recordMapper.selectOne(new LambdaQueryWrapper<CouponRecord>().
//                    eq(CouponRecord::getGrantTime, s).
//                    eq(CouponRecord::getInvalid, DisableEnum.ENABLE).
//                    gt(CouponRecord::getTotalNum, 0).
//                    eq(CouponRecord::getCouponId, coupon.getId()));
//            if(record == null){
//                return ResultType.COUPON_RECORD_NOT_EXIST.build();
//            }

            if (coupon.getEndTime().compareTo(time) < 0) {
                return ResultType.COUPON_OUT_OF_END_TIME.build();
            }
            if (coupon.getBeginTime().compareTo(time) < 0) {
                coupon.setBeginTime(time);
            }
            res = onSellByAdmin(coupon);
        } else if (CouponStatusEnum.OFF_SALE.getCode().equals(statusEnum.getCode()) &&
                (CouponStatusEnum.ON_SALE.getCode().equals(coupon.getCouponStatus().getCode()) || CouponStatusEnum.WILL_OFF_SALE.getCode().equals(coupon.getCouponStatus().getCode()))) {
            res = offSellByAdmin(coupon);
        } else {
            return ResultType.PARAM_IS_NULL.build();
        }
        if (!res) {
            throw new FrozenException(ResultType.SYSTEM_ERROR);
        }
        return ResultType.SUCCESS.build();
    }

    @Override
    public Result<CouponVo> getCouponInfo(String couponId) {
        Coupon coupon = baseMapper.selectById(couponId);
        if (coupon == null) {
            return ResultType.RESULT_IS_NULL.build();
        }
        CouponVo vo = new CouponVo();
        vo.setCouponName(coupon.getCouponName());
        vo.setCashBack(coupon.getCashBack());
        vo.setBeginTime(coupon.getBeginTime());
        vo.setEndTime(coupon.getEndTime());
        vo.setGrantStartTime(coupon.getGrantStartTime());
        vo.setGrantEndTime(coupon.getGrantEndTime());
        vo.setGrantCollection(coupon.getGrantCollection());
        vo.setSpecialInfo(coupon.getSpecialInfo());
        vo.setDescription(coupon.getDescription());
        vo.setCouponPlatform(coupon.getCouponPlatform().getCode());
        return ResultType.SUCCESS.build().bindData(vo);
    }


    @Override
    public CouponToUserDto getCouponDetailsToUser(String couponId, Double longitude, Double latitude) {
        //查询优惠券信息
        Coupon coupon = baseMapper.selectById(couponId);
        if (!(coupon.getCouponStatus().getValue().equals(CouponStatusEnum.ON_SALE.getValue()) ||
                coupon.getCouponStatus().getValue().equals(CouponStatusEnum.WILL_OFF_SALE.getValue()))) {
            return null;
        }
        //查询店铺信息
        QueryWrapper<Shop> wrapper = new QueryWrapper<>();
        wrapper.select("id", "shop_name", "shop_head", "shop_location", "longitude", "latitude", "shop_business_hours", "is_freeze","elme_image")
                .eq("id", coupon.getShopId());
        Shop shop = shopMapper.selectOne(wrapper);
        if (shop.getIsFreeze().getValue().equals(DisableEnum.DISABLE.getValue())) {
            return null;
        }
        //查询手机号
        String phone = null;
        if(shop != null){
            MerchantDto merchant = merchantService.getMerchantById(shop.getMerchantId());
            phone = merchant != null ? merchant.getMerchantPhone() : null;
        }
        //查询发布信息
        QueryWrapper<CouponRecord> wrapper1 = new QueryWrapper<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String time = LocalDate.now().format(dateFormatter);;
//        if (LocalTime.now().compareTo(LocalTime.of(9, 0, 0)) < 0) {
//            time = LocalDate.now().plusDays(-1).format(dateFormatter);
//        } else {
//            time = LocalDate.now().format(dateFormatter);
//        }
        wrapper1.select("remainder", "total_num")
                .eq("coupon_id", coupon.getId()).eq("grant_time", time).eq("putaway_time", coupon.getPutawayTime());
        CouponRecord record = recordMapper.selectOne(wrapper1);
        if (null == record) {
            return null;
        }
        CouponToUserDto dto = new CouponToUserDto();
        dto.setShopId(shop.getId());
//        dto.setShop_business_hours(shop.getShopBusinessHours());
        dto.setShop_business_hours(coupon.getGrantStartTime().concat("-").
                concat(coupon.getGrantEndTime()));
        dto.setShopAddr(shop.getShopLocation());
        dto.setShopIcon(shop.getShopHead());
        dto.setShopName(shop.getShopName());
        dto.setLongitude(shop.getLongitude());
        dto.setLatitude(shop.getLatitude());
        dto.setActivityStartTime(coupon.getBeginTime());
        dto.setActivityEndTime(coupon.getEndTime());
        dto.setCashBack(coupon.getCashBack());
        dto.setCouponId(couponId);
        dto.setStation(coupon.getCouponPlatform());
        dto.setName(coupon.getCouponName());
        dto.setEvaluate(coupon.getEvaluationRequirements());
        dto.setRemain(record.getRemainder());
        dto.setTotal(record.getTotalNum());
        dto.setCashBack(coupon.getCashBack());
        dto.setCouponPrice(coupon.getCouponPrice());
        dto.setCouponPics(coupon.getCouponImg());
        dto.setStartTime(coupon.getGrantStartTime());
        dto.setEndTime(coupon.getGrantEndTime());
        dto.setSpecialDes(coupon.getSpecialInfo());
        dto.setProcessDes(coupon.getDescription());
        dto.setConsumeRemark(coupon.getConsumeRemark());
        dto.setMemberCashBack(coupon.getMemberCashBack());
        dto.setCouponMerchantPrice(coupon.getCouponMerchantPrice());
        dto.setElemImage(shop.getElmeImage());
        dto.setDistance(new BigDecimal(GeoUtils.getDistance(shop.getLatitude().doubleValue(),shop.getLongitude().doubleValue(),latitude,longitude)));
        dto.setPhone(phone);
        //确认是否在配送范围内
        QueryWrapper<ShopCoordinate> coordinateQueryWrapper = new QueryWrapper<>();
        coordinateQueryWrapper.eq("shop_id", shop.getId());
        coordinateQueryWrapper.eq("platform", coupon.getCouponPlatform());
        ShopCoordinate coordinate = coordinateMapper.selectOne(coordinateQueryWrapper);
        if (null == coordinate) {
            dto.setIsArrive(false);
            return dto;
        }
        dto.setAppShopId(coordinate.getAppShopId());
        List<ShopCoordinatesDto> shopCoordinate = JSONArray.parseArray(coordinate.getCoordinate(), ShopCoordinatesDto.class);
        if (null == shopCoordinate || shopCoordinate.size() == 0) {
            dto.setIsArrive(false);
            return dto;
        }
        List<Point2D.Double> doubles = shopCoordinate.stream().map(shopCoordinate1 -> GeoUtils.toPoint2D(shopCoordinate1.getLng().toString(), shopCoordinate1.getLat().toString())).collect(Collectors.toList());
        Point2D.Double location = new Point2D.Double(longitude, latitude);
        boolean isArrive = GeoUtils.isPtInPoly(location, doubles);
        dto.setIsArrive(isArrive);
        return dto;
    }

    @Override
    public IPage<CouponPageVO> getPageByCondition(Long current, Long size, String shopName, String name, Integer status, Integer platform, LocalDateTime start, LocalDateTime end) {
        long offset = (current-1)*size;
        long total = baseMapper.countNormal(shopName, status, platform, start, end);
        List<CouponPageVO> record = baseMapper.getPageByCondition(offset, size, shopName, status, platform, start, end);
        Page<CouponPageVO> page = new Page<>();
        //查询当天领取记录
        Set<String> ids = record.stream().map(e -> e.getId()).collect(Collectors.toSet());
        if (ids.size() > 0) {
            List<CouponRecord> couponRecords = recordMapper.selectList(new LambdaQueryWrapper<CouponRecord>().
                    eq(CouponRecord::getDeleted, DeletedEnum.NORMAL).
                    eq(CouponRecord::getInvalid, DisableEnum.ENABLE).
                    eq(CouponRecord::getGrantTime, LocalDate.now().toString()).
                    in(CouponRecord::getCouponId,ids));
            record.stream().forEach(e->{
                for(CouponRecord couponRecord : couponRecords){
                    if(e.getId().equals(couponRecord.getCouponId())){
                        e.setReceive(couponRecord.getReceive());
                        e.setRemainder(couponRecord.getRemainder());
                        e.setTotalNum(couponRecord.getTotalNum());
                    }
                }
            });
        }
        page.setRecords(record);
        page.setTotal(total);
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean onSellByAdmin(Coupon coupon) {
        //修改优惠券状态
        if (null == coupon) {
            return false;
        }
        //如果是即将下架改上架，需要无效之前的发放记录
        if (CouponStatusEnum.OFF_SALE.getCode() != coupon.getCouponStatus().getCode()) {
            UpdateWrapper<CouponRecord> wrapper = new UpdateWrapper<>();
            wrapper.eq("coupon_id", coupon.getId());
            CouponRecord record = new CouponRecord();
            record.setInvalid(DisableEnum.DISABLE);
            int update = recordMapper.update(record, wrapper);
            if (update < 1) {
                throw new FrozenException(ResultType.SYSTEM_ERROR);
            }
        }else {
            UpdateWrapper<CouponRecord> wrapper = new UpdateWrapper<>();
            wrapper.eq("coupon_id", coupon.getId());
            CouponRecord record = new CouponRecord();
            record.setInvalid(DisableEnum.DISABLE);
            recordMapper.update(record, wrapper);
        }
        coupon.setCouponStatus(CouponStatusEnum.ON_SALE);
        LocalDateTime now = LocalDateTime.now();
        coupon.setPutawayTime(now);
        LocalDateTime startTime = coupon.getBeginTime();
        LocalDateTime endTime = coupon.getEndTime();
        Duration duration = Duration.between(startTime, endTime);
        long l = duration.toDays();
        //添加发放记录
        if (CouponGrantRuleEnum.TOTAL_COUNT.getCode().equals(coupon.getGrantType().getCode())) {
            for (long i = 0; i <= l; i++) {
                CouponRecord record = new CouponRecord();
                record.setDeleted(DeletedEnum.NORMAL);
                record.setInvalid(DisableEnum.ENABLE);
                record.setRemainder(coupon.getGrantCountTotal());
                record.setTotalNum(coupon.getGrantCountTotal());
                record.setGrantTime(startTime.plusDays(i).toLocalDate().toString());
                record.setCouponId(coupon.getId());
                record.setPutawayTime(now);
                int insert = recordMapper.insert(record);
                if (insert < 1) {
                    throw new FrozenException(ResultType.SYSTEM_ERROR);
                }
            }
        } else if (CouponGrantRuleEnum.SAME_COUNT.getCode().equals(coupon.getGrantType().getCode())) {
            for (long i = 0; i <= l; i++) {
                CouponRecord record = new CouponRecord();
                record.setDeleted(DeletedEnum.NORMAL);
                record.setInvalid(DisableEnum.ENABLE);
                record.setRemainder(coupon.getGrantCount());
                record.setTotalNum(coupon.getGrantCount());
                record.setGrantTime(startTime.plusDays(i).toLocalDate().toString());
                record.setCouponId(coupon.getId());
                record.setPutawayTime(now);
                int insert = recordMapper.insert(record);
                if (insert < 1) {
                    throw new FrozenException(ResultType.SYSTEM_ERROR);
                }
            }
        } else if (CouponGrantRuleEnum.CUSTOM_COUNT.getCode().equals(coupon.getGrantType().getCode())) {
            String grantCollection = coupon.getGrantCollection();
            JSONArray objects = JSONArray.parseArray(grantCollection);
            List<GrantRecordSimpleDto> CouponGrantList = objects.toJavaList(GrantRecordSimpleDto.class);
            for (GrantRecordSimpleDto couponGrant : CouponGrantList) {
                CouponRecord record = new CouponRecord();
                record.setDeleted(DeletedEnum.NORMAL);
                record.setCouponId(coupon.getId());
                record.setGrantTime(couponGrant.getTime());
                record.setRemainder(couponGrant.getCount().intValue());
                record.setTotalNum(couponGrant.getCount().intValue());
                record.setInvalid(DisableEnum.ENABLE);
                record.setPutawayTime(now);
                int insert = recordMapper.insert(record);
                if (insert < 1) {
                    throw new FrozenException(ResultType.SYSTEM_ERROR);
                }
            }
        } else {
            return false;
        }
        LocalDate date = coupon.getEndTime().toLocalDate();
        LocalDate endDate = now.toLocalDate();
        if (0 == date.compareTo(endDate)||0 == date.compareTo(endDate.plusDays(1))) {
            coupon.setCouponStatus(CouponStatusEnum.WILL_OFF_SALE);
        }
        return baseMapper.updateById(coupon) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean offSellByAdmin(Coupon coupon) {
        //修改优惠券状态
        if (null == coupon) {
            return false;
        }
        coupon.setCouponStatus(CouponStatusEnum.OFF_SALE);
        int update1 = baseMapper.updateById(coupon);
        if (update1 < 1) {
            throw new FrozenException(ResultType.SYSTEM_ERROR);
        }
        //使发放记录表失效
        UpdateWrapper<CouponRecord> wrapper = new UpdateWrapper<>();
        wrapper.eq("coupon_id", coupon.getId());
        CouponRecord record = new CouponRecord();
        record.setInvalid(DisableEnum.DISABLE);
        int update = recordMapper.update(record, wrapper);
        return update > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean create(String account, CouponAddVo vo) {
        Coupon model = new Coupon();
        BeanUtils.copyProperties(vo, model);
        if (CouponGrantRuleEnum.TOTAL_COUNT.getCode().equals(vo.getGrantType())) {
            model.setGrantType(CouponGrantRuleEnum.TOTAL_COUNT);
            model.setGrantCountTotal(vo.getGrantCountTotal());
        } else if (CouponGrantRuleEnum.SAME_COUNT.getCode().equals(vo.getGrantType())) {
            model.setGrantType(CouponGrantRuleEnum.SAME_COUNT);
            model.setGrantCount(vo.getGrantCount());
        } else if (CouponGrantRuleEnum.CUSTOM_COUNT.getCode().equals(vo.getGrantType())) {
            model.setGrantType(CouponGrantRuleEnum.CUSTOM_COUNT);
            model.setGrantCollection(JSONArray.toJSONString(vo.getGrantCollection()));
        } else {
            return false;
        }
        model.setPlatformCoordinate(vo.getPlatformCoordinate());
        model.setCreateUser(account);
        model.setUpdateUser(account);
        model.setCouponStatus(CouponStatusEnum.queryByCode(vo.getCouponStatus()));
        model.setCouponPlatform(CouponPlatformEnum.queryByCode(vo.getCouponPlatform()));
        model.setEvaluationRequirements(EvaluateLevelEnum.queryByCode(vo.getEvaluationRequirements()));
        model.setCommission(vo.getCommission());
        model.setMemberCashBack(vo.getMemberCashBack());
        LocalDateTime now = LocalDateTime.now();
        model.setPutawayTime(now);
        baseMapper.insert(model);
        //如果状态为上架，添加发放记录表
        if (CouponStatusEnum.ON_SALE.getCode().equals(model.getCouponStatus().getCode())) {
            model.setCouponStatus(CouponStatusEnum.OFF_SALE);
            onSellByAdmin(model);
        }
        return true;
    }

    @Override
    @Transactional
    public Result updateById(String account, String id, CouponAddVo vo) {
        Coupon model = baseMapper.selectById(id);
        BeanUtils.copyProperties(vo, model);
        model.setEvaluationRequirements(EvaluateLevelEnum.queryByCode(vo.getEvaluationRequirements()));
        if (CouponGrantRuleEnum.TOTAL_COUNT.getCode().equals(vo.getGrantType())) {
            model.setGrantType(CouponGrantRuleEnum.TOTAL_COUNT);
            model.setGrantCountTotal(vo.getGrantCountTotal());
        } else if (CouponGrantRuleEnum.SAME_COUNT.getCode().equals(vo.getGrantType())) {
            model.setGrantType(CouponGrantRuleEnum.SAME_COUNT);
            model.setGrantCount(vo.getGrantCount());
        } else if (CouponGrantRuleEnum.CUSTOM_COUNT.getCode().equals(vo.getGrantType())) {
            model.setGrantType(CouponGrantRuleEnum.CUSTOM_COUNT);
            model.setGrantCollection(JSONArray.toJSONString(vo.getGrantCollection()));
        } else {
            return ResultType.PARAM_IS_NULL.build();
        }
        model.setPlatformCoordinate(vo.getPlatformCoordinate());
        model.setCreateUser(account);
        model.setUpdateUser(account);
        model.setCouponPlatform(CouponPlatformEnum.queryByCode(vo.getCouponPlatform()));
//        model.setEvaluationRequirements(EvaluateLevelEnum.queryByCode(vo.getEvaluationRequirements()));
        //如果上下架状态更改，同步更新发放记录
        boolean res;
        if (null != vo.getCouponStatus()) {
            if (CouponStatusEnum.ON_SALE.getCode().equals(vo.getCouponStatus()) || CouponStatusEnum.WILL_OFF_SALE.getCode().equals(vo.getCouponStatus())) {
                //判断是否即将下架
                if (LocalDate.now().compareTo(model.getEndTime().toLocalDate()) < 0) {
                    model.setCouponStatus(CouponStatusEnum.ON_SALE);
                }
                if (LocalDate.now().compareTo(model.getEndTime().toLocalDate()) == 0) {
                    model.setCouponStatus(CouponStatusEnum.WILL_OFF_SALE);
                }
                res = onSellByAdmin(model);
            } else if (CouponStatusEnum.OFF_SALE.getCode() == vo.getCouponStatus()) {
                res = offSellByAdmin(model);
            } else {
                return ResultType.PARAM_IS_NULL.build();
            }
            if (!res) {
                throw new FrozenException(ResultType.SYSTEM_ERROR);
            }
        }else{
            int update = baseMapper.updateById(model);
            return update > 0 ? ResultType.SUCCESS.build() : ResultType.SYSTEM_ERROR.build();
        }
        return ResultType.SUCCESS.build();
    }

    @Override
    public CouponStatisticVo getCouponStatistic(){
        /*1.优惠券今日新增数量*/
        LocalDate now = LocalDate.now();
        LocalDateTime begin = LocalDateTime.of(now, LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(now, LocalTime.MAX);
        Integer increaseNum = baseMapper.selectCount(new LambdaQueryWrapper<Coupon>().
                eq(Coupon::getDeleted, DeletedEnum.NORMAL).
                ge(Coupon::getCreateTime, begin).
                le(Coupon::getCreateTime, end));
        /*2.查询优惠券总数*/
        List<Coupon> merchants = baseMapper.selectList(new LambdaQueryWrapper<Coupon>().
                eq(Coupon::getDeleted, DeletedEnum.NORMAL));
        Integer totalNum = merchants != null ? merchants.size() : 0;
        /*3.上架优惠券数量*/
        Long enableNum = merchants.stream().filter(e->e.getCouponStatus() == CouponStatusEnum.ON_SALE ||
                e.getCouponStatus() == CouponStatusEnum.WILL_OFF_SALE).count();
        CouponStatisticVo vo = new CouponStatisticVo();
        vo.setIncreaseNum(increaseNum);
        vo.setTotal(totalNum);
        vo.setEnableNum(enableNum.intValue());
        return vo;
    }

    @Override
    public List<CouponDto> getCouponList(Set<String> ids) {
        if (ids != null && ids.size() > 0) {
            List<Coupon> coupons = baseMapper.selectList(new LambdaQueryWrapper<Coupon>().
                    select(Coupon::getId, Coupon::getCouponName, Coupon::getCommission,Coupon::getMemberCashBack).
                    in(Coupon::getId, ids));
            return coupons.stream().map(e->{
                CouponDto dto = new CouponDto();
                dto.setId(e.getId());
                dto.setCouponName(e.getCouponName());
                dto.setCommission(e.getCommission());
                dto.setMemberCashBack(e.getMemberCashBack());
                return dto;
            }).collect(Collectors.toList());
        }
        return new ArrayList<>(0);
    }

    @Override
    public int getCountCount(LocalDateTime begin, LocalDateTime end, CouponStatusEnum statusEnum) {
        Integer integer = baseMapper.selectCount(new LambdaQueryWrapper<Coupon>().
                eq(Coupon::getDeleted, DeletedEnum.NORMAL).
                eq(statusEnum != null, Coupon::getCouponStatus, statusEnum).
                ge(begin != null, Coupon::getCreateTime, begin).
                lt(end != null, Coupon::getCreateTime, end));
        return integer;
    }

    @Override
    public Boolean recommend(String account, String id, Boolean recommend){
        Coupon coupon = baseMapper.selectById(id);
        if(coupon == null){
            return false;
        }
        Coupon c = new Coupon();
        c.setId(id);
        c.setUpdateUser(account);
        c.setRecommend(recommend);
        int i = baseMapper.updateById(c);
        return i==1 ? true : false;
    }

    @Override
    public void testCoupon(){
        //开始查询所有需要下架的优惠券
        QueryWrapper<Coupon> wrapper = new QueryWrapper<>();
        wrapper.select("id", "grant_start_time", "grant_end_time", "end_time");
        wrapper.lt("end_time", LocalDateTime.of(LocalDate.now(), LocalTime.MIN));
        wrapper.ge("begin_time", LocalDateTime.of(LocalDate.now(), LocalTime.MIN).plusDays(-10));
        wrapper.and(andWrapper -> andWrapper.eq("coupon_status", CouponStatusEnum.ON_SALE).or().eq("coupon_status", CouponStatusEnum.WILL_OFF_SALE));
        List<Coupon> list = baseMapper.selectList(wrapper);
        for (Coupon coupon : list) {
            UpdateWrapper<Coupon> couponWrapper = new UpdateWrapper<>();
            couponWrapper.eq("id", coupon.getId());
            coupon.setCouponStatus(CouponStatusEnum.OFF_SALE);
            baseMapper.update(coupon, couponWrapper);
            //使发放记录表失效
            UpdateWrapper<CouponRecord> recordQueryWrapper = new UpdateWrapper<>();
            recordQueryWrapper.eq("grant_time", LocalDate.now().plusDays(-1).toString());
            CouponRecord record = new CouponRecord();
            record.setInvalid(DisableEnum.DISABLE);
            recordMapper.update(record, recordQueryWrapper);
        }
    }

    @Override
    public Boolean timeGrant(String couponId, Boolean isTiming) {
        Coupon coupon = baseMapper.selectOne(new LambdaQueryWrapper<Coupon>().
                eq(Coupon::getDeleted, DeletedEnum.NORMAL.getCode()).
                eq(Coupon::getId, couponId));
        if(Objects.isNull(coupon)){
            throw new BizException("该优惠券不存在");
        }
        if(CouponStatusEnum.ON_SALE != coupon.getCouponStatus()){
            throw new BizException("该优惠券未上架");
        }
        //定时发放只适用于优惠券每日发放
        if(CouponGrantRuleEnum.SAME_COUNT != coupon.getGrantType()){
            throw new BizException("该功能只适用于优惠券每日等量发放");
        }
        //定时发布不为空时 进行更新操作
        if(Objects.nonNull(isTiming)){
            coupon.setIsTiming(isTiming);
            int i = baseMapper.updateById(coupon);
            return i == 1;
        }
        //如果isTiming 为空验证优惠券 原始定时发放状态 并进行修改
        Boolean isTiming1 = coupon.getIsTiming();
        if(isTiming1){
            coupon.setIsTiming(false);
            coupon.setCouponStatus(CouponStatusEnum.OFF_SALE);
            baseMapper.updateById(coupon);
            //无效优惠券记录
            List<String> list = new ArrayList<>(1);
            list.add(couponId);
            recordService.updateOldCouponRecord(list);
            return false;
        }
        coupon.setIsTiming(true);
        baseMapper.updateById(coupon);
        return true;
    }

    @Override
    public List<Coupon> getTimingCoupons() {
        return baseMapper.selectList(new LambdaQueryWrapper<Coupon>().
                eq(Coupon::getDeleted, DeletedEnum.NORMAL.getCode()).
                eq(Coupon::getIsTiming, true).
                eq(Coupon::getCouponStatus, CouponStatusEnum.ON_SALE.getCode()));
    }

    /**
     * 查询详情
     *
     * @param id id
     * @return coupon
     */
    @Override
    public Coupon getCoupon(String id) {
        Coupon coupon = baseMapper.selectOne(new LambdaQueryWrapper<Coupon>().
                eq(Coupon::getDeleted, DeletedEnum.NORMAL.getCode()).
                eq(Coupon::getId, id));
        return coupon;
    }

    @Override
    public List<Coupon> getCoupons(String shopId) {
        return baseMapper.selectList(new LambdaQueryWrapper<Coupon>().
                eq(Coupon::getDeleted,DeletedEnum.NORMAL.getCode()).
                eq(Coupon::getShopId,shopId));
    }

    @Override
    public Boolean updateById(String id){
        Coupon coupon = getCoupon(id);
        LocalDateTime endTime = coupon.getEndTime();
        LocalDateTime end = endTime.plusDays(-1);
        end = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23, 59, 59);
        coupon.setEndTime(end);
        baseMapper.updateById(coupon);
        return false;
    }

    /**
     * 批量改优惠券结束时间
     */
    @Override
    public Boolean updateBatchById() {
        List<Coupon> coupons = baseMapper.selectList(new LambdaQueryWrapper<Coupon>().
                eq(Coupon::getDeleted, DeletedEnum.NORMAL.getCode()));
        for (Coupon coupon : coupons) {
            LocalDateTime endTime = coupon.getEndTime();
            LocalDateTime end = endTime.plusDays(-2);
            end = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23, 59, 59);
            coupon.setEndTime(end);
        }
        return updateBatchById(coupons);
    }

    @Override
    public List<StatisticsDTO> getCouponNum(String collectionName, LocalDateTime begin, LocalDateTime end){
        if(Constant.MongoCollection.DAY_COUPON.equals(collectionName)){
            LocalDateTime beginTime = LocalDateTime.of(begin.toLocalDate(), LocalTime.MIN);
            LocalDateTime endTime = end.with(LocalTime.parse("23:59:59"));
            List<Coupon> coupons = baseMapper.selectList(new LambdaQueryWrapper<Coupon>().
                    select(Coupon::getId, Coupon::getCreateTime).
                    eq(Coupon::getDeleted, DeletedEnum.NORMAL).
                    le(Coupon::getCreateTime, endTime).
                    ge(Coupon::getCreateTime, beginTime));
            if (CollectionUtils.isEmpty(coupons)) {
                return new ArrayList<>(0);
            }
            List<CouponDto> dtos = new ArrayList<>(100);
            for (Coupon coupon : coupons) {
                CouponDto dto = new CouponDto();
                dto.setId(coupon.getId());
                dto.setCreateTime(coupon.getCreateTime().toLocalDate().toString());
                dtos.add(dto);
            }
            Map<String, List<CouponDto>> collect = dtos.stream().collect(Collectors.groupingBy(CouponDto::getCreateTime));
            Set<Map.Entry<String, List<CouponDto>>> entries = collect.entrySet();
            ArrayList<StatisticsDTO> list = new ArrayList<>(entries.size());
            List<String> timeStr = LocalDateTimeUtil.getTimeStr(begin, end, 0);
            for (String time : timeStr) {
                StatisticsDTO dto = new StatisticsDTO();
                dto.setDate(time);
                for (Map.Entry<String, List<CouponDto>> str : entries) {
                    if (time.equals(str.getKey())) {
                        List<CouponDto> couponDtos = collect.get(str.getKey());
                        dto.setNum(couponDtos.size());
                    }
                }
                list.add(dto);
            }
            return list;
        }else {
            List<Coupon> coupons = baseMapper.selectList(new LambdaQueryWrapper<Coupon>().
                    select(Coupon::getId, Coupon::getCreateTime).
                    eq(Coupon::getDeleted, DeletedEnum.NORMAL));
            if (CollectionUtils.isEmpty(coupons)) {
                return new ArrayList<>(0);
            }
            List<CouponDto> dtos = new ArrayList<>(100);
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM");
            for (Coupon coupon : coupons) {
                CouponDto dto = new CouponDto();
                dto.setId(coupon.getId());
                dto.setCreateTime(df.format(coupon.getCreateTime()));
                dtos.add(dto);
            }
            Map<String, List<CouponDto>> collect = dtos.stream().collect(Collectors.groupingBy(CouponDto::getCreateTime));
            Set<Map.Entry<String, List<CouponDto>>> entries = collect.entrySet();
            ArrayList<StatisticsDTO> list = new ArrayList<>(entries.size());
            List<String> timeStr = LocalDateTimeUtil.getTimeStr(begin, end, 1);

            for (String time : timeStr) {
                StatisticsDTO dto = new StatisticsDTO();
                dto.setDate(time);
                for (Map.Entry<String, List<CouponDto>> str : entries) {
                    if (time.equals(str.getKey())) {
                        List<CouponDto> couponDtos = collect.get(str.getKey());
                        dto.setNum(couponDtos.size());
                    }
                }
                list.add(dto);
            }
            return list;
        }
    }
}

