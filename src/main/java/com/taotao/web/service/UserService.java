package com.taotao.web.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.bean.HttpResult;
import com.taotao.common.service.ApiService;
import com.taotao.web.pojo.User;

@Service
public class UserService {

	@Autowired
	private ApiService apiService;

	@Value("${SSO_TAOTAO_URL}")
	private String SSO_TAOTAO_URL;

	private static final ObjectMapper MAPPER = new ObjectMapper();

	/**
	 * 注册用户
	 * 
	 * @param user
	 * @return
	 */
	public Boolean doRegister(User user) {
		String url = SSO_TAOTAO_URL + "/user/register";
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("username", user.getUsername());
		param.put("phone", user.getPhone());
		param.put("password", user.getPassword());
		try {
			HttpResult doPost = apiService.doPost(url, param);
			if (doPost.getCode() == 201) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 登入用户
	 * 
	 * @param user
	 * @return
	 */
	public String doLogin(User user) {
		String url = SSO_TAOTAO_URL + "/user/login";
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("u", user.getUsername());
		param.put("p", user.getPassword());
		try {
			HttpResult doPost = apiService.doPost(url, param);
			if (doPost.getCode() == 200) {
				String body = doPost.getBody();
				if (body == null) {
					return null;
				}
				return body;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据ticket查询用户
	 * @param ticket
	 * @return
	 */
	public User queryUserByTicket(String ticket) {
		String url = SSO_TAOTAO_URL + "/user/" + ticket;
		try {
			String jsonData = this.apiService.doGet(url,null);
			if (jsonData == null) {
				return null;
			}
			return MAPPER.readValue(jsonData, User.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
