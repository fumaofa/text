package com.taotao.sso.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.RedisService;
import com.taotao.sso.mapper.UserMapper;
import com.taotao.sso.pojo.User;

@Service
public class UserService {
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private RedisService redisService;
	
	private static final Map<Integer, Boolean> TYPES=new HashMap<>();  
	
	private static final ObjectMapper MAPPER=new ObjectMapper();
	
	static{
		TYPES.put(1, true);
		TYPES.put(2, true);
		TYPES.put(3, true);
	}
	
	/**
	 * 数据校验
	 * @param param
	 * @param type
	 * @return
	 */
	public Boolean check(String param, Integer type)throws Exception {
		if(!TYPES.containsKey(type)){
			throw new Exception("参数合法！只能是1，2，3");
		}
		User user=new User();
		
		switch (type) {
		case 1:
			user.setUsername(param);
			break;
		case 2:
			user.setPhone(param);
			break;
		case 3:
			user.setEmail(param);
			break;
		default:
			break;
		}
		return userMapper.selectOne(user)==null;
	}
	
	/**
	 * 注册用户
	 * @param user
	 */
	public void register(User user) {
		user.setCreated(new Date());
		user.setUpdated(user.getCreated());
		user.setPassword(DigestUtils.md5Hex(user.getPassword()));
		userMapper.insertSelective(user);
	}
	
	/**
	 * 登入用户
	 * @param userName
	 * @param password
	 * @return
	 */
	public String login(String userName, String password)throws Exception {
		User user=new User();
		user.setUsername(userName);
		User selectOne = userMapper.selectOne(user);
		if(!StringUtils.equals(selectOne.getPassword(), DigestUtils.md5Hex(password))){
			return null;
		}
		String ticket=DigestUtils.md5Hex(System.currentTimeMillis()+userName);
		redisService.set(ticket, MAPPER.writeValueAsString(selectOne), 1800);
		return ticket;
	}
	
	/**
	 * 根据ticket查询是否登入
	 * @param ticket
	 * @return
	 */
	public User queryByTicket(String ticket)throws Exception {
		String string = redisService.get(ticket);
		if(string==null){
			return null;
		}
		//如果用户登录了，每查询一次ticket,必须保存刷新生命周期
        this.redisService.expire(ticket, 1800);
		return MAPPER.readValue(string, User.class);
	}
	
	
}
