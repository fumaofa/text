package com.taotao.sso.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.sso.pojo.User;
import com.taotao.sso.service.UserService;

@RequestMapping("user")
@Controller
public class UserController {

	@Autowired
	private UserService userService;

	/**
	 * 检验数据是否可以用
	 * 
	 * @param param
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "check/{param}/{type}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Boolean> check(@PathVariable("param") String param,
			@PathVariable("type") Integer type) {
		try {
			Boolean bool = userService.check(param, type);
			return ResponseEntity.ok(bool);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
				null);
	}

	/**
	 * 注册用户
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "register", method = RequestMethod.POST)
	public ResponseEntity<String> register(@Valid User user,BindingResult result) {
		if(result.hasErrors()){
			List<Object> list=new ArrayList<>();
			//取出所有的检验错误信息
			List<ObjectError> objectErrors=result.getAllErrors();
			for (ObjectError error : objectErrors) {
				String defaultMessage = error.getDefaultMessage();
				list.add(defaultMessage);
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StringUtils.join(list,"|"));
		}
		try {
			userService.register(user);
			return ResponseEntity.status(HttpStatus.CREATED).body(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	/**
	 * 登入用户
	 * @param userName
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "login", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> login(@RequestParam("u") String userName,
			@RequestParam("p") String password) {
		try{
			String ticket=userService.login(userName,password);
			if(ticket==null){
				return ResponseEntity.ok(null);
			}
			return ResponseEntity.status(HttpStatus.OK).body(ticket);
		}catch(Exception e){
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	/**
	 * 根据传来ticket验证是否登入
	 * @param ticket
	 * @return
	 */
	@RequestMapping(value="{ticket}",method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<User> ticket(@PathVariable("ticket")String ticket){
		try{
			User user=userService.queryByTicket(ticket);
			if(user==null){
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
			return ResponseEntity.status(HttpStatus.OK).body(user);
		}catch(Exception e){
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
}
