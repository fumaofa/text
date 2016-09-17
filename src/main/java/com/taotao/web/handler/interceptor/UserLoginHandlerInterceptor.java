package com.taotao.web.handler.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.utils.CookieUtils;
import com.taotao.web.controller.UserController;
import com.taotao.web.pojo.User;
import com.taotao.web.service.UserService;
import com.taotao.web.threadlocal.UserThreadLocal;

public class UserLoginHandlerInterceptor implements HandlerInterceptor{
	
	@Autowired
	private UserService userService;
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String ticket=CookieUtils.getCookieValue(request, UserController.TT_TICKET);
		if(StringUtils.isEmpty(ticket)){
			//用户没有登入跳转到登入页面
			response.sendRedirect("/user/login.html");
			UserThreadLocal.set(null);
			return false;
		}
		
		//检查cookie是否有效
		User user = userService.queryUserByTicket(ticket);
		if(user==null){
			//登录已失效
            response.sendRedirect("/user/login.html");
            UserThreadLocal.set(null);
            return false;
		}
		//登入成功就放入线程
		UserThreadLocal.set(user);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
