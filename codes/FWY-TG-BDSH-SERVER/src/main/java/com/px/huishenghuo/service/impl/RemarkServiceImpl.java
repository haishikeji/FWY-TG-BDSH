package com.px.huishenghuo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.huishenghuo.entity.Remark;
import com.px.huishenghuo.mapper.RemarkMapper;
import com.px.huishenghuo.service.IRemarkService;
import org.springframework.stereotype.Service;
import java.util.Collection;

/**
 * @@author 品讯科技
 * @Description:  服务实现类
 * @Date: 2020-06-03
 **/
@Service
public class RemarkServiceImpl extends ServiceImpl<RemarkMapper,Remark> implements IRemarkService {
    @Override
    public boolean saveBatch(Collection<Remark> entityList) {
        return false;
    }

}

