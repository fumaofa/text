package com.taotao.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.utils.CookieUtils;
import com.taotao.web.pojo.User;
import com.taotao.web.service.UserService;

@RequestMapping("user")
@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	public static final String TT_TICKET="TT_TICKET";
	/**
	 * 跳转到注册页面
	 * @return
	 */
	@RequestMapping(value="register",method=RequestMethod.GET)
	public String register(){
		return "register";
	}
	/**
	 * 跳转到登入页面
	 */
	@RequestMapping(value="login",method=RequestMethod.GET)
	public String login(){
		return "login";
	}
	/**
	 * 跳转到个人信息页面
	 */
	@RequestMapping(value="my-info",method=RequestMethod.GET)
	public String inof(){
		return "my-info";
	}
	
	/**
	 * 注册用户
	 */
	@RequestMapping(value="doRegister",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> doRegister(User user){
		Boolean bool=userService.doRegister(user);
		Map<String, Object> resultMap=new HashMap<>(1);
		if(bool){
			resultMap.put("status", "200");
		}else{
			resultMap.put("status", "500");
		}
		return resultMap; 
	}
	
	/**
	 * 用户登入
	 */
	@RequestMapping(value="doLogin",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> doLogin(User user,HttpServletRequest request,HttpServletResponse response){
		String ticket =userService.doLogin(user);
		Map<String, Object> resultMap=new HashMap<>(1);
		if(ticket ==null){
			resultMap.put("status","500");
		}else{
			resultMap.put("status", "200");
			CookieUtils.setCookie(request, response, TT_TICKET, ticket, 60*60, true);
		}
		return resultMap;
	}
}
