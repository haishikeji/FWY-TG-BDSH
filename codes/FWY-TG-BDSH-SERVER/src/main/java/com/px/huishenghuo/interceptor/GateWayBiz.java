package com.px.huishenghuo.interceptor;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author 品讯科技
 */
@Service
public class GateWayBiz {

	@Resource
	private RedisTemplate<String, Object> redisDao;


	/**
	 * 验证是否重复请求
	 * @param content content
	 * @param token   token
	 * @param uri     uri
	 * @return        boolean
	**/
	public boolean isRepeatRequest(String token, String uri, String content) {
		boolean repeat = false;
		String key = generateKey(token, uri);
		if(redisDao.opsForValue().get(key) != null
				&& redisDao.opsForValue().get(key).equals(content)) {
			repeat = true;
		}

		return repeat;
	}

	/**
	 * 保存用户请求地址
	 * @param token    token
	 * @param uri      uri
	 * @param content  content
	**/
	public void saveLastRequest(String token, String uri, String content) {
		String key = generateKey(token, uri);
		redisDao.opsForValue().set(key, content, 500, TimeUnit.MILLISECONDS);
	}
	
	private String generateKey(String token, String uri) {
		return token + "_" + uri;
	}
}
