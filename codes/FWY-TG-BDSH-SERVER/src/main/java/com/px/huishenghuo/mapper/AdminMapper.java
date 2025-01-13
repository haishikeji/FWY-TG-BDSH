package com.px.huishenghuo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.px.huishenghuo.entity.Admin;

import java.util.List;


/**
 * @@author 品讯科技
 * @Description: 管理平台用户 Mapper 接口
 * @Date: 2020-04-11
 **/
@Repository
public interface AdminMapper extends BaseMapper<Admin> {

    Page getByCondition(Page page, @Param("id") String id, @Param("name") String name, @Param("phone") String phone,
                        @Param("roleLevel") Integer roleLevel, @Param("disabled") Integer disabled);

    Integer deletedById(@Param("updatePhone") String updatePhone, @Param("id") String id);

    List<String> selectOnLineAuditors();
}
