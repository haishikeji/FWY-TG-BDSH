package com.px.huishenghuo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.px.huishenghuo.dto.UserMessageDto;
import com.px.huishenghuo.entity.UserMessage;

import java.util.List;

/**
 * @@author 品讯科技
 * @Description: 用户通知消息 服务类
 * @Date: 2020-06-03
 **/
public interface IUserMessageService extends IService<UserMessage> {

    /**
     * 添加用户消息
     *
     * @param dtos 对象
     */
    void addUserMessage(List<UserMessageDto> dtos);
}
