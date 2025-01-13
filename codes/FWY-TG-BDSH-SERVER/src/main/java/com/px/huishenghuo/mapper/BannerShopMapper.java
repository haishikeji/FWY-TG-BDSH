package com.px.huishenghuo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.px.huishenghuo.dto.ShopSimpleNameDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.px.huishenghuo.entity.BannerShop;

import java.util.List;


/**
 * @@author 品讯科技
 * @Description:  Mapper 接口
 * @Date: 2020-04-27
 **/
@Repository
public interface BannerShopMapper extends BaseMapper<BannerShop> {

    List<ShopSimpleNameDto> getBanners(@Param("shopId") String shopId);

    List<ShopSimpleNameDto> getShops(@Param("bannerId") String bannerId);
}
