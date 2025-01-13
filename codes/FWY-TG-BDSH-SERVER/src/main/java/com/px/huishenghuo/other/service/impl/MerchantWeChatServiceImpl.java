package com.px.huishenghuo.other.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.px.huishenghuo.constant.Constant;
import com.px.huishenghuo.constant.WxMsgTempEnum;
import com.px.huishenghuo.entity.Merchant;
import com.px.huishenghuo.mapper.MerchantMapper;
import com.px.huishenghuo.mapper.MerchantWalletMapper;
import com.px.huishenghuo.redis.RedisKeyUtil;
import com.px.huishenghuo.result.Result;
import com.px.huishenghuo.result.ResultType;
import com.px.huishenghuo.util.AESUtils;
import com.px.huishenghuo.util.MD5Util;
import com.px.huishenghuo.other.Dto.OrderPayByWeChatDto;
import com.px.huishenghuo.other.service.WeChatService;
import com.px.huishenghuo.other.util.WechatRequestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 品讯科技
 * @description 微信相关业务实现代码
 * @datetime 2020-03-18 21:10
 */
@Service("merchant")
public class MerchantWeChatServiceImpl implements WeChatService {

    @Value("wechat.merchant.appId")
    private String appId;

    @Value("wechat.merchant.appSecret")
    private String appSecret;

    @Resource
    private MerchantMapper merchantMapper;

    @Resource
    private MerchantWalletMapper walletMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private WechatRequestUtils requestUtils;

    @Override
    public Map<String, String> getOpenId(String code) throws IOException {
        HashMap<String, String> param = new HashMap<>();
        param.put("appid",appId);
        param.put("secret",appSecret);
        param.put("code",code);
        return requestUtils.getOpenId(param);
    }

    @Override
    public Result login(String code, String distributionId){
        try {
            Map<String, String> res = getOpenId(code);
            Integer errCode = Integer.valueOf(res.get("errcode"));
            String openid = res.get("openid");
            HashMap<String, String> map = new HashMap<>();
            if (0 == errCode) {
                //查询是否有此用户
                QueryWrapper<Merchant> wrapper = new QueryWrapper<>();
                wrapper.eq("open_id", openid);
                Merchant one = merchantMapper.selectOne(wrapper);
                if (null == one) {//没有此微信绑定的商家账号
                    return ResultType.ACCOUNT_NOT_EXIST.build();
                }
                //登录换取token
                String encode = MD5Util.encode(one.getId());
                String token = AESUtils.enCode(Constant.Token.ENCODE_RULE, encode);
                //存入redis,设置默认过期时间
                redisTemplate.opsForValue().set(RedisKeyUtil.getMerchantLoginTokenKey(encode), one.getId(), 30L, TimeUnit.MINUTES);
                map.put("token", token);
                return ResultType.SUCCESS.build().bindData(map);
            } else if (40029 == errCode) {
                return ResultType.WX_CODE_INVALID.build();
            } else if (45011 == errCode) {
                return ResultType.WX_CALL_LIMIT.build();
            } else {
                return ResultType.WX_AUTHORIZATION_FAIL.build();
            }
        }catch (Exception e) {
            return ResultType.WX_AUTHORIZATION_FAIL.build();
        }
    }

    @Override
    public Result createOrder(OrderPayByWeChatDto pay) throws Exception {
        return null;
    }

    @Override
    public Result getFlag(String code) {
        try {
            //封装授权请求参数
            Map<String, String> res = getOpenId(code);
            //根据结果errcode确认是否获取到openid
            Integer errCode = Integer.valueOf(res.get("errcode"));
            String openid = res.get("openid");
            HashMap<String, String> map = new HashMap<>();
            if (0 == errCode) {
                map.put("openId", openid);
                return ResultType.SUCCESS.build().bindData(map);
            } else if (40029 == errCode) {
                return ResultType.WX_CODE_INVALID.build();
            } else if (45011 == errCode) {
                return ResultType.WX_CALL_LIMIT.build();
            } else {
                return ResultType.WX_AUTHORIZATION_FAIL.build();
            }
        } catch (Exception e) {
            return ResultType.WX_AUTHORIZATION_FAIL.build();
        }
    }

    @Override
    public Result sendMsg(String id, WxMsgTempEnum template, Map<String, Object> map) {
        return null;
    }

}

