package com.px.huishenghuo.service.impl;

import com.px.huishenghuo.constant.NoticeResultEnum;
import com.px.huishenghuo.constant.NoticeStatusEnum;
import com.px.huishenghuo.constant.NoticeTypeEnum;
import com.px.huishenghuo.entity.NoticeCenter;
import com.px.huishenghuo.service.INoticeCenterService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;

/**
 * 〈${DESCRIPTION}〉
 *
 * @author 品讯科技
 * @date 2024-08
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class NoticeCenterServiceImplTest {

    @Autowired
    private INoticeCenterService service;

    @Test
    public void addNoticeCenter() {
        NoticeCenter center = new NoticeCenter();
        center.setNoticeType(NoticeTypeEnum.SYSTEM_NOTICE);
        center.setNoticeTime(LocalDateTime.now());
        center.setIsRead(0);
        center.setNoticeText("系统测试");
        center.setNoticeTitle("系统测试消息");
        center.setUserId("2022-02-28");
        center.setNoticeResult(NoticeResultEnum.OPERATION_FAILED);
        center.setNoticeStatus(NoticeStatusEnum.APPLY_REFUND);
        center.setCreateUser("admin");
        center.setUpdateUser("admin");
        service.addNoticeCenter(center);
    }
}