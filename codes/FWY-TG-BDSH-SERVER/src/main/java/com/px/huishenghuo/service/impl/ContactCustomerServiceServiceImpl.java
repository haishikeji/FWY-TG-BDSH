package com.px.huishenghuo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.huishenghuo.entity.ContactCustomerService;
import com.px.huishenghuo.mapper.ContactCustomerServiceMapper;
import com.px.huishenghuo.service.IContactCustomerServiceService;
import org.springframework.stereotype.Service;
import java.util.Collection;

/**
 * @@author 品讯科技
 * @Description:  服务实现类
 * @Date: 2020-01-02
 **/
@Service
public class ContactCustomerServiceServiceImpl extends ServiceImpl<ContactCustomerServiceMapper,ContactCustomerService> implements IContactCustomerServiceService {
    @Override
    public boolean saveBatch(Collection<ContactCustomerService> entityList) {
        return false;
    }

    @Override
    public String contactCustomerService() {
        return baseMapper.contactCustomerService();
    }
}

