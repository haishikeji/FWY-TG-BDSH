package com.px.huishenghuo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.huishenghuo.entity.MerchantWallet;
import com.px.huishenghuo.mapper.MerchantWalletMapper;
import com.px.huishenghuo.service.IMerchantWalletService;
import org.springframework.stereotype.Service;
import java.util.Collection;

/**
 * @@author 品讯科技
 * @Description: 商户钱包表 服务实现类
 * @Date: 2020-03-13
 **/
@Service
public class MerchantWalletServiceImpl extends ServiceImpl<MerchantWalletMapper,MerchantWallet> implements IMerchantWalletService {
    @Override
    public boolean saveBatch(Collection<MerchantWallet> entityList) {
        return false;
    }

}

