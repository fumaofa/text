package com.taotao.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("user/info")
@Controller
public class UserInfoController {
	
	@RequestMapping(value="my-info-img",method=RequestMethod.GET)
	public ModelAndView savaUserImg(){
		ModelAndView mv=new ModelAndView("my-info-img");
		return mv;
	}
}
